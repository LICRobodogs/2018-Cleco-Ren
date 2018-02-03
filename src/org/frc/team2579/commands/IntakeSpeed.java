package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeSpeed extends Command {

	public double speed;
	
	public IntakeSpeed(double speed){
		requires(Robot.intake);
		this.speed = speed;
	}
	
	@Override
	protected void initialize() {
		Robot.arm.setArmPiston(ArmPistonState.RELEASE);
		Robot.intake.setSpeed(speed);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
