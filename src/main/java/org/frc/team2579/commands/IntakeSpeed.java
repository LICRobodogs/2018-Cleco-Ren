package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSpeed extends Command {

	public double mSpeed, mSpeed2;
	public boolean mUnstuck, mRotate;

	public IntakeSpeed() {
		requires(Robot.intake);
	}

	public IntakeSpeed(double speed) {
		requires(Robot.intake);
		this.mSpeed = speed;
		this.mSpeed2 = 0;
		mUnstuck = false;
		mRotate = false;
	}

	public IntakeSpeed(double speed1, double speed2) {
		this.mSpeed = speed1;
		this.mSpeed2 = speed2;
	}

	public IntakeSpeed(boolean kill) {
		mUnstuck = !kill;
		mRotate = !kill;
	}

	@Override
	protected void initialize() {
		if (!mUnstuck && !mRotate) {
			Robot.intake.setStuck(mUnstuck);
			Robot.intake.setRotate(mRotate);
		}
		// Robot.arm.setArmPiston(ArmPistonState.RELEASE);
		// Intake.setIntakePiston(IntakePistonState.IN);
	}

	protected void execute() {
		if (mSpeed != 0 && mSpeed2 != 0) {
			Robot.intake.setLeftSpeed(mSpeed);
			Robot.intake.setRightSpeed(mSpeed2);
		} else if (mSpeed != 0) {
			Robot.intake.setSpeed(mSpeed);
		} else {
			Robot.intake.setSpeed();
		}
		// SmartDashboard.putNumber("Intake Speed: ",mSpeed);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	protected void end() {
		// mSpeed = 0;
	}

}
