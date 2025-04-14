package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Rico implementation of the ForwardZeroPowerAccelerationTuner
 */
@Config
@Autonomous(name = "Forward Zero Power Acceleration Tuner - Rico", group = "Automatic Tuners")
@Disabled
public class ForwardZeroPowerAccelerationTunerRico extends ForwardZeroPowerAccelerationTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}