package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Localization test for the Rico robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Localization Test - Rico", group = "Teleop Test")
@Disabled
public class LocalizationTestRico extends LocalizationTest {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}