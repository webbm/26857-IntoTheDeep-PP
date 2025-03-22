package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Rico implementation of the StrafeVelocityTuner
 */
@Config
@Autonomous(name = "Strafe Velocity Tuner - Rico", group = "Automatic Tuners")
public class StrafeVelocityTunerRico extends StrafeVelocityTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}