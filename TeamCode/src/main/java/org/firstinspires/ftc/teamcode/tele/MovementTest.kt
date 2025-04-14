package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.TriggerReader
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad.RumbleEffect
import org.firstinspires.ftc.teamcode.movement.PickupReadyMovement
import org.firstinspires.ftc.teamcode.movement.ScoreMovement
import org.firstinspires.ftc.teamcode.movement.ScoreReadyMovement
import org.firstinspires.ftc.teamcode.movement.TravelMovement
import org.firstinspires.ftc.teamcode.movement.autopickup.PickupMovement45Left
import org.firstinspires.ftc.teamcode.movement.autopickup.PickupMovement45Right
import org.firstinspires.ftc.teamcode.movement.autopickup.PickupMovementHorizontal
import org.firstinspires.ftc.teamcode.movement.autopickup.PickupMovementStraight
import org.firstinspires.ftc.teamcode.robot.*
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.abs
import kotlin.math.max

/**
 * Test OpMode for complex movements including Travel Position
 */
@TeleOp(name = "Main Tele Worlds")
class MovementTest : LinearOpMode() {

    override fun runOpMode() {
        // Setup telemetry for FTC Dashboard
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val slidePID = SlidePID(hardwareMap)
        val pivotPID = PivotPID(hardwareMap)
        val elbow = Elbow(hardwareMap)
        val wrist = Wrist(hardwareMap)
        val claw = Claw(hardwareMap)
        val rotate = Rotate(hardwareMap)

        // Initialize gamepad
        val driverGamepad = GamepadEx(gamepad1)
        val manipulatorGamepad = GamepadEx(gamepad2)
        val leftTriggerReader = TriggerReader(manipulatorGamepad, PS5Keys.Trigger.LEFT_TRIGGER.xboxTrigger)
        val rightTriggerReader = TriggerReader(manipulatorGamepad, PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger)

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
        val pickupMovementStraight = PickupMovementStraight(hardwareMap, pivotPID, slidePID, telemetry)
        val pickupReadyMovement = PickupReadyMovement(hardwareMap, pivotPID, slidePID, telemetry)
        val pickupMovementHorizontal = PickupMovementHorizontal(hardwareMap, telemetry)
        val pickupMovement45Right = PickupMovement45Right(hardwareMap, telemetry)
        val pickupMovement45Left = PickupMovement45Left(hardwareMap, telemetry)
        val scoreReadyMovement = ScoreReadyMovement(hardwareMap, pivotPID, slidePID, telemetry)
        val scoreMovement = ScoreMovement(hardwareMap, pivotPID, slidePID, telemetry)

        telemetry.addData("Status", "Initialized")
        telemetry.addData("Controls", "Y button: Travel Position")
        telemetry.update()

        waitForStart()
        
        while (opModeIsActive()) {
            // Read gamepad inputs
            driverGamepad.readButtons()
            manipulatorGamepad.readButtons()
            leftTriggerReader.readValue()
            rightTriggerReader.readValue()
            
            // Check for movement commands
          /*  if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.TRIANGLE.xboxButton)) {
                travelMovement.setTravelPosition()
                telemetry.addData("Command", "Travel Position initiated")
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                pickupMovementStraight.setPickupPosition()
                telemetry.addData("Command", "Travel Position initiated")
            }*/

            if (rightTriggerReader.wasJustPressed()) {
                pickupReadyMovement.setPickupPosition()
            }
            else if (rightTriggerReader.wasJustReleased()) {
                pickupMovementStraight.setPickupPosition()
            }

            if (leftTriggerReader.wasJustPressed()) {
                scoreMovement.setPickupPosition()
            }

            pickupReadyMovement.update()
            pickupMovementStraight.update()
            scoreMovement.update()

            /*
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
                scoreReadyMovement.setPickupPosition()
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
                scoreMovement.setPickupPosition()
            }
            if (manipulatorGamepad.getTrigger(PS5Keys.Trigger.LEFT_TRIGGER.xboxTrigger) >= 0.2) {
                pickupReadyMovement.setPickupPosition()
            }
            if (manipulatorGamepad.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger) >= 0.2) {
                elbow.setPosition(Elbow.Position.PICKUP)
                wrist.setPosition(Wrist.Position.PRONATED)
                claw.setPosition(Claw.Position.OPEN)
            }


            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                slidePID.setTarget(SlidePID.Position.HIGH_BASKET)
            }else if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                slidePID.setTarget(SlidePID.Position.LOW_BASKET)
            }else if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_LEFT.xboxButton)) {
                slidePID.setTarget(SlidePID.Position.INTAKE)
                elbow.setPosition(Elbow.Position.PICKUP)
                claw.setPosition(Claw.Position.OPEN)
                wrist.setPosition(Wrist.Position.PRONATED)
            }else if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.RSB.xboxButton)){
                elbow.setPosition(Elbow.Position.PICKUP)
                claw.setPosition(Claw.Position.OPEN)
                wrist.setPosition(Wrist.Position.PRONATED)
            }else if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.LSB.xboxButton)){
                elbow.setPosition(Elbow.Position.TRAVEL)
                claw.setPosition(Claw.Position.TRAVEL)
                wrist.setPosition(Wrist.Position.TRAVEL)
            }


            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.CROSS.xboxButton)) {
                pickupMovementStraight.setPickupPosition()
//                rotate.setPosition(Rotate.Position.SQUARE)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.TRIANGLE.xboxButton)) {
                pickupMovementHorizontal.setPickupPosition()
//                rotate.setPosition(Rotate.Position.HORIZON)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.CIRCLE.xboxButton)) {
                pickupMovement45Right.setPickupPosition()
//                rotate.setPosition(Rotate.Position.RIGHT)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.SQUARE.xboxButton)) {
                pickupMovement45Left.setPickupPosition()
//                rotate.setPosition(Rotate.Position.LEFT_SAFE)
            }
         /*   if (manipulatorGamepad.getTrigger(PS5Keys.Trigger.RIGHT_TRIGGER.xboxTrigger) >= 0.15) {
                scoreMovement.setPickupPosition()
            }*/
            
            // Update complex movement state machine
            travelMovement.update()
            pickupMovementStraight.update()
            pickupMovementHorizontal.update()
            pickupMovement45Left.update()
            pickupMovement45Right.update()
            scoreMovement.update()
            scoreReadyMovement.update()
             */

            /* if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.RIGHT_BUMPER.xboxButton)) {
               pivotPID.setTarget(position = PivotPID.Position.SCORING_POSITION)
           }
           if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.LEFT_BUMPER.xboxButton)) {
               pivotPID.setTarget(position = PivotPID.Position.FLOOR_POSITION)
           }*/
            /*  if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.HIGH_BASKET)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_RIGHT.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.LOW_BASKET)
            }
            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                slidePID.setTarget(position = SlidePID.Position.RETRACTED)
            }*/

            val pivotStickY = -manipulatorGamepad.leftY  // NOT inverted - stick up (negative Y) = scoring position
            val pivotPosition = pivotPID.getCurrentPosition()


            if (pivotPosition > -900 || slidePID.getCurrentPosition() > -100) {
                if (abs(pivotStickY) > 0.1) {
                    // Map the stick position to a target value
                    // When stick is pushed up (negative Y) = SCORING_POSITION
                    // When stick is pulled down (positive Y) = FLOOR_POSITION
                    if (pivotStickY < 0) {
                        // When stick is pushed up (negative Y), go to scoring position
                        pivotPID.setTarget(PivotPID.Position.SCORING_POSITION)
                        elbow.setPosition(Elbow.Position.PARALLEL)
                    } else {
                        // When stick is pulled down (positive Y), go to floor position
                        pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
                        elbow.setPosition(Elbow.Position.TRAVEL)
                    }
                }
            }
            pivotPID.update()

            if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_UP.xboxButton)) {
                slidePID.setTarget(SlidePID.Position.HIGH_BASKET)
            } else if (manipulatorGamepad.wasJustPressed(PS5Keys.Button.DPAD_DOWN.xboxButton)) {
                slidePID.setTarget(SlidePID.Position.LOW_BASKET)
            }

            val slideStickY = -manipulatorGamepad.rightY  // Invert for intuitive control (stick up = extend)

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
            telemetry.addData("slide", slidePID.getCurrentPosition())
            telemetry.addData("Pivot", pivotPID.getCurrentPosition())
//            travelMovement.addTelemetry(telemetry)
            telemetry.update()
        }
    }
}
