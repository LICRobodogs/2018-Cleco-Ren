package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ArmHome extends CommandGroup {
	public ArmHome() {
		if(Robot.arm.getAngleSetpoint()>Robot.arm.SWITCH_ANGLE_SETPOINT) {
			addSequential(new ArmPosition(ArmControlMode.SENSORED, 160));
			addSequential(new WaitCommand(0.35));
		}
		addSequential(new ArmGracefulDown());
		addSequential(new ArmPistonPosition(ArmPistonState.RELOAD));
		addSequential(new IntakeInnerWheelPosition(IntakePistonState.IN));
	}
}
