package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmGearboxState;
import edu.wpi.first.wpilibj.command.Command;

public class ArmGearboxPistonPosition extends Command {
private ArmGearboxState state;
	
	public ArmGearboxPistonPosition(ArmGearboxState state) {
		requires(Robot.arm);
		this.state = state;
	}

	@Override
	protected void initialize() {
		Robot.arm.setArmGearbox(state);
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
