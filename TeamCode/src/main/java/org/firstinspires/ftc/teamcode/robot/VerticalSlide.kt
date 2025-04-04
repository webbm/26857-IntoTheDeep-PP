package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

class VerticalSlide(hardwareMap: HardwareMap) {
    private val slide = hardwareMap.dcMotor.get("right_slide").apply {
        direction = DcMotorSimple.Direction.REVERSE
        mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        mode = DcMotor.RunMode.RUN_USING_ENCODER
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }
    private val slide2 = hardwareMap.dcMotor.get("left_slide").apply {
        direction = DcMotorSimple.Direction.REVERSE
        mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        mode = DcMotor.RunMode.RUN_USING_ENCODER
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun getRawPositions(): Pair<Int, Int> {
        return slide.currentPosition to slide2.currentPosition
    }

    fun resetEncoders() {
        slide.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        slide.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun setPower(power: Double, pivotPosition: Int, manualOverride: Boolean = false) {
        if (manualOverride) {
            slide.power = power
            slide2.power = power
            return
        }

        if (pivotPosition > -2000) {
//         pivot is lower than 45 degrees, limit extension
            if (slide.currentPosition < -1538 && power < 0) {
                slide.power = 0.0
                slide2.power = 0.0
            } else if (slide.currentPosition > 0 && power > 0) {
                slide.power = 0.0
                slide2.power = 0.0
            } else {
                slide.power = power
                slide2.power = power
            }
        }
        else {
            if (slide.currentPosition < -2500 && power < 0) {
                slide.power = 0.0
                slide2.power = 0.0
            } else if (slide.currentPosition > 0 && power > 0) {
                slide.power = 0.0
                slide2.power = 0.0
            } else {
                slide.power = power
                slide2.power = power
            }
        }
    }

    fun setPosition(position: Int) {

        val currentLimit = 2.0

        slide.targetPosition = position
        slide2.targetPosition = position
        slide.mode = DcMotor.RunMode.RUN_TO_POSITION
        slide2.mode = DcMotor.RunMode.RUN_TO_POSITION
        slide.power = 1.0
        slide2.power = 1.0
    }

    fun calculateElbowPosition(slide1: Double, slide2: Double): Int {
        // Example inverse kinematics calculation
        val targetPosition = (slide1 + slide2) / 2.0

        // Normalize to a range of 0 to 1
        val normalizedPosition = targetPosition.coerceIn(0.0, 1.0)

        // Convert to servo position (1000 to 1500)
        return (1000 + normalizedPosition * 500).toInt()
    }

}
