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
@Autonomous(name = "Spec Auto - Rico")
public class SpecAutoRico extends SpecAuto {
    
    @Override
    public void setConstants() {
        Constants.setConstants(RicoFConstants.class, RicoLConstants.class);
    }
}