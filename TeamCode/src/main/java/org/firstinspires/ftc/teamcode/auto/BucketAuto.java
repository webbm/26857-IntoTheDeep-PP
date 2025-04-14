package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.*;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.movement.autopickup.*;
import org.firstinspires.ftc.teamcode.movement.autoscore.AutoScoreReadyMovement;
import org.firstinspires.ftc.teamcode.robot.*;

/**
 * This is an abstract example auto that showcases movement and control of two servos autonomously.
 * It is a 0+4 (Specimen + Sample) bucket auto. It scores a neutral preload and then pickups 3 samples from the ground and scores them before parking.
 * There are examples of different ways to build paths.
 * A path progression method has been created and can advance based on time, position, or other factors.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 3.0, 3/22/2025
 */
public abstract class BucketAuto extends OpMode {

    //new Pose(19, 122.5, Math.toRadians(0));
    public static double PICKUP_ONE_POSE_X = 22.0;
    public static double PICKUP_ONE_POSE_Y = 124;
    public static double PICKUP_ONE_POSE_H = 0.0;

    private ElapsedTime     runtime = new ElapsedTime();

    protected Follower follower;
    protected Timer pathTimer, actionTimer, opmodeTimer;

    /** This is the variable where we store the state of our auto.
     * It is used by the pathUpdate method. */
    protected int pathState;

    private AutoScoreReadyMovement autoScoreReadyMovement;
    private AutoPickupReadyMovement autoPickupReadyMovement;
    private AutoPickupReadyMovementSample3 autoPickupReadyMovementSample3;

    private SlidePID slidePID;

    private PivotPID pivotPID;

    /* Create and Define Poses + Paths
     * Poses are built with three constructors: x, y, and heading (in Radians).
     * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
     * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
     * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
     * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
     * Lets assume our robot is 18 by 18 inches
     * Lets assume the Robot is facing the human player and we want to score in the bucket */

    /** Start Pose of our robot */
    protected final Pose startPose = new Pose(6.5, 111.0, Math.toRadians(270));

    /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
    protected final Pose scorePose = new Pose(14, 129, Math.toRadians(315));

    /** Lowest (First) Sample from the Spike Mark */
    protected final Pose pickup1Pose = new Pose(PICKUP_ONE_POSE_X, PICKUP_ONE_POSE_Y, Math.toRadians(PICKUP_ONE_POSE_H));

    /** Middle (Second) Sample from the Spike Mark */
    protected final Pose pickup2Pose = new Pose(24, 133, Math.toRadians(0));

    /** Highest (Third) Sample from the Spike Mark */
    protected final Pose pickup3Pose = new Pose(24, 130, Math.toRadians(45));

    protected final Pose cyclePose = new Pose(60, 97, Math.toRadians(-90));

    /** Park Control Pose for our robot, this is used to manipulate the bezier curve that we will create for the parking.
     * The Robot will not go to this pose, it is used a control point for our bezier curve. */
    protected final Pose cycleControlPose = new Pose(65, 120, Math.toRadians(-90));

    protected final Pose scoreCycle = new Pose(14, 129, Math.toRadians(315));

    protected final Pose cycleScoreControlPose = new Pose(65, 120, Math.toRadians(-90));

    protected final Pose cyclePose2 = new Pose(60, 97, Math.toRadians(-90));

    /** Park Control Pose for our robot, this is used to manipulate the bezier curve that we will create for the parking.
     * The Robot will not go to this pose, it is used a control point for our bezier curve. */
    protected final Pose cycleControlPose2 = new Pose(65, 120, Math.toRadians(-90));

    protected final Pose scoreCycle2 = new Pose(14, 129, Math.toRadians(315));

    protected final Pose cycleScoreControlPose2 = new Pose(65, 120, Math.toRadians(-90));

    /** Park Pose for our robot, after we do all of the scoring. */
    protected final Pose parkPose = new Pose(60, 98, Math.toRadians(90));

    /** Park Control Pose for our robot, this is used to manipulate the bezier curve that we will create for the parking.
     * The Robot will not go to this pose, it is used a control point for our bezier curve. */
    protected final Pose parkControlPose = new Pose(60, 100, Math.toRadians(90));

    /* These are our Paths and PathChains that we will define in buildPaths() */
    protected Path scorePreload, park, cycle, cycleScore1, cycle2, cycleScore2, grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;


    /**
     * Set the constants for this OpMode
     */
    protected abstract void setConstants();

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
        scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        grabPickup1 = new Path(new BezierLine(new Point(scorePose), new Point(pickup1Pose)));
        grabPickup1.setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading());

        scorePickup1 = new Path(new BezierLine(new Point(pickup1Pose), new Point(scorePose)));
        scorePickup1.setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading());

        grabPickup2 = new Path(new BezierLine(new Point(scorePose), new Point(pickup2Pose)));
        grabPickup2.setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading());

        scorePickup2 = new Path(new BezierLine(new Point(pickup2Pose), new Point(scorePose)));
        scorePickup2.setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading());

        grabPickup3 = new Path(new BezierLine(new Point(scorePose), new Point(pickup3Pose)));
        grabPickup3.setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading());

        scorePickup3 = new Path(new BezierLine(new Point(pickup3Pose), new Point(scorePose)));
        scorePickup3.setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading());

        cycle = new Path(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(cycleControlPose), new Point(cyclePose)));
        cycle.setLinearHeadingInterpolation(scorePose.getHeading(), cyclePose.getHeading());

        cycleScore1 = new Path(new BezierCurve(new Point(cyclePose), /* Control Point */ new Point(cycleScoreControlPose), new Point(scoreCycle)));
        cycleScore1.setLinearHeadingInterpolation(cyclePose.getHeading(), scorePose.getHeading());

        cycle2 = new Path(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(cycleControlPose2), new Point(cyclePose2)));
        cycle2.setLinearHeadingInterpolation(scorePose.getHeading(), cyclePose2.getHeading());

        cycleScore2 = new Path(new BezierCurve(new Point(cyclePose2), /* Control Point */ new Point(cycleScoreControlPose2), new Point(scoreCycle2)));
        cycleScore2.setLinearHeadingInterpolation(cyclePose2.getHeading(), scorePose.getHeading());

        park = new Path(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(cycleControlPose2), new Point(cyclePose2)));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), cyclePose2.getHeading());
    }

  /*  TravelMovement travelMovement = new TravelMovement(hardwareMap, telemetry);
    PickupMovementStraight pickupMovementStraight = new PickupMovementStraight(hardwareMap, telemetry);
    PickupReadyMovement pickupReadyMovement = new PickupReadyMovement(hardwareMap, pivotPID, slidePID, telemetry);
    AutoPickupReadyMovementSample3 pickupReadyMovementSample3 = new AutoPickupReadyMovementSample3(hardwareMap, pivotPID, slidePID, telemetry);
    PickupMovementHorizontal pickupMovementHorizontal = new PickupMovementHorizontal(hardwareMap, telemetry);
    PickupMovement45Right pickupMovement45Right = new PickupMovement45Right(hardwareMap, telemetry);
    PickupMovement45Left pickupMovement45Left = new PickupMovement45Left(hardwareMap, telemetry);
    AutoScoreReadyMovement autoScoreReadyMovement = new AutoScoreReadyMovement(hardwareMap, pivotPID, slidePID, telemetry);
    AutoScoreMovement autoScoreMovement = new AutoScoreMovement(hardwareMap, pivotPID, slidePID, telemetry);*/

    /** This switch is called continuously and runs the pathing, at certain points, it triggers the action state.
     * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
     * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
    public void autonomousPathUpdate() throws InterruptedException {
        switch (pathState) {
            case 0:
                follower.followPath(scorePreload);
                autoScoreReadyMovement.setPickupPosition();
                if (autoScoreReadyMovement.isMovementInProgress()) {
                    setPathState(1);
                }

                break;
            case 1:

                if(!follower.isBusy() && !autoScoreReadyMovement.isMovementInProgress()) {
                    follower.followPath(grabPickup1, false);
                    autoPickupReadyMovement.setPickupPosition();
                    if (!autoPickupReadyMovement.isMovementInProgress()) {
                        setPathState(-1);
                    }
                }
                break;
            case 2:
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scorePickup1);
//                    autoScoreReadyMovement.setPickupPosition();
                    setPathState(-1);
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(grabPickup2);
                    actionTimer.resetTimer();
                    autoPickupReadyMovement.setPickupPosition();
                    if (actionTimer.getElapsedTimeSeconds() >= 2) {
                        setPathState(4);
                    }
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scorePickup2,true);
                    autoScoreReadyMovement.setPickupPosition();
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(grabPickup3);
                    actionTimer.resetTimer();
                    autoPickupReadyMovementSample3.setPickupPosition();
                    if (actionTimer.getElapsedTimeSeconds() >= 5){
                        setPathState(6);
                    }
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(scorePickup3, true);
                    autoScoreReadyMovement.setPickupPosition();
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(cycle);
                    actionTimer.resetTimer();
                    autoPickupReadyMovement.setPickupPosition();
                    if (actionTimer.getElapsedTimeSeconds() >= 5){
                        setPathState(8);
                    }
                }
                break;
            case 8:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(cycleScore1,true);
                    autoScoreReadyMovement.setPickupPosition();
                    if (actionTimer.getElapsedTimeSeconds() >= 5){
                        setPathState(9);
                    }
                }
                break;
            case 9:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if(!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower.followPath(cycle2, true);
                    autoPickupReadyMovement.setPickupPosition();
                    if (actionTimer.getElapsedTimeSeconds() >= 5){
                        setPathState(10);
                    }
                }
               break;
            case 10:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(cycleScore2,true);
                    autoScoreReadyMovement.setPickupPosition();
                    setPathState(11);
                }
                break;
            case 11:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are parked */
                    follower.followPath(park,true);
                    setPathState(12);
                }
                break;
            case 12:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Level 1 Ascent */

                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
            default: break;
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
        try {
            autonomousPathUpdate();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        autoScoreReadyMovement.update();
        autoPickupReadyMovement.update();
        slidePID.update();
        pivotPID.update();


        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("SlidePID", slidePID.getCurrentPosition());
        telemetry.addData("PivotPID", pivotPID.getCurrentPosition());
        telemetry.addData("autoScoreReadyMovement", autoScoreReadyMovement.getCurrentState());
        telemetry.addData("autoPickupReadyMovement", autoPickupReadyMovement.getCurrentState());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {

        Elbow elbow = new Elbow(hardwareMap);
        Wrist wrist = new Wrist(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        Rotate rotate = new Rotate(hardwareMap);

        slidePID = new SlidePID(hardwareMap);
        pivotPID = new PivotPID(hardwareMap);
        autoScoreReadyMovement = new AutoScoreReadyMovement(hardwareMap, pivotPID, slidePID, telemetry);
        autoPickupReadyMovement = new AutoPickupReadyMovement(hardwareMap, pivotPID, slidePID, telemetry);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        actionTimer = new Timer();
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