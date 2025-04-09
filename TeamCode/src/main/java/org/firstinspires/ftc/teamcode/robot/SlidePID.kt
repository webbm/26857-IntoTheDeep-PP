package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.cos

@Config
class SlidePID(hardwareMap: HardwareMap) {
    private val slideRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "right_slide")
    private val slideLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "left_slide").apply {
        direction = DcMotorSimple.Direction.REVERSE
    }

    enum class Position(val value: Double) {
        RETRACTED(0.0),
        LOW_BASKET(500.0),
        HIGH_BASKET(900.0);
    }

    init {
        slideRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        slideLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slideLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        setTarget(Position.RETRACTED.value)
    }

    private val controller = PIDController(p, i, d)
    private val ticksInDegree = 12.19

    var target = 0.0
        private set
    
    var isManualControl = false
        private set
    
    companion object {
        @JvmField var p = -0.014
        @JvmField var i = 0.0
        @JvmField var d = 0.0
        @JvmField var f = 0.0
        
        @JvmField var MAX_EXTENSION = 1000.0
        @JvmField var MIN_EXTENSION = -20.0
    }

    fun setTarget(targetPosition: Double) {
        // Constrain target to valid range
        target = when {
            targetPosition > MAX_EXTENSION -> MAX_EXTENSION
            targetPosition < MIN_EXTENSION -> MIN_EXTENSION
            else -> targetPosition
        }
        isManualControl = false
    }
    
    fun setTarget(position: Position) {
        target = position.value
        isManualControl = false
    }
    
    fun setManualPower(power: Double) {
        // Only allow manual control within the valid range
        val currentPosition = getCurrentPosition().toDouble()
        val safePower = when {
            // Don't allow extending past MAX_EXTENSION
            currentPosition >= MAX_EXTENSION && power > 0 -> 0.0
            // Don't allow retracting past MIN_EXTENSION
            currentPosition <= MIN_EXTENSION && power < 0 -> 0.0
            else -> power
        }

        slideLeft.power = safePower
        slideRight.power = safePower
        isManualControl = true
    }
    
    fun update() {
        // Skip PID update if in manual control mode
        if (isManualControl) return
        
        // Update PID controller with latest coefficients
        controller.setPID(p, i, d)
        
        // Get current position
        val currentPosition = slideLeft.currentPosition
        
        // Calculate PID output
        val pid = controller.calculate(currentPosition.toDouble(), target)
        
        // Calculate feed-forward term
        val ff = cos(Math.toRadians(target / ticksInDegree)) * f
        
        // Combine for final power
        val power = pid + ff
        
        // Apply power to both motors
        slideLeft.power = power
        slideRight.power = power
    }
    
    fun getCurrentPosition(): Int {
        return slideLeft.currentPosition
    }

    fun isBusy(): Boolean {
        val currentPosition = slideLeft.currentPosition.toDouble()
        // Consider the slide "at target" when within 20 ticks
        return Math.abs(currentPosition - target) > 20 && !isManualControl
    }
}
