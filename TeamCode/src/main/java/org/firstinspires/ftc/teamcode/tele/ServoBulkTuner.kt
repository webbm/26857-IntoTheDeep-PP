package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@Config
@TeleOp
@Disabled
class ServoBulkTuner : LinearOpMode() {

    @Config
    object ServoWrist {
        //pickup: 0.1
        //max: 1.0
        @JvmField var target: Double = 0.0
    }

    @Config
    object ServoRotate {
        //square: 0.5
        //min: 0.2
        //max: 1.0
        @JvmField var target: Double = 0.5
    }

    @Config
    object ServoClaw {
        //open: 0.0
        //grab sample: 0.9
        @JvmField var target: Double = 0.0
    }

    @Config
    object ServoElbow {
        //do not go higher than: 0.8
        //pickup sample from floor: 0.25 or so
        //do not go lower than 0.25 when the pivot is down
        @JvmField var target: Double = 0.25
    }

    override fun runOpMode() {
        val wrist = hardwareMap.servo.get("wrist")
        val rotate = hardwareMap.servo.get("rotate")
        val claw = hardwareMap.servo.get("claw")
        val elbow = hardwareMap.servo.get("elbow").apply {
            direction = Servo.Direction.REVERSE
        }

        waitForStart()

        while (opModeIsActive()) {

            wrist.position = ServoWrist.target
            rotate.position = ServoRotate.target
            claw.position = ServoClaw.target
            elbow.position = ServoElbow.target

            telemetry.addData("Wrist", wrist.position)
            telemetry.addData("Rotate", rotate.position)
            telemetry.addData("Claw", claw.position)
            telemetry.addData("Elbow", elbow.position)

            idle()
        }
    }
}
