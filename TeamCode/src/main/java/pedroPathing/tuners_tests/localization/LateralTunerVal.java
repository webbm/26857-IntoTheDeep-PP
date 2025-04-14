package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Lateral tuner for the Val robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Lateral Localizer Tuner - Val", group = ".Localization")
@Disabled
public class LateralTunerVal extends LateralTuner {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}