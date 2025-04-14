package org.firstinspires.ftc.teamcode.samples;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
@Disabled
public class PivotPidTuner extends OpMode {

    private PIDController controller;

    public static double p = -0.008, i = 0.0, d = 0.0;
    //then tune these^
    public static double f = 0.; // tune this first use this video as a reminder youtube.com/watch?v=E6H6Nqe6qJo
    // to tune this^ pick up the arm and see if it resist the force of gravity

    //Min = -0
    //Max = -1000
    public static double target = 0;

    private final double ticksindegree = 12.19;

    private DcMotorEx pivotRight;
    private DcMotorEx pivotLeft;

    @Override
    public void init() {
        controller = new PIDController(p , i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        pivotRight = hardwareMap.get(DcMotorEx.class, "right_pivot");
        pivotLeft = hardwareMap.get(DcMotorEx.class, "left_pivot");

        pivotLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        controller.setPID(p, i ,d);
        int pivot2Pos = pivotLeft.getCurrentPosition();

        double pid2 = controller.calculate(pivot2Pos, target);

        double ff = Math.cos(Math.toRadians(target / ticksindegree)) * f;

        double power = pid2 + ff;

        pivotLeft.setPower(power);
        pivotRight.setPower(power);

        telemetry.addData("pivot2 position", pivot2Pos);
        telemetry.addData("target", target);
        telemetry.addData("power", pivotLeft.getPower());
        telemetry.update();
    }
}
