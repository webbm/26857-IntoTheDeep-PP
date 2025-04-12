package org.firstinspires.ftc.teamcode.limelight

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@Disabled
@Config
@TeleOp(name = "LimeLightColor")
class LimeLightColor : LinearOpMode() {

    // LimeLight NetworkTable constants
    private lateinit var limelight3A: Limelight3A
    private lateinit var servo: Servo

    override fun runOpMode() {

        limelight3A = hardwareMap.get(Limelight3A::class.java, "limelight")
        servo = hardwareMap.get(Servo::class.java, "rotate")

        servo.position = 0.0

        telemetry.addData("Status", "Initializing")
        telemetry.update()

        limelight3A.start()

        limelight3A.pipelineSwitch(9)

        // Wait for the camera to initialize
        sleep(1000)

        telemetry.addData("Status", "Initialized")
        telemetry.update()

        var result: LLResult = limelight3A.latestResult

        waitForStart()

        while (opModeIsActive()) {

            limelight3A.setPollRateHz(1/2)

            result = limelight3A.latestResult

            sleep(1000)

            val corners = result
                .colorResults
                .getOrNull(0)
                ?.targetCorners
                .orEmpty()
                .map { Point(x = it[0], y = it[1]) }

            if (corners.size == 4) {

                var longestSide = 0.0
                var longestAngle = 0.0

                for (i in corners.indices) {
                    val p1 = corners[i]
                    val p2 = corners[(i + 1) % 4]

                    val dx = p2.x - p1.x
                    val dy = p2.y - p1.y

                    val sideLength = Math.sqrt(dx * dx + dy * dy)
                    val angle = Math.toDegrees(Math.atan2(-dx, dy)) // Negative dx for 0 degrees at up

                    if (sideLength > longestSide) {
                        longestSide = sideLength
                        longestAngle = angle
                    }
                }
                when (longestAngle) {
                    in -45.0..45.0 -> {
                        servo.position = 1.0
                        telemetry.addData("Servo Position", "1.0")
                    }
                    in -170.0..-46.0, in 170.1..180.0 -> {
                        servo.position = 0.4 // Adjust as needed
                        telemetry.addData("Servo Position", "0.4")
                    }
                    in 46.0..170.0 -> {
                        servo.position = 0.6 // Adjust as needed
                        telemetry.addData("Servo Position", "0.6")
                    }
                    else -> {
                        servo.position = 0.0
                        telemetry.addData("Servo Position", "0.0")
                    }
                }
                telemetry.addData("Status", "Sample Found")
                telemetry.addData("Longest Side Length", longestSide)
                telemetry.addData("Longest Side Angle (Degrees)", longestAngle)
            }

            telemetry.update()
        }

        limelight3A.stop()

    }

}

data class Point(val x: Double, val y: Double)
