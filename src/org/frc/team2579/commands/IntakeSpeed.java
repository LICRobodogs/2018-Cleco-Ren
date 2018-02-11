package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSpeed extends Command {

	public double mSpeed;
	public boolean mUnstuck, mRotate;
	
	public IntakeSpeed(){
		requires(Robot.intake);
	}
	
	public IntakeSpeed(double speed){
		requires(Robot.intake);
		this.mSpeed = speed;
		mUnstuck = false;
		mRotate = false;
	}
	
	public IntakeSpeed(boolean kill) {
		mUnstuck = !kill;
		mRotate = !kill;
	}
	
	@Override
	protected void initialize() {
		if(!mUnstuck&&!mRotate) {
			Robot.intake.setStuck(mUnstuck);
			Robot.intake.setRotate(mRotate);
		}
		Robot.arm.setArmPiston(ArmPistonState.RELEASE);
		Intake.setIntakePiston(IntakePistonState.IN);
	}
	protected void execute() {
			Robot.intake.setSpeed();
		//SmartDashboard.putNumber("Intake Speed: ",speed);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
