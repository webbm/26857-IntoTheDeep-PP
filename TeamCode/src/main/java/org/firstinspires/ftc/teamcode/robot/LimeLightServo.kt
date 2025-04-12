package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.limelight.Point

class LimeLightServo(hardwareMap: HardwareMap, private val telemetry: Telemetry) {
    var sampleAngle: Double = 0.0

    private val elbow = Elbow(hardwareMap)
    private val wrist = Wrist(hardwareMap)
    private val claw = Claw(hardwareMap)
    private val rotate = Rotate(hardwareMap)

    private val servo = hardwareMap.servo.get("rotate").apply {
        position = 0.0
        direction = Servo.Direction.REVERSE
    }

    private val limelight3A = hardwareMap.get(Limelight3A::class.java, "limelight").apply {
        start()
        pipelineSwitch(9)
        setPollRateHz(1)
    }

    // Calculate the sample angle based on the longest side
    fun calculateSampleAngle() {

        limelight3A.setPollRateHz(1)

        var result: LLResult = limelight3A.latestResult

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

//            if (longestAngle >= -170 && longestAngle >= 170) {
//                servo.position = 0.4
//            }else if (longestAngle > 80 && longestAngle < 170) {
//                servo.position = 0.7
//            }else if (longestAngle <= -170 && longestAngle >= -120) {
//                servo.position = 0.2
//            }else {
//                servo.position = 0.4
//            }

            // 0 degrees is 0.4
            //
            telemetry.addData("Status", "Sample Found")
            telemetry.addData("Angle (Degrees)", longestAngle)
        }

        telemetry.update()

    }

    // Stop the limelight3A
    fun stop() {
        limelight3A.stop()
    }
}

