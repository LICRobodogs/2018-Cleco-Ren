package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeDown extends CommandGroup {
	public IntakeDown() {
		addSequential(new IntakePosition(IntakePistonState.DOWN));
	}

}
