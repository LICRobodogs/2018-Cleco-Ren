package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPistonPosition extends Command{
private ArmPistonState state;
	
	public ArmPistonPosition(ArmPistonState state) {
		requires(Robot.arm);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.arm.setArmPiston(state);
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
			
	}

}
