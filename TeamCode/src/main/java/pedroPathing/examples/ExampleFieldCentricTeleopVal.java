package pedroPathing.examples;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Field-centric teleop using the Val robot constants.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Example Field-Centric Teleop - Val", group = "Examples")
@Disabled
public class ExampleFieldCentricTeleopVal extends ExampleFieldCentricTeleop {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}