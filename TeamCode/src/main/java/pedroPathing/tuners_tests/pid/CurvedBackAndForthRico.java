package pedroPathing.tuners_tests.pid;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Curved back and forth test for the Rico robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Curved Back And Forth - Rico", group = "PIDF Testing")
public class CurvedBackAndForthRico extends CurvedBackAndForth {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}