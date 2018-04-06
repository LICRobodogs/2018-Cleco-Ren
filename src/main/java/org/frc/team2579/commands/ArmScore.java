package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmScore extends CommandGroup {
	public ArmScore() {
		addSequential(new ArmPistonPosition(ArmPistonState.RELEASE));
		addSequential(new ArmPistonPosition(ArmPistonState.SHOOT));
	}
}
