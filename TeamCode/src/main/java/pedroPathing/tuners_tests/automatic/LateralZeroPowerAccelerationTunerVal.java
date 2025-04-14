package pedroPathing.tuners_tests.automatic;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import pedroPathing.constants.ValFConstants;
import pedroPathing.constants.ValLConstants;

/**
 * Val implementation of the LateralZeroPowerAccelerationTuner
 */
@Config
@Autonomous(name = "Lateral Zero Power Acceleration Tuner - Val", group = "Automatic Tuners")
@Disabled
public class LateralZeroPowerAccelerationTunerVal extends LateralZeroPowerAccelerationTuner {
    @Override
    protected void setConstants() {
        Constants.setConstants(ValFConstants.class, ValLConstants.class);
    }
}