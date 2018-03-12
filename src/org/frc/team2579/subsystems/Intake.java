package org.frc.team2579.subsystems;

import org.frc.team2579.OI;
import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;
import org.frc.team2579.utility.ControlLoopable;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem implements ControlLoopable {
	public static enum IntakePistonState {
		UP, DOWN, IN, OUT
	};
	
	private boolean unstuck, rotate;
	
	public static final double INTAKE_LOAD_SPEED = 0.65;
	public static final double INTAKE_EJECT_SPEED = -0.55;

	private static DoubleSolenoid intakePiston;
	private static DoubleSolenoid innerWheelPiston;
	private Spark leftIntake, rightIntake;

	public Intake() {
		try {
			leftIntake = new Spark(RobotMap.LEFT_INTAKE_PWM);
			rightIntake = new Spark(RobotMap.RIGHT_INTAKE_PWM);
			innerWheelPiston = new DoubleSolenoid(RobotMap.INTAKE_WHEEL_IN_PCM2_ID,RobotMap.INTAKE_WHEEL_OUT_PCM2_ID);
			intakePiston = new DoubleSolenoid(RobotMap.INTAKE_DOWN_PCM_ID,RobotMap.INTAKE_UP_PCM_ID);
		} catch (Exception e) {
			System.err.println("An error occurred in the Intake constructor");
		}
	}

	public void setStuck(boolean u) {
		this.unstuck = u;
	}
	
	public void setRotate(boolean r) {
		this.rotate = r;
	}
	
	public double getRightTriggerAxis() {
		return OI.getInstance().getOperatorGamepad().getRightTriggerAxis()>0.3?OI.getInstance().getOperatorGamepad().getRightTriggerAxis():0;
	}
	
	public double getLeftTriggerAxis() {
		return OI.getInstance().getOperatorGamepad().getLeftTriggerAxis()>0.4?OI.getInstance().getOperatorGamepad().getLeftTriggerAxis():0;
	}
	
	public void setSpeed() {
		if (getRightTriggerAxis()-getLeftTriggerAxis()<0/*unstuck&&!rotate*/) {
			leftIntake.set(-0.7*(getRightTriggerAxis()-getLeftTriggerAxis()));
			rightIntake.set(-0.7*(getRightTriggerAxis()-getLeftTriggerAxis()));
		}else{
			leftIntake.set(-0.7*(getRightTriggerAxis()-getLeftTriggerAxis()));
			rightIntake.set(-0.7*(getRightTriggerAxis()-getLeftTriggerAxis()));
		}
			/*else if(rotate&&!unstuck) {
			leftIntake.set(-0.6*(OI.getInstance().getOperatorGamepad().getRightTriggerAxis()-OI.getInstance().getOperatorGamepad().getLeftTriggerAxis()));
			rightIntake.set(0.6*(OI.getInstance().getOperatorGamepad().getRightTriggerAxis()-OI.getInstance().getOperatorGamepad().getLeftTriggerAxis()));
		}else if(!unstuck&&!rotate){
			leftIntake.set(-0.6*(OI.getInstance().getOperatorGamepad().getRightTriggerAxis()-OI.getInstance().getOperatorGamepad().getLeftTriggerAxis()));
			rightIntake.set(-0.6*(OI.getInstance().getOperatorGamepad().getRightTriggerAxis()-OI.getInstance().getOperatorGamepad().getLeftTriggerAxis()));
		}*/
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

	public static boolean isIntakeIn(){
		return innerWheelPiston.get()==Value.kForward;
	}
	
	public static void setIntakePiston(IntakePistonState state) {
		if(state == IntakePistonState.DOWN) {
			intakePiston.set(Value.kForward);
		} else if(state == IntakePistonState.UP) {
			intakePiston.set(Value.kReverse);
		} else if(state == IntakePistonState.IN){
			innerWheelPiston.set(Value.kForward);
		} else if(state == IntakePistonState.OUT){
			innerWheelPiston.set(Value.kReverse);
		}
	}
	
	public void shootPowercube(){
		//if(isIntakeUp())
			//setSpeed(-.7);
		//else 
			//setSpeed(0);
	}

	@Override
	public void controlLoopUpdate() {
		//if(Math.abs(OI.getInstance().getDriverGamepad().getRightTriggerAxis()-OI.getInstance().getDriverGamepad().getLeftTriggerAxis())>0&&innerWheelPiston.get()==Value.kReverse)
			//setIntakePiston(IntakePistonState.IN);
		//setSpeed(0.6*(OI.getInstance().getDriverGamepad().getRightTriggerAxis()-OI.getInstance().getDriverGamepad().getLeftTriggerAxis()));
	}

	@Override
	public void setPeriodMs(long periodMs) {
		// TODO Auto-generated method stub
		
	}

	public void setSpeed(double mSpeed, double mSpeed2) {
		leftIntake.set(-mSpeed);
		rightIntake.set(-mSpeed2);
		
	}
	
	public void setLeftSpeed(double speed) {
		leftIntake.set(speed);
	}
	
	public void setRightSpeed(double speed) {
		rightIntake.set(-speed);
	}
}