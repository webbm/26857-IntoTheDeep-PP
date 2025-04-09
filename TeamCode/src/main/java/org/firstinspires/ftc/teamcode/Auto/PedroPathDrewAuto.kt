package org.firstinspires.ftc.teamcode.Auto

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.*
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import pedroPathing.constants.ValFConstants
import pedroPathing.constants.ValLConstants


@Autonomous(name = "Pedro Path Drew Auto")
abstract class PedroPathDrewAuto : LinearOpMode() {
    private lateinit var follower: Follower
    private lateinit var pathTimer: Timer
    private lateinit var generatedPathChain: PathChain
    private var pathState = 0
    
    // Starting pose facing down
    private val startPose = Pose(6.0, 108.0, Math.toRadians(0.0))

    private fun buildPaths() {
        // Use the path from GeneratedPath directly
        val generatedPath = GeneratedPath()
        generatedPathChain = generatedPath.builder.build()
    }
    
    private fun setPathState(state: Int) {
        pathState = state
        pathTimer.resetTimer()
    }
    
    private fun autonomousPathUpdate() {
        when (pathState) {
            0 -> {
                // Start following the generated path
                follower.followPath(generatedPathChain, true)
                setPathState(1)
            }
            1 -> {
                // Wait until the path is complete
                if (!follower.isBusy()) {
                    setPathState(-1) // End of path
                }
                
                // Display telemetry data
                telemetry.addData("Following path", pathState)
                telemetry.addData("x", follower.pose.x)
                telemetry.addData("y", follower.pose.y)
                telemetry.addData("heading (deg)", Math.toDegrees(follower.pose.heading))
                telemetry.update()
            }
        }
    }

    override fun runOpMode() {
        // Initialize timers
        pathTimer = Timer()
        
        // Initialize Pedro pathing
        Constants.setConstants(ValFConstants::class.java, ValLConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(startPose)
        
        // Build paths
        buildPaths()
        
        telemetry.addData("Status", "Initialized")
        telemetry.update()
        
        waitForStart()
        
        // Start pathing
        setPathState(0)
        
        // Main loop
        while (opModeIsActive()) {
            follower.update()
            autonomousPathUpdate()
        }
    }

    abstract fun setConstants()
}

class GeneratedPath {
    val builder: PathBuilder = PathBuilder()

    init {
        builder
            .addPath( // Line 1
                BezierLine(
                    Point(65.0, 6.5000, Point.CARTESIAN),
                    Point(72.000, 36.000, Point.CARTESIAN)
                )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180.0))
            .addPath( // Line 2
                BezierCurve(
                    Point(72.000, 36.000, Point.CARTESIAN),
                    Point(33.000, 30.000, Point.CARTESIAN),
                    Point(36.000, 60.000, Point.CARTESIAN)
                )
            )
//            .setLinearHeadingInterpolation(Math.toRadians(180.0), Math.toRadians(-90.0))
//            .addPath( // Line 3
//                BezierLine(
//                    Point(60.000, 36.000, Point.CARTESIAN),
//                    Point(60.000, 24.000, Point.CARTESIAN)
//                )
//            )
//            .setTangentHeadingInterpolation()
//            .addPath( // Line 4
//                BezierLine(
//                    Point(60.000, 24.000, Point.CARTESIAN),
//                    Point(12.000, 24.000, Point.CARTESIAN)
//                )
//            )
//            .setConstantHeadingInterpolation(Math.toRadians(-90.0))
//            .addPath( // Line 5
//                BezierCurve(
//                    Point(12.000, 24.000, Point.CARTESIAN),
//                    Point(34.000, 60.000, Point.CARTESIAN),
//                    Point(63.000, 14.000, Point.CARTESIAN)
//                )
//            )
//            .setConstantHeadingInterpolation(Math.toRadians(-90.0))
//            .addPath( // Line 6
//                BezierLine(
//                    Point(63.000, 14.000, Point.CARTESIAN),
//                    Point(12.000, 14.000, Point.CARTESIAN)
//                )
//            )
//            .setConstantHeadingInterpolation(Math.toRadians(-90.0))
//            .addPath( // Line 7
//                BezierCurve(
//                    Point(12.000, 14.000, Point.CARTESIAN),
//                    Point(72.000, 30.000, Point.CARTESIAN),
//                    Point(60.000, 7.000, Point.CARTESIAN)
//                )
//            )
//            .setLinearHeadingInterpolation(Math.toRadians(-90.0), Math.toRadians(0.0))
//            .addPath( // Line 8
//                BezierLine(
//                    Point(60.000, 7.000, Point.CARTESIAN),
//                    Point(12.000, 7.000, Point.CARTESIAN)
//                )
//            )
//            .setTangentHeadingInterpolation()
//            .setReversed(true)
//            .addPath( // Line 9
//                BezierLine(
//                    Point(12.000, 7.000, Point.CARTESIAN),
//                    Point(24.000, 24.000, Point.CARTESIAN)
//                )
//            )
//            .setConstantHeadingInterpolation(Math.toRadians(0.0))
    }
}
