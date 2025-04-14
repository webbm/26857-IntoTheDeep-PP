package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime

/**
 * Example OpMode written in Kotlin to demonstrate Kotlin support
 */
@TeleOp(name = "Kotlin Example", group = "Examples")
@Disabled
class KotlinExample : LinearOpMode() {
    // Declare OpMode members
    private val runtime = ElapsedTime()
    private var leftDrive: DcMotor? = null
    private var rightDrive: DcMotor? = null

    override fun runOpMode() {
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        // Initialize the hardware variables
        leftDrive = hardwareMap.get(DcMotor::class.java, "left_drive")
        rightDrive = hardwareMap.get(DcMotor::class.java, "right_drive")

        // Most robots need the motor on one side to be reversed to drive forward
        leftDrive?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD
        rightDrive?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE

        // Wait for the game to start (driver presses PLAY)
        waitForStart()
        runtime.reset()

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry
            var leftPower: Double
            var rightPower: Double

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used
            
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            val drive = -gamepad1.left_stick_y.toDouble()
            val turn = gamepad1.right_stick_x.toDouble()
            leftPower = drive + turn
            rightPower = drive - turn

            // Tank Mode uses one stick per wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower = -gamepad1.left_stick_y.toDouble()
            // rightPower = -gamepad1.right_stick_y.toDouble()

            // Send calculated power to wheels
            leftDrive?.power = leftPower
            rightDrive?.power = rightPower

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: $runtime")
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
            telemetry.update()
        }
    }
}