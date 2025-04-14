package org.firstinspires.ftc.teamcode.nextftc

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.BezierLine
import com.pedropathing.pathgen.Path
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.pedro.FollowPath
import com.rowanmcalpin.nextftc.pedro.PedroOpMode
import org.firstinspires.ftc.teamcode.nextftc.subsystems.NextFtcPivot
import pedroPathing.constants.RicoFConstants
import pedroPathing.constants.RicoLConstants

@Autonomous(name = "NextFTC Autonomous Program Kotlin")
class NextFtcAuto: PedroOpMode(NextFtcPivot) {
    private val startPose = Pose(6.5, 111.0, Math.toRadians(270.0))
    private val finishPose = Pose(14.0, 129.0, Math.toRadians(315.0))

    private lateinit var move: Path

    fun buildPaths() {
        move = Path(BezierLine(Point(startPose), Point(finishPose)))
        move.setLinearHeadingInterpolation(startPose.heading, finishPose.heading)
    }

    val secondRoutine: Command
        get() = SequentialGroup(
            ParallelGroup(
                FollowPath(move),
                NextFtcPivot.toMiddle,
            ),
            Delay(10.0),
        )

    override fun onInit() {
        Constants.setConstants(RicoFConstants::class.java, RicoLConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(startPose)
        buildPaths()
    }

    override fun onStartButtonPressed() {
        secondRoutine()
    }
}
