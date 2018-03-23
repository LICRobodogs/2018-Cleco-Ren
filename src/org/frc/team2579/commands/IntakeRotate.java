package org.frc.team2579.commands;

import org.frc.team2579.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeRotate extends Command {

	private boolean rotate;
	private boolean flip;
	
	public IntakeRotate(boolean r) {
		requires(Robot.intake);
		if(rotate==r)
			flip = !flip;
		this.rotate = r;
	}
	protected void initialize() {
		Robot.intake.setRotate(flip&&rotate);
		Robot.intake.setStuck(!rotate);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}