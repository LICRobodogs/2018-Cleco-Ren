package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmHome extends CommandGroup {
	public ArmHome() {
		addSequential(new ArmGracefulDown());
		addSequential(new ArmPistonPosition(ArmPistonState.RELOAD));
		addSequential(new IntakeInnerWheelPosition(IntakePistonState.IN));
	}
}
