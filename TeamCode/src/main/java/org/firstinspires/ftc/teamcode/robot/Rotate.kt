package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Controls the rotate servo that rotates the claw
 */
@Config
class Rotate(hardwareMap: HardwareMap) {
    private val servo: Servo = hardwareMap.get(Servo::class.java, "rotate")

    enum class Position(val value: Double) {
        LEFT_SAFE(0.3),
        SQUARE(0.5),
        RIGHT(0.8),
        HORIZON(1.0),
        TRAVEL(0.5);
    }

    init {
        setPosition(Position.TRAVEL.value)
    }

    companion object {
        // These match the values from CLAUDE.md
        @JvmField var LEFT_SAFE = 0.2
        @JvmField var SQUARE = 0.5
        @JvmField var RIGHT = 1.0
        @JvmField var TRAVEL = 0.5
    }
    
    /**
     * Sets the rotate to a preset position
     */
    fun setPosition(position: Position) {
        servo.position = position.value
    }
    
    /**
     * Sets the rotate to a specific position value with safety limits
     * @param position Value between LEFT_SAFE and RIGHT
     */
    fun setPosition(position: Double) {
        val safePosition = when {
            position < LEFT_SAFE -> LEFT_SAFE
            position > RIGHT -> RIGHT
            else -> position
        }
        servo.position = safePosition
    }
    
    /**
     * Gets the current position of the rotate
     */
    fun getPosition(): Double {
        return servo.position
    }
}
