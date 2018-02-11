package org.frc.team2579.commands;

import org.frc.team2579.Robot;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeSpeed extends Command {

	public double speed;
	
	public IntakeSpeed(double speed){
		requires(Robot.intake);
		this.speed = speed;
	}
	
	@Override
	protected void initialize() {
		Robot.arm.setArmPiston(ArmPistonState.RELEASE);
		Intake.setIntakePiston(IntakePistonState.IN);
	}
	protected void execute() {
		Robot.intake.setSpeed(speed);
		SmartDashboard.putNumber("Intake Speed: ",speed);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
