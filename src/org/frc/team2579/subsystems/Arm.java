package org.frc.team2579.subsystems;

import org.frc.team2579.OI;
import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;
import org.frc.team2579.subsystems.Intake.IntakePistonState;
import org.frc.team2579.utility.ControlLoopable;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
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
	
	private ArmControlMode controlMode = ArmControlMode.SENSORED;
	
	public static DoubleSolenoid clawPiston, shootPiston, shiftPiston;
	private WPI_TalonSRX armTalon;
	private WPI_VictorSPX armFollower1;
	private WPI_VictorSPX armFollower2;
	
	private static final double NATIVE_TO_ANGLE_FACTOR = (80/12)*(60/14);
	private static double offset;
	private static final double ARM_MOTOR_VOLTAGE_PERCENT_LIMIT = 3.5/12;
	public double mAngle;
	public static final double SCALE_ANGLE_SETPOINT = 240;
	public static final double SWITCH_ANGLE_SETPOINT = 100;
	public static double mArmOnTargetTolerance = 10;
	public static double mArmKp = 1.375;
    public static double mArmKi = 0.001;
    public static double mArmKd = 0.0;
    public static double mArmKf = 1;
    public static int mArmIZone = (int) (1023.0 / mArmKp);
    public static double mArmRampRate = 0;
    public static int mArmAllowableError = 0;
    
    private DigitalInput homeLimit;
	
	public Arm() {
		try {
			clawPiston = new DoubleSolenoid(RobotMap.CLAW_IN_PCM_ID,RobotMap.CLAW_OUT_PCM_ID);
			shootPiston = new DoubleSolenoid(RobotMap.SHOOT_IN_PCM_ID,RobotMap.SHOOT_OUT_PCM_ID);
			shiftPiston = new DoubleSolenoid(RobotMap.SHIFT_IN_PCM_ID,RobotMap.SHIFT_OUT_PCM_ID);
			
			homeLimit = new DigitalInput(RobotMap.ARM_HOME_LIMIT_PORT);
			
			armTalon = new WPI_TalonSRX(RobotMap.ARM_TALON1_CAN_ID);
			armFollower1 = new WPI_VictorSPX(RobotMap.ARM_VICTOR1_CAN_ID);
			armFollower2 = new WPI_VictorSPX(RobotMap.ARM_VICTOR2_CAN_ID);
			
			armFollower1.follow(armTalon);
			armFollower2.follow(armTalon);
			//armFollower2.setInverted(true);
			//test
			armFollower1.setInverted(true);
			armTalon.setInverted(true);
			armTalon.setNeutralMode(NeutralMode.Brake);
			armTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
			//armTalon.setSensorPhase(true);
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
			mAngle = 0;
		} catch (Exception e) {
			System.err.println("An error occurred in the Arm constructor");
		}
	}
	
	public void setArmPiston(ArmPistonState state){
		if(state == ArmPistonState.SHOOT) {
			clawPiston.set(Value.kReverse);
			Timer.delay(.15);
			shootPiston.set(Value.kForward);
			Timer.delay(.5);
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
			armTalon.set(ControlMode.Position,(mAngle-offset)*NATIVE_TO_ANGLE_FACTOR);
		}
	}
	
	public void controlLoopUpdate() {
		if (controlMode == ArmControlMode.MANUAL) {
			moveWithJoystick();
		} else if (controlMode == ArmControlMode.SENSORED) {
			moveWithFeedBack();
		}

		if(homeLimit.get()) {
			resetArmEncoder();
		}
	}
	public void moveWithFeedBack() {
		setArmAngle(ArmControlMode.SENSORED,(mAngle-offset)*NATIVE_TO_ANGLE_FACTOR);
	}
	
	private void moveWithJoystick() {
		//setArmAngle(ArmControlMode.MANUAL,OI.getInstance().getOperatorGamepad().getRightYAxis() * 4096);
		if(!(getArmAngle() < 2 && OI.getInstance().getOperatorGamepad().getRightYAxis() > 0.2))
			//if(Intake.isIntakeIn())
				//Intake.setIntakePiston(IntakePistonState.OUT);
			armTalon.set(OI.getInstance().getOperatorGamepad().getRightYAxis());
	}
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	public void updateStatus(Robot.OperationMode operationMode) {
		SmartDashboard.putNumber("Arm Angle: ", getArmAngle());
		SmartDashboard.putNumber("Arm Setpoint", getAngleSetpoint());
		SmartDashboard.putNumber("isOnTarget result", Math.abs(getArmAngle() - Math.abs(getAngleSetpoint())));
        SmartDashboard.putBoolean("onTarget", isOnTarget());
        SmartDashboard.putNumber("Arm Motor Current", armTalon.getOutputCurrent());
		SmartDashboard.putNumber("PWM:", armTalon.getMotorOutputVoltage());
		SmartDashboard.putBoolean("isHome", isHome());
        if (operationMode == Robot.OperationMode.TEST) {
		}
	}

	public boolean isOnTarget() {
		if(getAngleSetpoint()==0 && isHome())
			return true;
		else
			return (armTalon.getControlMode() == ControlMode.Position
                && Math.abs(getAngleSetpoint() - Math.abs(getArmAngle())) < mArmOnTargetTolerance);
	}

	private double getAngleSetpoint() {
		return armTalon.getClosedLoopTarget(0);
	}

	private double getArmAngle() {
		//return ((double)armTalon.getSelectedSensorPosition(0))/NATIVE_TO_ANGLE_FACTOR;
		return armTalon.getSensorCollection().getPulseWidthPosition()/NATIVE_TO_ANGLE_FACTOR-offset;
	}
	
	public void resetArmEncoder() {
		offset=armTalon.getSensorCollection().getPulseWidthPosition()/NATIVE_TO_ANGLE_FACTOR;
		armTalon.setSelectedSensorPosition(0, 0, 10);
	}
	
	public void setControlMode(ArmControlMode mode){
		this.controlMode = mode;
		resetArmEncoder();
	}
	
	public ArmControlMode getMode(){
		return controlMode;
	}
	
	public boolean isHome() {
		return homeLimit.get();
	}

	@Override
	public void setPeriodMs(long periodMs) {
		// TODO Auto-generated method stub
		
	}
}
