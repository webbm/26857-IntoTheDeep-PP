package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Forward tuner for the Rico robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Forward Localizer Tuner - Rico", group = ".Localization")
@Disabled
public class ForwardTunerRico extends ForwardTuner {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}