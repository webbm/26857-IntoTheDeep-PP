package pedroPathing.tuners_tests.pid;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Straight back and forth test for the Val robot constants.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Straight Back And Forth - Val", group = "PIDF Tuning")
public class StraightBackAndForthVal extends StraightBackAndForth {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}