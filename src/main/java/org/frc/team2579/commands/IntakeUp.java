package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeUp extends CommandGroup {
	public IntakeUp() {
		addSequential(new IntakePosition(IntakePistonState.UP));
	}
}
