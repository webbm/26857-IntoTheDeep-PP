package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Rico implementation of the LateralZeroPowerAccelerationTuner
 */
@Config
@Autonomous(name = "Lateral Zero Power Acceleration Tuner - Rico", group = "Automatic Tuners")
@Disabled
public class LateralZeroPowerAccelerationTunerRico extends LateralZeroPowerAccelerationTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}