package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.command.Command;

public class ArmPosition extends Command {

	private ArmControlMode mode;
	private double angle;
	public ArmPosition(ArmControlMode mode, double angle) {
		requires(Robot.arm);
		this.mode = mode;
		this.angle = angle;
	}

	@Override
	protected void initialize() {
		Robot.arm.setArmPiston(ArmPistonState.GRAB);
	}
	
	@Override
	protected void execute() {
		Robot.arm.setArmAngle(mode, angle);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.arm.isOnTarget();
	}
	
	protected void end() {
		Robot.arm.setArmAngle(ArmControlMode.HOLD, angle);
	}

}
