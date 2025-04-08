package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Controls the claw servo for grasping samples
 */
@Config
class Claw(hardwareMap: HardwareMap) {
    private val servo: Servo = hardwareMap.get(Servo::class.java, "claw")

    enum class Position(val value: Double) {
        OPEN(0.0),
        CLOSED(0.9),
        TRAVEL(0.9);
    }

    init {
        setPosition(Position.TRAVEL.value)
    }

    companion object {
        // These match the values from CLAUDE.md
        @JvmField var OPEN = 0.0
        @JvmField var CLOSED = 0.9
        @JvmField var TRAVEL = 0.9
    }
    
    /**
     * Sets the claw to a preset position
     */
    fun setPosition(position: Position) {
        servo.position = position.value
    }
    
    /**
     * Sets the claw to a specific position value
     * @param position Value between 0.0 (open) and 0.9 (closed)
     */
    fun setPosition(position: Double) {
        servo.position = position
    }
    
    /**
     * Gets the current position of the claw
     */
    fun getPosition(): Double {
        return servo.position
    }
}
