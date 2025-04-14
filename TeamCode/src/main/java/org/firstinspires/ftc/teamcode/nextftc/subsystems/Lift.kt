package org.firstinspires.ftc.teamcode.nextftc.subsystems

import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition

object Lift: Subsystem() {
    lateinit var slideRight: MotorEx
    lateinit var slideLeft: MotorEx
    lateinit var motors: MotorGroup

    val controller = PIDFController(-0.014, 0.0, 0.0, StaticFeedforward(0.0))

    val slideRightName = "slide_right"
    val slideLeftName = "slide_left"

    val toLow: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            0.0, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    val toMiddle: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            -600.0, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    val toHigh: Command
        get() = RunToPosition(
            motors, // MOTOR TO MOVE
            -1360.0, // TARGET POSITION, IN TICKS
            controller, // CONTROLLER TO IMPLEMENT
            this) // IMPLEMENTED SUBSYSTEM

    override fun initialize() {
        slideRight = MotorEx(slideRightName)
        slideLeft = MotorEx(slideLeftName)
        motors = MotorGroup(slideRight, slideLeft)
    }
}
