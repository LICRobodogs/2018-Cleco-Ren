package org.frc.team2579.subsystems;

import org.frc.team2579.OI;
import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;
import org.frc.team2579.utility.ControlLoopable;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm extends Subsystem implements ControlLoopable {
	public static enum ArmPistonState {
		GRAB, RELEASE, SHOOT, RELOAD
	}
	
	public static enum ArmGearboxState {
		ARM_DOG, CLIMB_DOG
	}
	
	public static enum ArmControlMode {
		MANUAL, SENSORED, HOLD, TEST
	}
	
	private ArmControlMode controlMode = ArmControlMode.MANUAL;
	
	public static DoubleSolenoid clawPiston, shootPiston, shiftPiston;
	private WPI_TalonSRX armTalon;
	private WPI_VictorSPX armFollower1;
	private WPI_VictorSPX armFollower2;
	
	private static final double NATIVE_TO_RPM_FACTOR = 10 * 60 / 12;
	private static final double ARM_MOTOR_VOLTAGE_PERCENT_LIMIT = 12/12;
	public double mAngle;
	public static final double SCALE_ANGLE_SETPOINT = 140;
	public static final double SWITCH_ANGLE_SETPOINT = 50;
	public static double mArmOnTargetTolerance = 5;
	public static double mArmKp = 0.1;
    public static double mArmKi = 0.001;
    public static double mArmKd = 0.0;
    public static double mArmKf = 1;
    public static int mArmIZone = (int) (1023.0 / mArmKp);
    public static double mArmRampRate = 0;
    public static int mArmAllowableError = 0;
	
	public Arm() {
		try {
			clawPiston = new DoubleSolenoid(RobotMap.CLAW_IN_PCM_ID,RobotMap.CLAW_OUT_PCM_ID);
			shootPiston = new DoubleSolenoid(RobotMap.SHOOT_IN_PCM_ID,RobotMap.SHOOT_OUT_PCM_ID);
			shiftPiston = new DoubleSolenoid(1,RobotMap.SHIFT_IN_PCM2_ID,RobotMap.SHIFT_OUT_PCM2_ID);
			
			armTalon = new WPI_TalonSRX(RobotMap.ARM_TALON1_CAN_ID);
			armFollower1 = new WPI_VictorSPX(RobotMap.ARM_VICTOR1_CAN_ID);
			armFollower2 = new WPI_VictorSPX(RobotMap.ARM_VICTOR2_CAN_ID);
			
			armFollower1.follow(armTalon);
			armFollower2.follow(armTalon);
			
			armTalon.setNeutralMode(NeutralMode.Brake);
			armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
			armTalon.config_kP(0, mArmKp, 10);
			armTalon.config_kI(0, mArmKi, 10);
			armTalon.config_kD(0, mArmKd, 10);
			armTalon.config_kF(0, mArmKf, 10);
			armTalon.config_IntegralZone(0, mArmIZone, 10);
			armTalon.configClosedloopRamp(mArmRampRate, 10);
			armTalon.configNominalOutputForward(0, 10);
			armTalon.configNominalOutputReverse(0, 10);
			armTalon.configPeakOutputForward(ARM_MOTOR_VOLTAGE_PERCENT_LIMIT, 10);
			armTalon.configPeakOutputReverse(-ARM_MOTOR_VOLTAGE_PERCENT_LIMIT, 10);
			resetArmEncoder();
			setArmGearbox(ArmGearboxState.ARM_DOG);
		} catch (Exception e) {
			System.err.println("An error occurred in the Arm constructor");
		}
	}
	
	public void setArmPiston(ArmPistonState state){
		if(state == ArmPistonState.SHOOT) {
			clawPiston.set(Value.kForward);
			Timer.delay(.1);
			shootPiston.set(Value.kReverse);
		} else if(state == ArmPistonState.RELOAD) {
			shootPiston.set(Value.kReverse);
		} else if(state == ArmPistonState.GRAB){
			clawPiston.set(Value.kForward);
		} else if(state == ArmPistonState.RELEASE){
			clawPiston.set(Value.kReverse);
		}
	}
	
	public void setArmGearbox(ArmGearboxState state){
		if(state == ArmGearboxState.ARM_DOG){
			shiftPiston.set(Value.kForward);
		} else if(state == ArmGearboxState.CLIMB_DOG) {
			shiftPiston.set(Value.kReverse);
		}
	}
	
	public void setArmAngle(ArmControlMode controlMode, double angle) {
		this.controlMode = controlMode;
		this.mAngle = angle;
		if (controlMode == ArmControlMode.MANUAL) {
			armTalon.set(ControlMode.Position, angle);
		} else if (controlMode == ArmControlMode.TEST) {

		} else if (controlMode == ArmControlMode.HOLD) {
			armTalon.set(ControlMode.Position, armTalon.getSelectedSensorPosition(0));
		} else if (controlMode == ArmControlMode.SENSORED) {
			armTalon.set(ControlMode.Position,angle);
		}
	}
	
	public void controlLoopUpdate() {
		if (controlMode == ArmControlMode.MANUAL) {
			moveWithJoystick();
		} else if (controlMode == ArmControlMode.SENSORED) {
			moveWithFeedBack();
		}

	}
	public void moveWithFeedBack() {
		setArmAngle(ArmControlMode.SENSORED,mAngle);
	}
	
	private void moveWithJoystick() {
		//setArmAngle(ArmControlMode.MANUAL,OI.getInstance().getOperatorGamepad().getRightYAxis() * 4096);
		armTalon.set(OI.getInstance().getOperatorGamepad().getRightYAxis());
	}
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Shooter Speed: ", getArmAngle());
		SmartDashboard.putNumber("flywheel_setpoint", getAngleSetpoint());
		SmartDashboard.putNumber("isOnTarget result", Math.abs(getArmAngle() + Math.abs(getAngleSetpoint())));
        SmartDashboard.putBoolean("flywheel_on_target", isOnTarget());
		if (operationMode == Robot.OperationMode.TEST) {
		}
	}

	public boolean isOnTarget() {
		return (armTalon.getControlMode() == ControlMode.Position
                && Math.abs(getArmAngle() + Math.abs(getAngleSetpoint())) < mArmOnTargetTolerance);
	}

	private double getAngleSetpoint() {
		return armTalon.getClosedLoopTarget(0);
	}

	private double getArmAngle() {
		return armTalon.getSelectedSensorPosition(0)*(1/4096);
	}
	
	public void resetArmEncoder() {
		armTalon.setSelectedSensorPosition(0, 0, 10);
	}
	
	public void setControlMode(ArmControlMode mode){
		this.controlMode = mode;
	}
	
	public ArmControlMode getMode(){
		return controlMode;
	}

	@Override
	public void setPeriodMs(long periodMs) {
		// TODO Auto-generated method stub
		
	}
}
