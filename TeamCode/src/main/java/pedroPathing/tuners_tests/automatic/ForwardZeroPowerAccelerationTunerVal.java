package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Val implementation of the ForwardZeroPowerAccelerationTuner
 */
@Config
@Autonomous(name = "Forward Zero Power Acceleration Tuner - Val", group = "Automatic Tuners")
public class ForwardZeroPowerAccelerationTunerVal extends ForwardZeroPowerAccelerationTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}