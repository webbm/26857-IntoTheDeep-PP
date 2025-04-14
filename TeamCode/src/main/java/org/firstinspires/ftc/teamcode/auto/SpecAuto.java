package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.*;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * This is an abstract example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 3.0, 3/22/2025
 */

public abstract class SpecAuto extends OpMode {

    protected Follower follower;
    protected Timer pathTimer, actionTimer, opmodeTimer;

    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    protected int pathState;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).) 
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    protected final Pose startPose = new Pose(6.5, 65, Math.toRadians(180));

    protected final Pose drive = new Pose(36, 35, Math.toRadians(180));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    protected final Pose scorePose = new Pose(36, 65, Math.toRadians(180));

    /** Lowest (First) Sample from the Spike Mark */
    protected final Pose sample1Pose = new Pose(60, 26, Math.toRadians(-90));

    protected final Pose pushSample1 = new Pose(20, 30, Math.toRadians(180));

    /** Middle (Second) Sample from the Spike Mark */
//    protected final Pose sample2Pose = new Pose(63, 14, Math.toRadians(-90));
//
//    protected final Pose sample2ControlPoint1 = new Pose(34, 60);

//    protected final Pose pushSample2 = new Pose(20, 14, Math.toRadians(180));
//
//    /** Highest (Third) Sample from the Spike Mark */
//    protected final Pose sample3Pose = new Pose(60, 7, Math.toRadians(0));
//    protected final Pose sample3ControlPoint1 = new Pose(72, 30);
//
//    protected final Pose pushSample3 = new Pose(12, 7, Math.toRadians(180));
//    protected final Pose awaitCyclePose = new Pose(24, 24, Math.toRadians(180));



    /* These are our Paths and PathChains that we will define in buildPaths() */
    protected PathChain pushSampleOne, pushSampleDos, pushSampleTres, gotoSample1, gotoSample2, gotoSample3, scorePreload,
             pushSampleTwo, pushSampleThree, awaitingCycle, driveAway;
    
    /**
     * Set the constants for this OpMode
     */
    public void setConstants() { }

    /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
     * It is necessary to do this so that all the paths are built before the auto starts. **/
    public void buildPaths() {

        /* There are two major types of paths components: BezierCurves and BezierLines.
         *    * BezierCurves are curved, and require >= 3 points. There are the start and end points, and the control points.
         *    - Control points manipulate the curve between the start and end points.
         *    - A good visualizer for this is [this](https://pedro-path-generator.vercel.app/).
         *    * BezierLines are straight, and require 2 points. There are the start and end points.
         * Paths have can have heading interpolation: Constant, Linear, or Tangential
         *    * Linear heading interpolation:
         *    - Pedro will slowly change the heading of the robot from the startHeading to the endHeading over the course of the entire path.
         *    * Constant Heading Interpolation:
         *    - Pedro will maintain one heading throughout the entire path.
         *    * Tangential Heading Interpolation:
         *    - Pedro will follows the angle of the path such that the robot is always driving forward when it follows the path.
         * PathChains hold Path(s) within it and are able to hold their end point, meaning that they will holdPoint until another path is followed.
         * Here is a explanation of the difference between Paths and PathChains <https://pedropathing.com/commonissues/pathtopathchain.html> */

        /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */


        /* Here is an example for Constant Interpolation
        scorePreload.setConstantInterpolation(startPose.getHeading()); */

        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setTangentHeadingInterpolation()
                .setReversed(true)
                .build();

        driveAway = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(drive)))
                .setConstantHeadingInterpolation(drive.getHeading())
                .build();

        gotoSample1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(drive), new Point(sample1Pose)))
                .setTangentHeadingInterpolation()
                .setReversed(true)
                .build();

        pushSampleOne = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(sample1Pose), new Point(pushSample1)))
                .setConstantHeadingInterpolation(pushSample1.getHeading())
                .build();

//        gotoSample2 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(pushSample1), new Point(sample2Pose), new Point(sample2ControlPoint1)))
//                .setLinearHeadingInterpolation(pushSample1.getHeading(), sample2Pose.getHeading())
//                .build();
//
//        /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        pushSampleTwo = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(sample2Pose), new Point(pushSample2)))
//                .setLinearHeadingInterpolation(sample2Pose.getHeading(), pushSample2.getHeading())
//                .build();
//
//        /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        gotoSample3 = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(pushSample2), new Point(sample3Pose), new Point(sample3ControlPoint1)))
//                .setLinearHeadingInterpolation(pushSample2.getHeading(), sample3Pose.getHeading())
//                .build();
//
//        /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        pushSampleThree = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(sample3Pose), new Point(pushSample3)))
//                .setLinearHeadingInterpolation(sample3Pose.getHeading(), pushSample3.getHeading())
//                .build();
//
//        /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//        awaitingCycle = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pushSample3), new Point(awaitCyclePose)))
//                .setConstantHeadingInterpolation(awaitCyclePose.getHeading())
//                .build();
//
   }

    /** This switch is called continuously and runs the pathing, at certain points, it triggers the action state.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                if(!follower.isBusy()) {
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                   follower.followPath(driveAway,true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(gotoSample1,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    /* Score Sample */

                    follower.followPath(pushSampleOne,true);
                    setPathState(4);
                }
                break;
//            case 4:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(gotoSample2,true);
//                    setPathState(5);
//                }
//                break;
//            case 5:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Score Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                    follower.followPath(pushSampleTres,true);
//                    setPathState(6);
//                }
//                break;
//            case 6:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(pushSampleTres, true);
//                    setPathState(7);
//                }
//                break;
//            case 7:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
//                if(!follower.isBusy()) {
//                    /* Grab Sample */
//
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                    follower.followPath(awaitingCycle, true);
//                    setPathState(-1);
//                }
//                break;
        }
    }

    /** These change the states of the paths and actions
     * It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        setConstants();
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {
    }
}