package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class ArmGracefulDown extends Command {

	public ArmGracefulDown() {
		requires(Robot.arm);

	}

	@Override
	protected void initialize() {
		// Robot.arm.setArmPiston(ArmPistonState.SHOOT);
		// Robot.arm.setArmPiston(ArmPistonState.GRAB);
	}

	@Override
	protected void execute() {
		Robot.arm.setArmAngle(ArmControlMode.MANUAL, 0);
		// Robot.arm.setArmAngle(ArmControlMode.SENSORED, Arm.SWITCH_ANGLE_SETPOINT);
	}

	@Override
	protected boolean isFinished() {
		return Robot.arm.isOnTarget();
	}

	@Override
	protected void end() {
		// Robot.arm.setArmAngle(ArmControlMode.MANUAL, 0);
	}

}
