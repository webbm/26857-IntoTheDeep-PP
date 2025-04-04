package org.firstinspires.ftc.teamcode.samples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class SlidePID extends OpMode {

    private PIDController slideController;

    public static double sP = -0.014, sI = 0.0, sD = 0.0;
                //then tune these^
    public static double sF = 0.; // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
    // to tune this^ pick up the arm and see if it resist the force of gravity

    //Min = -70
    //42" = 550
    //Max = 875
    public static double slideTarget = 0;

    private final double ticksindegree = 12.19;

    private DcMotorEx slideRight;
    private DcMotorEx slideLeft;

    @Override
    public void init() {
        slideController = new PIDController(sP, sI, sD);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        slideRight = hardwareMap.get(DcMotorEx.class, "right_slide");
        slideLeft = hardwareMap.get(DcMotorEx.class, "left_slide");

        slideLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        slideController.setPID(sP, sI,sD);
        int slide2Pos = slideLeft.getCurrentPosition();

        double slidePID = slideController.calculate(slide2Pos, slideTarget);

        double slideff = Math.cos(Math.toRadians(slideTarget / ticksindegree)) * sF;

        double slidePower = slidePID + slideff;

        slideLeft.setPower(slidePower);
        slideRight.setPower(slidePower);

        telemetry.addData("slide position", slide2Pos);
        telemetry.addData("target", slideTarget);
        telemetry.addData("power", slideLeft.getPower());
        telemetry.update();
    }
}
