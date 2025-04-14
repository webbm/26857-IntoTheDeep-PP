package org.firstinspires.ftc.teamcode.samplepiplines

//import org.firstinspires.ftc.teamcode.samplepiplines.BlueDetectionPipeline
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Elbow
import org.firstinspires.ftc.teamcode.robot.Rotate
import org.firstinspires.ftc.teamcode.robot.Wrist
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvWebcam

@TeleOp
@Disabled
class YellowDetectionTest : LinearOpMode() {
    private lateinit var camera: OpenCvWebcam
    private lateinit var pipeline: YellowDetectionPipeline

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



        pipeline = YellowDetectionPipeline()
        camera.setPipeline(pipeline)

        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT, OpenCvWebcam.StreamFormat.MJPEG)
            }
            override fun onError(errorCode: Int) {
                telemetry.addData("Camera Error", errorCode)
            }
        })
        val elbow = Elbow(hardwareMap)
        val wrist = Wrist(hardwareMap)
        val claw = Claw(hardwareMap)
        val rotate = Rotate(hardwareMap)

        var shouldGrab = true

        elbow.setPosition(Elbow.Position.HUNTING)

        waitForStart()

        while (opModeIsActive()) {

            elbow.setPosition(Elbow.Position.GOIN_UP)

//                rotate.setPosition(pipeline.getServoPosition())
//
            if (pipeline.hasValidDetection()) {
                telemetry.addData("Angle", "%.0f", pipeline.angle)
            } else {
                telemetry.addData("Status", "No Yellow object detected")
            }
            telemetry.update()
        }
    }
}