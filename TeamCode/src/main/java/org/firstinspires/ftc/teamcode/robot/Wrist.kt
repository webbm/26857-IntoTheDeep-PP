package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Controls the wrist servo that operates in a pronation/supination motion
 */
@Config
class Wrist(hardwareMap: HardwareMap) {
    private val servo: Servo = hardwareMap.get(Servo::class.java, "wrist")

    enum class Position(val value: Double) {
        PRONATED(0.0),
        MID(0.5),
        SUPINATED(1.0),
        TRAVEL(0.75);
    }

    init {
        setPosition(Position.TRAVEL.value)
    }

    companion object {
        // These match the values from CLAUDE.md
        @JvmField var PRONATED = 0.0
        @JvmField var SUPINATED = 1.0
        @JvmField var TRAVEL = 0.75
    }
    
    /**
     * Sets the wrist to a preset position
     */
    fun setPosition(position: Position) {
        servo.position = position.value
    }
    
    /**
     * Sets the wrist to a specific position value
     * @param position Value between 0.0 (pronated) and 1.0 (supinated)
     */
    fun setPosition(position: Double) {
        servo.position = position
    }
    
    /**
     * Gets the current position of the wrist
     */
    fun getPosition(): Double {
        return servo.position
    }
}
