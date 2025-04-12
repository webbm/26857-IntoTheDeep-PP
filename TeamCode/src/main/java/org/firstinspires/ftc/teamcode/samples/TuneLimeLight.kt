package org.firstinspires.ftc.teamcode.samples

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.robot.Claw
import org.firstinspires.ftc.teamcode.robot.Elbow
import org.firstinspires.ftc.teamcode.robot.LimeLightServo
import org.firstinspires.ftc.teamcode.robot.Wrist
import org.firstinspires.ftc.teamcode.util.PS5Keys

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

            elbow.setPosition(Elbow.Position.PICKUP)

            wrist.setPosition(Wrist.Position.PRONATED)

            claw.position = 1.0

        }
    }
}
