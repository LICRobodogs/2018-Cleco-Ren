package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmScore extends CommandGroup {
	public ArmScore() {
		addParallel(new ArmPistonPosition(ArmPistonState.RELEASE));
		addParallel(new ArmPistonPosition(ArmPistonState.SHOOT));
	}
}
