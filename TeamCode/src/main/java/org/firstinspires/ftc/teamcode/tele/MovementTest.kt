package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.movement.PickupMovement
import org.firstinspires.ftc.teamcode.movement.ScoreMovement
import org.firstinspires.ftc.teamcode.movement.ScoreReadyMovement
import org.firstinspires.ftc.teamcode.movement.TravelMovement
import org.firstinspires.ftc.teamcode.robot.*
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.abs
import kotlin.math.max

/**
 * Test OpMode for complex movements including Travel Position
 */
@TeleOp(name = "Movement Test")
class MovementTest : LinearOpMode() {

    override fun runOpMode() {
        // Setup telemetry for FTC Dashboard
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val slidePID = SlidePID(hardwareMap)
        val pivotPID = PivotPID(hardwareMap)

        // Initialize gamepad
        val driverGamepad = GamepadEx(gamepad1)
        val manipulatorGamepad = GamepadEx(gamepad2)
        
        // Initialize drive motors
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
        
        // Initialize complex movement handler
        val travelMovement = TravelMovement(hardwareMap, telemetry)
        val pickupMovement = PickupMovement(hardwareMap, telemetry)
        val scoreReadyMovement = ScoreReadyMovement(hardwareMap, telemetry)
        val scoreMovement = ScoreMovement(hardwareMap, telemetry)

        telemetry.addData("Status", "Initialized")
        telemetry.addData("Controls", "Y button: Travel Position")
        telemetry.update()
        
        waitForStart()
        
        while (opModeIsActive()) {
            // Read gamepad inputs
            driverGamepad.readButtons()
            manipulatorGamepad.readButtons()
            
            // Check for movement commands
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.TRIANGLE.xboxButton)) {
                travelMovement.setTravelPosition()
                telemetry.addData("Command", "Travel Position initiated")
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                pickupMovement.setPickupPosition()
                telemetry.addData("Command", "Travel Position initiated")
            }
            if (manipulatorGamepad.getTrigger(PS5Keys.Trigger.LEFT_TRIGGER.xboxTrigger) >= 0.15) {
                scoreReadyMovement.setPickupPosition()
            }
            if (manipulatorGamepad.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger) >= 0.15) {
                scoreMovement.setPickupPosition()
            }
            
            // Update complex movement state machine
            travelMovement.update()
            pickupMovement.update()
            scoreMovement.update()
            scoreReadyMovement.update()

            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
                pivotPID.setTarget(position = PivotPID.Position.SCORING_POSITION)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
                pivotPID.setTarget(position = PivotPID.Position.FLOOR_POSITION)
            }

            pivotPID.update()

            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.HIGH_BASKET)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_RIGHT.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.LOW_BASKET)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.RETRACTED)
            }

            val slideStickY = manipulatorGamepad.rightY  // Invert for intuitive control (stick up = extend)

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

            // Drive control with speed reduction based on trigger
            val driveSpeedMultiplier = max(0.5, (1.0 - driverGamepad.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)))
            
            // Mecanum drive control
            driveTrain.driveRobotCentric(
                -driverGamepad.leftX * driveSpeedMultiplier,
                -driverGamepad.leftY * driveSpeedMultiplier,
                -driverGamepad.rightX * driveSpeedMultiplier
            )
            
            // Display telemetry
            telemetry.addData("Status", "Running")
            travelMovement.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}
