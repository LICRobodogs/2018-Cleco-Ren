package org.frc.team2579.commands;

import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ArmScaleScore extends CommandGroup {
	public ArmScaleScore(){
		addParallel(new ArmPistonPosition(ArmPistonState.GRAB));
		addParallel(new IntakeInnerWheelPosition(IntakePistonState.OUT));
		addSequential(new IntakeSpeed(-0.7));
		addSequential(new WaitCommand(0.35));
		addSequential(new IntakeSpeed(0.0));
		addSequential(new ArmPosition(ArmControlMode.SENSORED, Arm.SCALE_ANGLE_SETPOINT));
		addSequential(new ArmPistonPosition(ArmPistonState.RELEASE));
		addSequential(new WaitCommand(0.15));
		addSequential(new ArmPistonPosition(ArmPistonState.SHOOT));
		addSequential(new ArmPosition(ArmControlMode.SENSORED, Arm.SWITCH_ANGLE_SETPOINT));
		addSequential(new ArmPistonPosition(ArmPistonState.RELOAD));
		addSequential(new ArmGracefulDown());
		}
}
