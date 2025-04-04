package org.firstinspires.ftc.teamcode.tele

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@TeleOp
class SlideTest: LinearOpMode() {

    private lateinit var slideRight: DcMotor
    private lateinit var slideLeft: DcMotor
    private lateinit var pivotLeft: DcMotor
    private lateinit var pivotRight: DcMotor

    override fun runOpMode() {
        slideRight = hardwareMap.dcMotor.get("right_slide").apply {
            direction = DcMotorSimple.Direction.FORWARD
        }
        slideLeft = hardwareMap.dcMotor.get("left_slide").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }
        pivotRight = hardwareMap.dcMotor.get("right_pivot").apply {
            direction = DcMotorSimple.Direction.FORWARD
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        pivotLeft = hardwareMap.dcMotor.get("left_pivot").apply {
            direction = DcMotorSimple.Direction.REVERSE
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }

        waitForStart()

        while (opModeIsActive()) {
            slideRight.power = gamepad2.left_stick_y.toDouble()
            slideLeft.power = gamepad2.left_stick_y.toDouble()

            pivotRight.power = -gamepad2.right_stick_y.toDouble()
            pivotLeft.power = -gamepad2.right_stick_y.toDouble()

//            if (gamepad2.a) {
//                pivotLeft.targetPosition = 500
//                pivotLeft.mode = DcMotor.RunMode.RUN_TO_POSITION
//                pivotLeft.power = 0.5
//
//                pivotRight.targetPosition = 500
//                pivotRight.mode = DcMotor.RunMode.RUN_TO_POSITION
//                pivotRight.power = 0.5
//
//            }

            idle()
        }
    }
}
