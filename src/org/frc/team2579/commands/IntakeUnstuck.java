package org.frc.team2579.commands;

import org.frc.team2579.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeUnstuck extends Command {
	
	private boolean unstuck;
	private boolean flip;
	
	public IntakeUnstuck(boolean u) {
		requires(Robot.intake);
		if(unstuck==u)
			flip = !flip;
		this.unstuck = u;
	}
	protected void initialize() {
		Robot.intake.setStuck(flip&&unstuck);
		Robot.intake.setRotate(!unstuck);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
