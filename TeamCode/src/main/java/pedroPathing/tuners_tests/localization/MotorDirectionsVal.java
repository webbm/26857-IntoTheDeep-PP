package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Motor directions test for the Val robot constants.
 *
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Motor Directions - Val", group = "Teleop Test")
@Disabled
public class MotorDirectionsVal extends MotorDirections {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}