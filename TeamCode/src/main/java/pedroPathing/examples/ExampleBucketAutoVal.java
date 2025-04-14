package pedroPathing.examples;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Example bucket auto for the Val robot.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Example Auto Blue - Val", group = "Examples")
@Disabled
public class ExampleBucketAutoVal extends ExampleBucketAuto {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}