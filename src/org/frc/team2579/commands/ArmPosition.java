package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

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
		Intake.setIntakePiston(IntakePistonState.OUT);
	}
	
	@Override
	protected void execute() {
		Robot.arm.setArmAngle(mode, angle);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.arm.isOnTarget();
	}
	
	@Override
	protected void end() {
		//Robot.arm.setArmAngle(ArmControlMode.HOLD, angle);
	}

}
