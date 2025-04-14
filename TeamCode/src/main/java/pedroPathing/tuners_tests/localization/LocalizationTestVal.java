package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Localization test for the Val robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Localization Test - Val", group = "Teleop Test")
@Disabled
public class LocalizationTestVal extends LocalizationTest {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}