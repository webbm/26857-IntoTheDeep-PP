package org.firstinspires.ftc.teamcode.kotlin

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime

/**
 * A hardware class written in Kotlin
 */
class RobotHardwareKt {
    // Define Motors and Servos
    var leftDrive: DcMotor? = null
    var rightDrive: DcMotor? = null
    var leftArm: DcMotor? = null
    var rightArm: DcMotor? = null
    var leftClaw: Servo? = null
    var rightClaw: Servo? = null

    // Local OpMode members
    private var hwMap: HardwareMap? = null
    private val period = ElapsedTime()

    // Constructor
    constructor() {}

    /* Initialize standard Hardware interfaces */
    fun init(ahwMap: HardwareMap) {
        // Save reference to Hardware map
        hwMap = ahwMap

        // Define and Initialize Motors
        leftDrive = hwMap?.get(DcMotor::class.java, "left_drive")
        rightDrive = hwMap?.get(DcMotor::class.java, "right_drive")
        leftArm = hwMap?.get(DcMotor::class.java, "left_arm")
        rightArm = hwMap?.get(DcMotor::class.java, "right_arm")
        
        // Set motor directions
        leftDrive?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD
        rightDrive?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
        leftArm?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD
        rightArm?.direction = com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
        
        // Set all motors to zero power
        leftDrive?.power = 0.0
        rightDrive?.power = 0.0
        leftArm?.power = 0.0
        rightArm?.power = 0.0
        
        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightDrive?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        leftArm?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightArm?.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        
        // Define and initialize ALL installed servos
        leftClaw = hwMap?.get(Servo::class.java, "left_claw")
        rightClaw = hwMap?.get(Servo::class.java, "right_claw")
        leftClaw?.position = 0.5
        rightClaw?.position = 0.5
    }
    
    // Helper methods
    fun setDrivePower(left: Double, right: Double) {
        leftDrive?.power = left
        rightDrive?.power = right
    }
    
    fun stopDriveMotors() {
        leftDrive?.power = 0.0
        rightDrive?.power = 0.0
    }
}