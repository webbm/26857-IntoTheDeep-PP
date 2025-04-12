package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.movement.ScoreMovement
import org.firstinspires.ftc.teamcode.movement.ScoreReadyMovement
import org.firstinspires.ftc.teamcode.movement.TravelMovement
import org.firstinspires.ftc.teamcode.movement.autopickup.PickupMovementStraight
import org.firstinspires.ftc.teamcode.robot.*
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.abs
import kotlin.math.max

@TeleOp(name = "Main Tele New")
class MainTeleNew : LinearOpMode() {

    override fun runOpMode() {
        // Initialize telemetry with dashboard
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        
        // Initialize gamepads with FTCLib
        val driver = GamepadEx(gamepad1)
        val manipulator = GamepadEx(gamepad2)
        
        // Initialize subsystems with PID control
        val pivotPID = PivotPID(hardwareMap)
        val slidePID = SlidePID(hardwareMap)
        
        // Setup drivetrain with FTCLib
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

        val elbow = Elbow(hardwareMap)
        val wrist = Wrist(hardwareMap)
        val claw = Claw(hardwareMap)
        val rotate = Rotate(hardwareMap)

        // Wait for the start button to be pressed
        waitForStart()

        // Main control loop
        while (opModeIsActive()) {
            // Read gamepad button states
            driver.readButtons()
            manipulator.readButtons()

            // ---- DRIVER CONTROLS (GAMEPAD 1) ----
            
            // Set drive speed based on right trigger (for precision control)
            val driveSpeedMultiplier = max(0.5, (1.0 - driver.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)))
            
            // Drive robot with left stick (forward/strafe) and right stick (turn)
            driveTrain.driveRobotCentric(
                -driver.leftX * driveSpeedMultiplier,
                -driver.leftY * driveSpeedMultiplier,
                -driver.rightX * driveSpeedMultiplier
            )

            // ---- MANIPULATOR CONTROLS (GAMEPAD 2) ----
            
            // PIVOT CONTROL - Left stick Y
            val pivotStickY = manipulator.leftY  // NOT inverted - stick up (negative Y) = scoring position
            
            // Apply deadzone to prevent drift
            if (abs(pivotStickY) > 0.1) {
                // Map the stick position to a target value
                // When stick is pushed up (negative Y) = SCORING_POSITION
                // When stick is pulled down (positive Y) = FLOOR_POSITION
                if (pivotStickY < 0) {
                    // When stick is pushed up (negative Y), go to scoring position
                    pivotPID.setTarget(PivotPID.Position.SCORING_POSITION)
                } else {
                    // When stick is pulled down (positive Y), go to floor position
                    pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
                }
            }
            
            // Preset positions with D-pad buttons for pivot
            if (manipulator.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                pivotPID.setTarget(PivotPID.Position.SCORING_POSITION)
            } else if (manipulator.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
            }
            
            // SLIDE CONTROL - Right stick Y
            val slideStickY = -manipulator.rightY  // Invert for intuitive control (stick up = extend)
            
            // Check if we're using manual control or preset positions
            if (abs(slideStickY) > 0.1) {
                // Manual control mode when right stick is moved
                slidePID.setManualPower(slideStickY)
            } else if (!slidePID.isManualControl) {
                // Continue with PID control if we were already in that mode
                slidePID.update()
            } else {
                // Stop slides when stick is released (returning from manual mode)
                slidePID.setManualPower(0.0)
                // Optional: could switch back to PID mode and hold position with:
                // slidePID.setTarget(slidePID.getCurrentPosition().toDouble())
            }
            
            // Preset positions for slides with bumpers
            if (manipulator.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                slidePID.setTarget(SlidePID.Position.HIGH_BASKET)
            } else if (manipulator.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
                slidePID.setTarget(SlidePID.Position.LOW_BASKET)
            } else if (manipulator.wasJustPressed(GamepadKeys.Button.Y)) {
                slidePID.setTarget(SlidePID.Position.RETRACTED)
            }
            
            // Update PID control for pivot (must be called every loop)
            pivotPID.update()

            // Display telemetry for debugging and monitoring
            telemetry.addData("Pivot Position", pivotPID.getCurrentPosition())
            telemetry.addData("Pivot Target", pivotPID.target)
            telemetry.addData("Slide Position", slidePID.getCurrentPosition())
            telemetry.addData("Slide Target", slidePID.target)
            telemetry.addData("Slide Manual", slidePID.isManualControl)
            telemetry.addData("Drive Speed", driveSpeedMultiplier)
            telemetry.update()
        }
    }
}