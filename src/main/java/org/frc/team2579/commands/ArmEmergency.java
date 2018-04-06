package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class ArmEmergency extends Command {

	public ArmEmergency() {
		requires(Robot.arm);
	}
	
	@Override
	protected void execute() {
		Robot.arm.moveWithJoystickEmergency();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void end() {
		Robot.arm.setArmAngle(ArmControlMode.MANUAL, 0);
	}

}
