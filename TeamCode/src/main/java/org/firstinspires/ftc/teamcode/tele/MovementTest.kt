package org.firstinspires.ftc.teamcode.tele

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.robot.TravelMovement
import org.firstinspires.ftc.teamcode.util.PS5Keys
import kotlin.math.max

/**
 * Test OpMode for complex movements including Travel Position
 */
@TeleOp(name = "Movement Test")
class MovementTest : LinearOpMode() {

    override fun runOpMode() {
        // Setup telemetry for FTC Dashboard
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        
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
            
            // Update complex movement state machine
            travelMovement.update()
            
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
