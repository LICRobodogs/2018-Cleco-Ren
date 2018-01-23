package org.frc.team2579.subsystems;

import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	public static enum IntakePistonState {
		UP, DOWN
	};

	public static final double INTAKE_LOAD_SPEED = 0.7;
	public static final double INTAKE_EJECT_SPEED = -0.8;

	private static DoubleSolenoid leftIntakePiston, rightIntakePiston;
	private Spark leftIntake, rightIntake;

	public Intake() {
		try {
			leftIntake = new Spark(RobotMap.LEFT_INTAKE_PWM);
			rightIntake = new Spark(RobotMap.LEFT_INTAKE_PWM);

			//leftIntakePiston = new DoubleSolenoid(RobotMap.LEFT_INTAKE_DOWN_PCM_ID,RobotMap.LEFT_INTAKE_UP_PCM_ID);
			//rightIntakePiston = new DoubleSolenoid(RobotMap.RIGHT_INTAKE_DOWN_PCM_ID,RobotMap.RIGHT_INTAKE_UP_PCM_ID);
		} catch (Exception e) {
			System.err.println("An error occurred in the Intake constructor");
		}
	}

	public void setSpeed(double speed) {
		leftIntake.set(-speed);
		rightIntake.set(speed);
	}

	@Override
	protected void initDefaultCommand() {

	}

	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.TEST) {
		}
	}

	public static boolean getLeftIntake() {
		return leftIntakePiston.get() == Value.kReverse;
	}
	
	public static boolean getRightIntake() {
		return leftIntakePiston.get() == Value.kReverse;
	}

	public void setLeftIntakePiston(IntakePistonState state) {
		if(state == IntakePistonState.DOWN) {
			leftIntakePiston.set(Value.kForward);
			rightIntakePiston.set(Value.kForward);
		} else if(state == IntakePistonState.UP) {
			leftIntakePiston.set(Value.kReverse);
			rightIntakePiston.set(Value.kReverse);
		}
	}
	
	public void shootPowercube(){
		if(getLeftIntake() && getRightIntake())
			setSpeed(-.7);
		else 
			setSpeed(0);
	}
}