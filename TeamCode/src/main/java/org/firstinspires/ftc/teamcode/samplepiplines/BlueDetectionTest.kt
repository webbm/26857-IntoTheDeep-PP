package org.firstinspires.ftc.teamcode.samplepiplines

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvWebcam

@Disabled
@TeleOp
class BlueDetectionTest : LinearOpMode() {
    private lateinit var camera: OpenCvWebcam
    private lateinit var pipeline: BlueDetectionPipeline

    override fun runOpMode() {
        // Initialize camera
        val cameraMonitorViewId = hardwareMap.appContext
            .resources.getIdentifier(
                "cameraMonitorViewId",
                "id",
                hardwareMap.appContext.packageName
            )
        camera = OpenCvCameraFactory.getInstance()
            .createWebcam(
                hardwareMap.get(WebcamName::class.java, "Webcam 1"),
                cameraMonitorViewId
            )

        pipeline = BlueDetectionPipeline()
        camera.setPipeline(pipeline)

        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }
            override fun onError(errorCode: Int) {
                telemetry.addData("Camera Error", errorCode)
            }
        })

        waitForStart()

        while (opModeIsActive()) {
            if (pipeline.hasValidDetection()) {
                telemetry.addData("Angle", "%.0f", pipeline.angle)
            } else {
                telemetry.addData("Status", "No blue object detected")
            }
            telemetry.update()
        }
    }
}