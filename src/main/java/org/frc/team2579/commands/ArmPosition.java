package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class ArmPosition extends Command {

	private ArmControlMode mMode;
	private double mAngle;

	public ArmPosition(ArmControlMode mode, double angle) {
		requires(Robot.arm);
		this.mMode = mode;
		this.mAngle = angle;
	}

	@Override
	protected void initialize() {
		Robot.arm.setSetpoint(mAngle);
		// Robot.arm.setArmPiston(ArmPistonState.GRAB);
		// Intake.setIntakePiston(IntakePistonState.OUT);
	}

	@Override
	protected void execute() {
		/*
		 * if(mMode!=ArmControlMode.MANUAL) {
		 * //Robot.arm.setControlMode(ArmControlMode.SENSORED); Robot.arm.setArmAngle();
		 * }else {
		 */ // Robot.arm.setControlMode(ArmControlMode.MANUAL);
		Robot.arm.setArmAngle(mMode, mAngle);
		// }
	}

	@Override
	protected boolean isFinished() {
		return Robot.arm.isOnTarget();
	}

	@Override
	protected void end() {
		// Robot.arm.setArmPiston(ArmPistonState.SHOOT);
		// Robot.arm.setArmPiston(ArmPistonState.GRAB);
		// Robot.arm.setArmAngle(ArmControlMode.SENSORED, Arm.SWITCH_ANGLE_SETPOINT);
		// Timer.delay(1.5);
		// Robot.arm.setSetpoint(25);
		// Robot.arm.setArmAngle(mMode, 30);
	}

}
