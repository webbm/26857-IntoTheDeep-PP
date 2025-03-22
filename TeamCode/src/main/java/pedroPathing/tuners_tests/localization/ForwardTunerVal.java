package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Forward tuner for the Val robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Forward Localizer Tuner - Val", group = ".Localization")
public class ForwardTunerVal extends ForwardTuner {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}