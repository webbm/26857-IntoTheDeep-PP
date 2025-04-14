package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import pedroPathing.constants.RicoFConstants;
import pedroPathing.constants.RicoLConstants;

/**
 * Example bucket auto for the Rico robot.
 *
 * @author Baron Henderson - 20077 The Indubitables
 * @version 1.0, 3/22/2025
 */
@Autonomous(name = "Bucket Auto - Rico!")
public class BucketAutoRico extends BucketAuto {
    
    @Override
    protected void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}