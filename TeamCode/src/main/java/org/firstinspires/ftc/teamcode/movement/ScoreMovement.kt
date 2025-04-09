package org.firstinspires.ftc.teamcode.movement

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.robot.*
import kotlin.math.E

/**
 * Handles complex movements that coordinate multiple manipulators
 * Uses a state machine approach with proper timing to ensure servos reach positions
 */
@Config
class ScoreMovement(
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
        STEP1,
        STEP2,
        STEP3,
        STEP4,
        COMPLETE
    }
    
    /**
     * Initiates the travel position movement sequence
     * Follows coordinated sequence with timing to ensure safe movement
     */
    fun setPickupPosition() {
        currentState = MovementState.STEP1
        stateStartTime = System.currentTimeMillis()
        
        // Step 1: First start the motors moving (they're slow) and set wrist and claw
//        slidePID.setTarget(SlidePID.Position.RETRACTED)
//        pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
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
//        slidePID.update()
//        pivotPID.update()

        if (slidePID.getCurrentPosition() <= 251){
            currentState = MovementState.IDLE
            return
        }
        
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - stateStartTime
        
        when (currentState) {
            MovementState.STEP1 -> {
                // Wait for wrist and claw to reach position
                if (elapsedTime >= 150) {

                    elbow.setPosition(Elbow.Position.TRAVEL)

                    // Step 2: Set elbow position
                    currentState = MovementState.STEP2
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel Step 2: Setting elbow")
                    telemetry?.update()
                }
            }
            
            MovementState.STEP2 -> {
                // Wait for elbow to reach position
                if (elapsedTime >= 250) {
                    // Step 3: Set rotate position

                   claw.setPosition(Claw.Position.OPEN)
                    // Transition to next state
                    currentState = MovementState.STEP3
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel Step 3: Setting rotate")
                    telemetry?.update()
                }
            }
            
            MovementState.STEP3 -> {
                // Wait for rotate to reach position and check if motors finished
                if (elapsedTime >= 100) {

                    rotate.setPosition(Rotate.TRAVEL)
                    elbow.setPosition(Elbow.Position.PARALLEL)
                    wrist.setPosition(Wrist.Position.TRAVEL)

                    // Movement complete
                    currentState = MovementState.IDLE
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            MovementState.STEP4 -> {
                // Wait for rotate to reach position and check if motors finished
                if (elapsedTime >= 250) {

                    elbow.setPosition(Elbow.Position.PARALLEL)
                    wrist.setPosition(Wrist.Position.TRAVEL)

                    // Movement complete
                    currentState = MovementState.IDLE
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            MovementState.COMPLETE -> {
                // Idle state, no movement in progress
                currentState = MovementState.IDLE
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
        return currentState == MovementState.COMPLETE
    }
    
    /**
     * Returns if any complex movement is currently in progress
     */
    fun isMovementInProgress(): Boolean {
        return currentState != MovementState.IDLE &&
               currentState != MovementState.COMPLETE
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
//        telemetry.addData("Slide Position", slidePID.getCurrentPosition())
//        telemetry.addData("Slide Target", slidePID.target)
//        telemetry.addData("Slide Busy", slidePID.isBusy())
        
//        telemetry.addData("Pivot Position", pivotPID.getCurrentPosition())
//        telemetry.addData("Pivot Target", pivotPID.target)
//        telemetry.addData("Pivot Busy", pivotPID.isBusy())
        
        telemetry.addData("Wrist Position", wrist.getPosition())
        telemetry.addData("Claw Position", claw.getPosition())
        telemetry.addData("Elbow Position", elbow.getPosition())
        telemetry.addData("Rotate Position", rotate.getPosition())
    }
}
