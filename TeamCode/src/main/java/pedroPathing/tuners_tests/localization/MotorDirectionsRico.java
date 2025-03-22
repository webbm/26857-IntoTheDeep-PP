package pedroPathing.tuners_tests.localization;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Motor directions test for the Rico robot constants.
 *
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Motor Directions - Rico", group = "Teleop Test")
public class MotorDirectionsRico extends MotorDirections {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}