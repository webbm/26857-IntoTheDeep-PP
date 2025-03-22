package org.firstinspires.ftc.teamcode.kotlin

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.BezierCurve
import com.pedropathing.pathgen.BezierLine
import com.pedropathing.pathgen.PathBuilder
import com.pedropathing.pathgen.PathChain
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.pedropathing.util.Timer
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import pedroPathing.constants.ValFConstants
import pedroPathing.constants.ValLConstants

@Autonomous(name = "Pedro Path Drew Auto")
class PedroPathDrewAuto : LinearOpMode() {
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
}

class GeneratedPath {
    val builder: PathBuilder = PathBuilder()

    init {
        builder
            .addPath( // Line 1
                BezierLine(
                    Point(6.000, 108.000, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(-45.0))
            .addPath( // Line 2
                BezierLine(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(40.000, 121.250, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(0.0))
            .addPath( // Line 3
                BezierLine(
                    Point(40.000, 121.250, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(-45.0))
            .setReversed(true)
            .addPath( // Line 4
                BezierLine(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(40.000, 131.250, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(0.0))
            .addPath( // Line 5
                BezierLine(
                    Point(40.000, 131.250, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0.0), Math.toRadians(-45.0))
            .addPath( // Line 6
                BezierLine(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(35.000, 137.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(15.0))
            .addPath( // Line 7
                BezierLine(
                    Point(35.000, 137.000, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(15.0), Math.toRadians(-45.0))
            .addPath( // Line 8
                BezierCurve(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(65.000, 120.000, Point.CARTESIAN),
                    Point(60.000, 95.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(-90.0))
            .addPath( // Line 9
                BezierCurve(
                    Point(60.000, 95.000, Point.CARTESIAN),
                    Point(65.000, 120.000, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-90.0), Math.toRadians(-45.0))
            .addPath( // Line 10
                BezierCurve(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(65.000, 120.000, Point.CARTESIAN),
                    Point(60.000, 95.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(-90.0))
            .addPath( // Line 11
                BezierCurve(
                    Point(60.000, 95.000, Point.CARTESIAN),
                    Point(65.000, 120.000, Point.CARTESIAN),
                    Point(15.000, 129.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-90.0), Math.toRadians(-45.0))
            .addPath( // Line 12
                BezierCurve(
                    Point(15.000, 129.000, Point.CARTESIAN),
                    Point(65.000, 120.000, Point.CARTESIAN),
                    Point(60.000, 96.000, Point.CARTESIAN)
                )
            )
            .setLinearHeadingInterpolation(Math.toRadians(-45.0), Math.toRadians(90.0))
    }
}
