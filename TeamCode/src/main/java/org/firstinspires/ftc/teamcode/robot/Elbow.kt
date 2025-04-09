package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Controls the elbow servo for flexion/extension motion
 */
@Config
class Elbow(hardwareMap: HardwareMap) {
    private val servo: Servo = hardwareMap.servo.get("elbow").apply {
        direction = Servo.Direction.REVERSE
    }

    enum class Position(val value: Double) {
        PARALLEL(0.3),
        EXTENDED(0.8),
        TRAVEL(0.75);
    }

    init {
        setPosition(Position.TRAVEL.value)
    }

    companion object {
        // These match the values from CLAUDE.md
        @JvmField var PARALLEL = 0.275
        @JvmField var EXTENDED = 0.8
        @JvmField var MIN_POSITION = 0.25
        @JvmField var TRAVEL = 0.75
    }
    
    /**
     * Sets the elbow to a preset position
     */
    fun setPosition(position: Position) {
        servo.position = position.value
    }
    
    /**
     * Sets the elbow to a specific position value with safety limits
     * @param position Value between MIN_POSITION and EXTENDED
     */
    fun setPosition(position: Double) {
        val safePosition = when {
            position < MIN_POSITION -> MIN_POSITION
            position > EXTENDED -> EXTENDED
            else -> position
        }
        servo.position = safePosition
    }
    
    /**
     * Gets the current position of the elbow
     */
    fun getPosition(): Double {
        return servo.position
    }
}
