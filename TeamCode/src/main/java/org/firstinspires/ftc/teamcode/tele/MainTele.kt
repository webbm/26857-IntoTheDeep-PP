package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.controller.PIDController
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.robot.Pivot
import org.firstinspires.ftc.teamcode.robot.VerticalSlide
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.cos
import kotlin.math.max

@TeleOp(name = "Main Tele")
class MainTele : LinearOpMode() {

    override fun runOpMode() {

        var controller: PIDController
        var slideController: PIDController

        var p = -0.008
        var i = 0.0
        var d = 0.0
        var f = 0.0
        //Min = -0
        //Max = -1000
        var target = 0.0
        var pivotRight: DcMotorEx
        var pivotLeft: DcMotorEx

        var sP = -0.014
        var sI = 0.0
        var sD = 0.0
        var sF = 0.0 // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
        //Min = 50
        //42" = 700
        //Max = 875
        var slideTarget = 0.0
        val ticksindegree = 12.19
        var slideRight: DcMotorEx? = null
        var slideLeft: DcMotorEx? = null


        // Initialize your robot hardware and other components here
        val dc = GamepadEx(gamepad1)
        val mc = GamepadEx(gamepad2)
        val verticalSlide = VerticalSlide(hardwareMap)
        val pivot = Pivot(hardwareMap)
        var manualOverride = false

        controller = PIDController(p, i, d)
        pivotRight = hardwareMap.get(DcMotorEx::class.java, "right_pivot")
        pivotLeft = hardwareMap.get(DcMotorEx::class.java, "left_pivot").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }

        slideController = PIDController(sP, sI, sD)
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        slideRight = hardwareMap.get(DcMotorEx::class.java, "right_slide")
        slideLeft = hardwareMap.get(DcMotorEx::class.java, "left_slide").apply {
            direction = DcMotorSimple.Direction.REVERSE
        }

        val leftSlide = hardwareMap.dcMotor.get("left_slide").apply {
            direction = DcMotorSimple.Direction.REVERSE
            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        val rightSlide = hardwareMap.dcMotor.get("right_slide").apply {
            direction = DcMotorSimple.Direction.FORWARD
            mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            mode = DcMotor.RunMode.RUN_USING_ENCODER
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }


        val frontLeft = Motor(hardwareMap, "left_front", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val frontRight = Motor(hardwareMap, "right_front", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val backLeft = Motor(hardwareMap, "left_back", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val backRight = Motor(hardwareMap, "right_back", Motor.GoBILDA.RPM_1150).apply {
            setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        }
        val driveTrain = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        while (opModeIsActive()) {

            dc.readButtons()
            mc.readButtons()

            leftSlide.power = mc.leftY
            rightSlide.power = mc.leftY

//            verticalSlide.setPower(-gamepad2.left_stick_y.toDouble(), pivot.getRawPosition(), manualOverride = true)

            val driveSpeedMultiplier = max(0.5, (1.0 - dc.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)))

            // drivetrain
            driveTrain.driveRobotCentric(
                -dc.leftX * driveSpeedMultiplier,
                -dc.leftY * driveSpeedMultiplier,
                -dc.rightX * driveSpeedMultiplier
            )

            if (gamepad2.dpad_up) {
                target = -1000.0
                slideTarget = 700.0

                //slides
                slideController.setPID(sP, sI, sD)
                val slide2Pos = slideLeft.currentPosition
                val slidePID = slideController.calculate(slide2Pos.toDouble(), slideTarget)
                val slideff = cos(Math.toRadians(slideTarget / ticksindegree)) * sF
                val slidePower = slidePID + slideff
                slideLeft.power = slidePower
                slideRight.setPower(slidePower)

                //Pivot
                controller.setPID(p, i, d)
                val pivot2Pos = pivotLeft.currentPosition
                val pid2 = controller.calculate(pivot2Pos.toDouble(), target)
                val ff = cos(Math.toRadians(target / ticksindegree)) * f
                val power = pid2 + ff
                pivotLeft.power = power
                pivotRight.power = power
            }

            if (gamepad2.dpad_down) {
                slideTarget = 25.0
                target = -50.0

                //slides
                slideController.setPID(sP, sI, sD)
                val slide2Pos = slideLeft.currentPosition
                val slidePID = slideController.calculate(slide2Pos.toDouble(), slideTarget)
                val slideff = cos(Math.toRadians(slideTarget / ticksindegree)) * sF
                val slidePower = slidePID + slideff
                slideLeft.power = slidePower
                slideRight.setPower(slidePower)

                //Pivot
                controller.setPID(p, i, d)
                val pivot2Pos = pivotLeft.currentPosition
                val pid2 = controller.calculate(pivot2Pos.toDouble(), target)
                val ff = cos(Math.toRadians(target / ticksindegree)) * f
                val power = pid2 + ff
                pivotLeft.power = power
                pivotRight.power = power
            }

            //slides
            slideController.setPID(sP, sI, sD)
            val slide2Pos = slideLeft.currentPosition
            val slidePID = slideController.calculate(slide2Pos.toDouble(), slideTarget)
            val slideff = cos(Math.toRadians(slideTarget / ticksindegree)) * sF
            val slidePower = slidePID + slideff
            slideLeft.power = slidePower
            slideRight.setPower(slidePower)

            //Pivot
            controller.setPID(p, i, d)
            val pivot2Pos = pivotLeft.currentPosition
            val pid2 = controller.calculate(pivot2Pos.toDouble(), target)
            val ff = cos(Math.toRadians(target / ticksindegree)) * f
            val power = pid2 + ff
            pivotLeft.power = power
            pivotRight.power = power

            telemetry.addData("pivot2 position", pivot2Pos)
            telemetry.addData("target", target)
            telemetry.addData("power", pivotLeft.power)
            telemetry.addData("slide position", slide2Pos)
            telemetry.addData("target", slideTarget)
            telemetry.addData("power", slideLeft.power)
            telemetry.addData("right slide power", rightSlide.power)
            telemetry.addData("left slide power", leftSlide.power)
            telemetry.update()
        }
    }
}
