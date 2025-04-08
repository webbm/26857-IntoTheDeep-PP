package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.Servo

//    @Config
    object ServoHangRelease {
        @JvmField
        var positionDown: Double = -1.0

        @JvmField
        var idle: Double = 0.0

        @JvmField
        var positionUp: Double = 1.0
    }

//   @Config
    object ServoPull {

        @JvmField
        var letUp: Double = .4

        @JvmField
        var pullDown: Double = -1.0

        @JvmField
        var tension: Double = 0.05

        @JvmField
        var idle: Double = 0.0
    }

//    @Config
    object ServoClaw {
        @JvmField
        var superOpen: Double = 0.0

        @JvmField
        var open: Double = 0.2

        @JvmField
        var closed: Double = 1.0
    }

//    @Config
    object ServoWrist {
        @JvmField
        var intake: Double = 0.0

        @JvmField
        var mid: Double = 0.5

        @JvmField
        var outake: Double = 1.0
    }


//    @Config
    object ServoRotateWrist {
        @JvmField
        var vertical: Double = 0.0

        @JvmField
        var rotateRight: Double = 0.3

        @JvmField
        var rotateLeft: Double = 1.0

        @JvmField
        var horizontal: Double = 0.5

    }

//    @Config
    object ServoElbow {
        @JvmField
        var outake: Double = 1.0

        @JvmField
        var lineUp: Double = 1.0

        @JvmField
        var mid: Double = 0.2

        @JvmField
        var intake: Double = 0.0
    }


//@TeleOp
class ServoTuning : LinearOpMode() {

    private lateinit var hangRelease: CRServoImplEx
    private lateinit var hangPull: CRServoImplEx
    private lateinit var claw: Servo
    private lateinit var wrist: Servo
    private lateinit var rotateWrist: Servo
    private lateinit var elbow: Servo

    override fun runOpMode() {

        hangRelease = hardwareMap.get(CRServoImplEx::class.java, "hangrelease").apply {
        }
        hangPull = hardwareMap.get(CRServoImplEx::class.java, "hangrelease").apply {
        }

        claw = hardwareMap.get(Servo::class.java, "claw").apply {
        }
        wrist = hardwareMap.get(Servo::class.java, "wrist").apply {
        }
        rotateWrist = hardwareMap.get(Servo::class.java, "rotateWrist").apply {
        }
        elbow = hardwareMap.get(Servo::class.java, "elbow").apply {
        }

        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        waitForStart()

        while (opModeIsActive()) {

            while (gamepad2.right_bumper){
               hangRelease.power = ServoHangRelease.positionUp
               hangPull.power = ServoPull.tension
            }
            while (gamepad2.left_bumper){
                hangRelease.power = ServoHangRelease.positionDown
                hangPull.power = ServoPull.tension
            }
            while (gamepad2.right_trigger > 0.5){
                hangPull.power = ServoPull.pullDown
            }
            while (gamepad2.left_trigger > 0.5){
                hangPull.power = ServoPull.letUp
            }

            if (gamepad2.a) {

            }

            telemetry.update()
        }
    }
}
