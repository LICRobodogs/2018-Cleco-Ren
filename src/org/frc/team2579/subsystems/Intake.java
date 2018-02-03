package org.frc.team2579.subsystems;

import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	public static enum IntakePistonState {
		UP, DOWN, IN, OUT
	};

	public static final double INTAKE_LOAD_SPEED = 0.65;
	public static final double INTAKE_EJECT_SPEED = -0.35;

	private static DoubleSolenoid intakePiston;
	private static Solenoid innerWheelPiston;
	private Spark leftIntake, rightIntake;

	public Intake() {
		try {
			leftIntake = new Spark(RobotMap.LEFT_INTAKE_PWM);
			rightIntake = new Spark(RobotMap.RIGHT_INTAKE_PWM);
			innerWheelPiston = new Solenoid(1,RobotMap.INTAKE_WHEEL_PCM2_ID);
			intakePiston = new DoubleSolenoid(RobotMap.INTAKE_DOWN_PCM_ID,RobotMap.INTAKE_UP_PCM_ID);
		} catch (Exception e) {
			System.err.println("An error occurred in the Intake constructor");
		}
	}

	public void setSpeed(double speed) {
		leftIntake.set(-speed);
		rightIntake.set(-speed);
	}

	@Override
	protected void initDefaultCommand() {

	}

	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.TEST) {
		}
	}

	public static boolean isIntakeUp() {
		return intakePiston.get() == Value.kReverse;
	}

	public void setIntakePiston(IntakePistonState state) {
		if(state == IntakePistonState.DOWN) {
			intakePiston.set(Value.kForward);
		} else if(state == IntakePistonState.UP) {
			intakePiston.set(Value.kReverse);
		} else if(state == IntakePistonState.IN){
			innerWheelPiston.set(true);
		} else if(state == IntakePistonState.OUT){
			innerWheelPiston.set(false);
		}
	}
	
	public void shootPowercube(){
		if(isIntakeUp())
			setSpeed(-.7);
		else 
			setSpeed(0);
	}
}