package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.controller.PIDController
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.math.cos

@Config
class PivotPID(hardwareMap: HardwareMap) {
    private val pivotRight: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "right_pivot")
    private val pivotLeft: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "left_pivot").apply {
        direction = DcMotorSimple.Direction.REVERSE
    }

    enum class Position(val value: Double) {
        SCORING_POSITION(-1000.0),
        FLOOR_POSITION(0.0);
    }

    init {
        pivotRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        pivotRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        pivotLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        pivotLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        setTarget(Position.FLOOR_POSITION)
    }

    private val controller = PIDController(p, i, d)
    private val ticksInDegree = 12.19

    var target = 0.0
        private set
    
    companion object {
        @JvmField var p = -0.008
        @JvmField var i = 0.0
        @JvmField var d = 0.0
        @JvmField var f = 0.0
    }

    fun setTarget(targetPosition: Double) {
        target = targetPosition
    }
    
    fun setTarget(position: Position) {
        target = position.value
    }
    
    fun update() {
        // Update PID controller with latest coefficients
        controller.setPID(p, i, d)
        
        // Get current position
        val currentPosition = pivotLeft.currentPosition
        
        // Calculate PID output
        val pid = controller.calculate(currentPosition.toDouble(), target)
        
        // Calculate feed-forward term
        val ff = cos(Math.toRadians(target / ticksInDegree)) * f
        
        // Combine for final power
        val power = pid + ff
        
        // Apply power to both motors
        pivotLeft.power = power
        pivotRight.power = power
    }
    
    fun getCurrentPosition(): Int {
        return pivotLeft.currentPosition
    }

    fun isBusy(): Boolean {
        val currentPosition = pivotLeft.currentPosition.toDouble()
        // Consider the pivot "at target" when within 20 ticks
        return Math.abs(currentPosition - target) > 20
    }
}
