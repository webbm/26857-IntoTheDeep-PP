package org.firstinspires.ftc.teamcode.limelight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import kotlin.math.tan

@Disabled
@Config
@TeleOp(name = "LimeLight", group = "Neural")
class LimeLight : LinearOpMode() {

    // LimeLight NetworkTable constants
    private lateinit var limelight3A: Limelight3A

    override fun runOpMode() {

        limelight3A = hardwareMap.get(Limelight3A::class.java, "limelight")

        telemetry.addData("Status", "Initializing")
        telemetry.update()

        limelight3A.start()

        limelight3A.pipelineSwitch(0)

        // Wait for the camera to initialize
        sleep(1000)

        telemetry.addData("Status", "Initialized")
        telemetry.update()

        var result: LLResult = limelight3A.getLatestResult()

        waitForStart()

        while (opModeIsActive()) {

            result = limelight3A.getLatestResult()

            val targetOffsetAngleVertical: Int = result.ty.toInt()


            // how many degrees back is your limelight rotated from perfectly vertical?
            val limelightMountAngleDegrees = -40.0

            // distance from the center of the Limelight lens to the floor
            val limelightLensHeightInches = 7.0

            // distance from the target to the floor
            val sampleHeightInches = 1.5

            val angleToSampleDegrees = limelightMountAngleDegrees + targetOffsetAngleVertical
            val angleToSampleRadians = angleToSampleDegrees * (3.14159 / 180.0)

            //calculate distance
            var distanceFromLimelightToSampleInches: Double =
                (sampleHeightInches - limelightLensHeightInches) / tan(angleToSampleRadians)




            // Check if the LimeLight has detected any objects
            telemetry.addData("num detectorResults", result.detectorResults.size)
            telemetry.addData("classId", result.detectorResults.getOrNull(0)?.classId)
            telemetry.addData("sample distance", distanceFromLimelightToSampleInches)
//            telemetry.addData("what is the angle", )
            telemetry.addData("num detector results", result.detectorResults.size)
            telemetry.addData("num corners results", result.detectorResults.getOrNull(0)?.targetCorners?.getOrNull(0)?.size)
            result.detectorResults.forEachIndexed { dIdx, detectionResult ->
                detectionResult.targetCorners.forEachIndexed { tIdx, corner ->
                    telemetry.addData("$dIdx $tIdx corner ", corner.joinToString())
                }
            }
            telemetry.update()

        }
    }

}