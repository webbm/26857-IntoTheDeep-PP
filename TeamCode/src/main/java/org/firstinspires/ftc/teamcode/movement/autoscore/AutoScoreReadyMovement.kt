package org.firstinspires.ftc.teamcode.movement.autoscore

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.movement.autoscore.AutoScoreMovement.MovementState
import org.firstinspires.ftc.teamcode.robot.*

/**
 * Handles complex movements that coordinate multiple manipulators
 * Uses a state machine approach with proper timing to ensure servos reach positions
 */
@Config
class AutoScoreReadyMovement(
    hardwareMap: HardwareMap,
    private val pivotPID: PivotPID,
    private val slidePID: SlidePID,
    private val telemetry: Telemetry? = null
) {
    // Individual manipulators
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
        STEP5,
        STEP6,
        STEP7,
        STEP8,
        COMPLETE
    }

    /**
     * Initiates the travel position movement sequence
     * Follows coordinated sequence with timing to ensure safe movement
     */
    fun setPickupPosition() {
        currentState = MovementState.STEP1
        stateStartTime = System.currentTimeMillis()

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

        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - stateStartTime

        when (currentState) {
            MovementState.STEP1 -> {
                // Wait for wrist and claw to reach position
                if (elapsedTime >= 300) {

                    pivotPID.setTarget(PivotPID.Position.SCORING_POSITION)
                    slidePID.setTarget(SlidePID.Position.RETRACTED)
                    wrist.setPosition(Wrist.Position.MID)
                    elbow.setPosition(Elbow.Position.PARALLEL)

                    // Transition to next state
                    currentState = MovementState.STEP2
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel Step 2: Setting elbow")
                    telemetry?.update()
                }
            }
            MovementState.STEP2 -> {
                // Wait for elbow to reach position
                currentState = MovementState.STEP3
                stateStartTime = currentTime
            }
            MovementState.STEP3 -> {
                // Wait for rotate to reach position and check if motors finished
                if (pivotPID.getCurrentPosition() <= PivotPID.Position.SCORING_POSITION.value.toInt()) {

                    slidePID.setTarget(SlidePID.Position.HIGH_BASKET)

                    // Movement complete
                    currentState = MovementState.STEP4
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            MovementState.STEP4 -> {

                if (slidePID.getCurrentPosition() <= -1250) {

                    elbow.setPosition(Elbow.Position.SCORE)
                    rotate.setPosition(Rotate.Position.SQUARE)
                    // Wait for rotate to reach position and check if motors finished
                    currentState = MovementState.STEP5
                    stateStartTime = currentTime
                }
            }
            MovementState.STEP5 -> {

                if (elapsedTime >= 350) {

                    claw.setPosition(Claw.Position.OPEN)

                    // Wait for rotate to reach position and check if motors finished
                    currentState = MovementState.STEP6
                    stateStartTime = currentTime
                }
            }
            MovementState.STEP6 -> {

                if (elapsedTime >= 300) {
                    // Step 3: Set rotate position

                    rotate.setPosition(Rotate.TRAVEL)
                    elbow.setPosition(Elbow.Position.PARALLEL)
                    wrist.setPosition(Wrist.Position.TRAVEL)

                    // Wait for rotate to reach position and check if motors finished
                    currentState = MovementState.STEP7
                    stateStartTime = currentTime
                    // Transition to next state
                }
            }
            MovementState.STEP7 -> {
                // Wait for rotate to reach position and check if motors finished
                if (elapsedTime >= 250) {

                    slidePID.setTarget(SlidePID.Position.RETRACTED)

                    // Movement complete
                    currentState = MovementState.STEP8
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            MovementState.STEP8 -> {
                // Wait for rotate to reach position and check if motors finished
                if (slidePID.getCurrentPosition() >= -300) {

                    elbow.setPosition(Rotate.TRAVEL)

                    // Movement complete
                    currentState = MovementState.COMPLETE
                    stateStartTime = currentTime
                    telemetry?.addData("Movement", "Travel position complete")
                    telemetry?.update()
                }
            }
            MovementState.COMPLETE -> {

                if (elapsedTime >= 250) {

//                    elbow.setPosition(Elbow.Position.TRAVEL)
                    pivotPID.setTarget(PivotPID.Position.FLOOR_POSITION)
                    // Movement complete
                    // Wait for rotate to reach position and check if motors finished
                    currentState = MovementState.IDLE
                    stateStartTime = currentTime
                }

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
        return currentState == MovementState.COMPLETE || currentState == MovementState.IDLE
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
