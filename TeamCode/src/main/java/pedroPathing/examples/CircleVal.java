package pedroPathing.examples;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Circle autonomous using the Val robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Circle - Val", group = "Examples")
@Disabled
public class CircleVal extends Circle {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}