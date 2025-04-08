package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * Handles complex movements that coordinate multiple manipulators
 * Uses a state machine approach with proper timing to ensure servos reach positions
 */
@Config
class TravelMovement(
    hardwareMap: HardwareMap,
    private val telemetry: Telemetry? = null
) {
    // Individual manipulators
    private val slidePID = SlidePID(hardwareMap)
    private val pivotPID = PivotPID(hardwareMap)
    private val elbow = Elbow(hardwareMap)
    private val wrist = Wrist(hardwareMap)
    private val claw = Claw(hardwareMap)
    private val rotate = Rotate(hardwareMap)
    
    // State machine variables
    private var currentState = MovementState.IDLE
    private var stateStartTime = 0L
    
    enum class MovementState {
        IDLE,
        TRAVEL_STEP1,
        TRAVEL_STEP2,
        TRAVEL_STEP3,
        TRAVEL_COMPLETE
    }
    
    companion object {
        // Timing constants for state machine (milliseconds)
        @JvmField var SERVO_MOVE_TIME = 250L
    }
    
    /**
     * Initiates the travel position movement sequence
     * Follows coordinated sequence with timing to ensure safe movement
     */
    fun setTravelPosition() {
        currentState = MovementState.TRAVEL_STEP1
        stateStartTime = System.currentTimeMillis()
        
        // Step 1: First start the motors moving (they're slow) and set wrist and claw
        slidePID.setTarget(SlidePID.Position.RETRACTED)
        pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
        wrist.setPosition(Wrist.Position.TRAVEL)
        claw.setPosition(Claw.Position.TRAVEL)
        
        telemetry?.addData("Movement", "Starting Travel Position sequence")
        telemetry?.update()
    }
    
    /**
     * Updates the state machine and servo positions
     * Call this method regularly (in the opMode loop)
     */
    fun update() {
        // Always update the PID controllers
        slidePID.update()
        pivotPID.update()
        
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - stateStartTime
        
        when (currentState) {
            MovementState.TRAVEL_STEP1 -> {
                // Wait for wrist and claw to reach position
                if (elapsedTime >= SERVO_MOVE_TIME) {
                    // Step 2: Set elbow position
                    elbow.setPosition(Elbow.Position.TRAVEL)
                    
                    // Transition to next state
                    currentState = MovementState.TRAVEL_STEP2
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel Step 2: Setting elbow")
                    telemetry?.update()
                }
            }
            
            MovementState.TRAVEL_STEP2 -> {
                // Wait for elbow to reach position
                if (elapsedTime >= SERVO_MOVE_TIME) {
                    // Step 3: Set rotate position
                    rotate.setPosition(Rotate.Position.TRAVEL)
                    
                    // Transition to next state
                    currentState = MovementState.TRAVEL_STEP3
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel Step 3: Setting rotate")
                    telemetry?.update()
                }
            }
            
            MovementState.TRAVEL_STEP3 -> {
                // Wait for rotate to reach position and check if motors finished
                if (elapsedTime >= SERVO_MOVE_TIME && !slidePID.isBusy() && !pivotPID.isBusy()) {
                    // Movement complete
                    currentState = MovementState.TRAVEL_COMPLETE
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            
            MovementState.TRAVEL_COMPLETE -> {
                // Stay in complete state until new movement requested
            }
            
            MovementState.IDLE -> {
                // Idle state, no movement in progress
            }
        }
    }
    
    /**
     * Resets the current state to idle
     */
    fun reset() {
        currentState = MovementState.IDLE
    }
    
    /**
     * Returns true if the travel position movement is complete
     */
    fun isTravelPositionComplete(): Boolean {
        return currentState == MovementState.TRAVEL_COMPLETE
    }
    
    /**
     * Returns if any complex movement is currently in progress
     */
    fun isMovementInProgress(): Boolean {
        return currentState != MovementState.IDLE && 
               currentState != MovementState.TRAVEL_COMPLETE
    }
    
    /**
     * Gets the current state of movement
     */
    fun getCurrentState(): MovementState {
        return currentState
    }
    
    /**
     * Adds telemetry data about the current movement state
     */
    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("Movement State", currentState)
        telemetry.addData("Movement Time", System.currentTimeMillis() - stateStartTime)
        
        // Add data for each manipulator
        telemetry.addData("Slide Position", slidePID.getCurrentPosition())
        telemetry.addData("Slide Target", slidePID.target)
        telemetry.addData("Slide Busy", slidePID.isBusy())
        
        telemetry.addData("Pivot Position", pivotPID.getCurrentPosition())
        telemetry.addData("Pivot Target", pivotPID.target)
        telemetry.addData("Pivot Busy", pivotPID.isBusy())
        
        telemetry.addData("Wrist Position", wrist.getPosition())
        telemetry.addData("Claw Position", claw.getPosition())
        telemetry.addData("Elbow Position", elbow.getPosition())
        telemetry.addData("Rotate Position", rotate.getPosition())
    }
}
