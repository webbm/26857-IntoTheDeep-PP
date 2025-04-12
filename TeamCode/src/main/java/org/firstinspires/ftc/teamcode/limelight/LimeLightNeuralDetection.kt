package org.firstinspires.ftc.teamcode.limelight

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.atan2
import kotlin.math.tan

@Disabled
@Config
@TeleOp(name = "LimeLight Sample Tracking", group = "TeleOp")
class LimeLightSampleTracking : OpMode() {

    private lateinit var limelight3A: Limelight3A

    override fun init() {
        try {
            limelight3A = hardwareMap.get(Limelight3A::class.java, "limelight")
            telemetry.addData("Status", "Initialized")
        } catch (e: Exception) {
            telemetry.addData("Error", "Failed to initialize Limelight")
        }
        telemetry.update()
    }

    override fun loop() {
        val result: LLResult = limelight3A.getLatestResult()

        if (result.detectorResults.isNotEmpty()) {
            val detection = result.detectorResults[0]

            // Get target offset angles
            val targetOffsetX = result.tx
            val targetOffsetY = result.ty

            // Limelight mounting and target information
            val limelightMountAngleDegrees = -40.0
            val limelightLensHeightInches = 7.0
            val sampleHeightInches = 1.5

            // Calculate distance from Limelight to the sample
            val angleToSampleDegrees = limelightMountAngleDegrees + targetOffsetY
            val angleToSampleRadians = Math.toRadians(angleToSampleDegrees)
            val distanceToSample = (sampleHeightInches - limelightLensHeightInches) / tan(angleToSampleRadians)

            // Calculate the required heading to face the sample
            val headingToTurn = -targetOffsetX // Assuming tx is the horizontal offset angle

            telemetry.addData("Sample Distance (in)", distanceToSample)
            telemetry.addData("Turn to Heading (°)", headingToTurn)
            telemetry.update()
        } else {
            telemetry.addData("Status", "No sample detected")
            telemetry.update()
        }
    }
}