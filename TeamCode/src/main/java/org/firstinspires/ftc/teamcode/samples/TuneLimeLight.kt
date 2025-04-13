package org.firstinspires.ftc.teamcode.samples

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Elbow
import org.firstinspires.ftc.teamcode.robot.Wrist

@TeleOp
class TuneLimeLight : LinearOpMode() {

    private lateinit var claw: Servo

    override fun runOpMode() {

        val elbow = Elbow(hardwareMap)
        val wrist = Wrist(hardwareMap)

        claw = hardwareMap.get(Servo::class.java, "light")

//        val claw = Claw(hardwareMap)

//        val gamepad = GamepadEx(gamepad1)

        waitForStart()

        while (opModeIsActive()) {

            elbow.setPosition(Elbow.Position.HUNTING)

            wrist.setPosition(Wrist.Position.PRONATED)

            claw.position = 1.0

        }
    }
}
