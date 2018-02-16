package org.frc.team2579.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class IntakeQuickInOut extends CommandGroup {
	public IntakeQuickInOut() {
		addSequential(new IntakeSpeed(-0.7,-0.5));
		addSequential(new WaitCommand(0.225));
		addSequential(new IntakeSpeed(0.6));
		addSequential(new WaitCommand(0.25));
		addSequential(new IntakeSpeed(0.0));
	}
}
