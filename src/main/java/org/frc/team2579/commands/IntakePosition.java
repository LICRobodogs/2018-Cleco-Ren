package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.Command;

public class IntakePosition extends Command {
	private IntakePistonState state;

	public IntakePosition(IntakePistonState state) {
		requires(Robot.intake);
		this.state = state;
	}

	public void initialize() {
		Robot.intake.setIntakePiston(state);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
