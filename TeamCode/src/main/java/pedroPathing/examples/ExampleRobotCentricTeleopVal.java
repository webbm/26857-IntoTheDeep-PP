package pedroPathing.examples;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Robot-centric teleop using the Val robot constants.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 3/22/2025
 */
@TeleOp(name = "Example Robot-Centric Teleop - Val", group = "Examples")
@Disabled
public class ExampleRobotCentricTeleopVal extends ExampleRobotCentricTeleop {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}