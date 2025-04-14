package pedroPathing.examples;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Triangle autonomous using the Rico robot constants.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Triangle - Rico", group = "Examples")
@Disabled
public class TriangleRico extends Triangle {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}