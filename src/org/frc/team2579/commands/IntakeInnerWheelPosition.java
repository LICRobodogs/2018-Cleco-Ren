package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeInnerWheelPosition extends Command {
private IntakePistonState state;
	
	public IntakeInnerWheelPosition(IntakePistonState state) {
		requires(Robot.intake);
		this.state = state;
	}
	
	public void initialize(){
		Intake.setIntakePiston(state);
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
