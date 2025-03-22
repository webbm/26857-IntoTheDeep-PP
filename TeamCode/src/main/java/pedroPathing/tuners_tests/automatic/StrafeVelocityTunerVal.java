package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Val implementation of the StrafeVelocityTuner
 */
@Config
@Autonomous(name = "Strafe Velocity Tuner - Val", group = "Automatic Tuners")
public class StrafeVelocityTunerVal extends StrafeVelocityTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}