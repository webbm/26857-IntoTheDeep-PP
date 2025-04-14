package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition

@Config
object NextFtcPivot: Subsystem() {
    lateinit var pivotLeft: MotorEx
    lateinit var pivotRight: MotorEx
    lateinit var motors: MotorGroup

    @Config
    object NextFtcPivotRightTargets {
        @JvmField var LOW = 0.0
        @JvmField var MIDDLE = -30.0
        @JvmField var HIGH = -30.0
    }

    val controller = PIDFController(-0.008, 0.0, 0.0, StaticFeedforward(0.0))

    val toLow: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            NextFtcPivotRightTargets.LOW, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    val toMiddle: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            NextFtcPivotRightTargets.MIDDLE, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    val toHigh: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            NextFtcPivotRightTargets.HIGH, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    override fun initialize() {
        pivotLeft = MotorEx("left_pivot").apply {
            resetEncoder()
            reverse()
        }
        pivotRight = MotorEx("right_pivot").apply {
            resetEncoder()
        }

        motors = MotorGroup(pivotLeft, pivotRight)
    }
}
