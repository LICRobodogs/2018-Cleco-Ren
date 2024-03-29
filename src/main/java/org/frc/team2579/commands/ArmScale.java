package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ArmScale extends CommandGroup {
	public ArmScale() {
		addSequential(new ArmPistonPosition(ArmPistonState.GRAB));
		addSequential(new IntakeInnerWheelPosition(IntakePistonState.OUT));
		addSequential(new IntakeSpeed(-1.0));
		addSequential(new WaitCommand(1.0));
		addSequential(new IntakeSpeed(0.0));
		addSequential(new ArmPosition(ArmControlMode.SENSORED, Robot.arm.SCALE_ANGLE_SETPOINT));
//		addSequential(new ArmPistonPosition(ArmPistonState.RELEASE));
//		//addSequential(new WaitCommand(0.35));
//		addSequential(new ArmPistonPosition(ArmPistonState.SHOOT));
//		addSequential(new ArmPosition(ArmControlMode.SENSORED, 200));
//		addSequential(new WaitCommand(0.35));
//		addSequential(new ArmPistonPosition(ArmPistonState.RELOAD));
//		addSequential(new ArmGracefulDown());
//		addSequential(new IntakeInnerWheelPosition(IntakePistonState.IN));
	}
}
