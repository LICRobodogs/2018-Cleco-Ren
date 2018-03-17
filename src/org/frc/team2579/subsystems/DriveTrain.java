package org.frc.team2579.subsystems;

//import org.frc.team2579.utility.CANTalonEncoder;
import org.frc.team2579.OI;
import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.frc.team2579.utility.ControlLoopable;
import org.frc.team2579.utility.instrumentation;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements ControlLoopable {
	public static enum DriveTrainControlMode {
		JOYSTICK, AUTON, HOLD, TEST
	};

	// Input Device Constants

	public static final double DRIVER_JOY1_C1 = .0089;
	public static final double DRIVER_JOY1_C2 = .0737;
	public static final double DRIVER_JOY1_C3 = 2.4126;

	private double m_moveInput = 0.0;
	private double m_steerInput = 0.0;

	// NavX

	//private AHRS mxp = new AHRS(SPI.Port.kMXP);

	// Set control mode
	private DriveTrainControlMode controlMode = DriveTrainControlMode.JOYSTICK;

	static SetValueMotionProfile setValue;
	
	// Robot Intrinsics
	public static double m_periodMs;

	public static final double ENCODER_TICKS_TO_INCHES = 4096 * Math.PI * 4.0;
	
	public static final double LEFT_P = 1;
	public static final double LEFT_I = 0.0;
	public static final double LEFT_D = 0.0;
	public static final double LEFT_F = 1.0;

	public static final double RIGHT_P = 1;
	public static final double RIGHT_I = 0.0;
	public static final double RIGHT_D = 0.0;
	public static final double RIGHT_F = 1.0;
	
	private static WPI_TalonSRX leftDrive1;//Vel:5636u/100ms
	private WPI_TalonSRX leftDrive2;

	private static WPI_TalonSRX rightDrive1;//Vel:5802u/100ms
	private WPI_TalonSRX rightDrive2;

	private static MotionProfileStatus statusLeft, statusRight;
	
	private DifferentialDrive m_drive;
	
	private static boolean isRunning;

	public DriveTrain() {
		try {
			leftDrive1 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID);
			//leftDrive2 = new WPI_VictorSPX(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID);
			leftDrive2 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID);
			//leftDrive1.setPID(LEFT_P, LEFT_I, LEFT_D);

			rightDrive1 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID);
			//rightDrive2 = new WPI_VictorSPX(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID);
			rightDrive2 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID);
			//rightDrive1.setPID(RIGHT_P, RIGHT_I, RIGHT_D);

			leftDrive2.follow(leftDrive1);
			rightDrive2.follow(rightDrive1);
			leftDrive1.setNeutralMode(NeutralMode.Brake);
			rightDrive1.setNeutralMode(NeutralMode.Brake);
			leftDrive2.setNeutralMode(NeutralMode.Brake);
			rightDrive2.setNeutralMode(NeutralMode.Brake);
			leftDrive1.configOpenloopRamp(0.2, 0);
			rightDrive1.configOpenloopRamp(0.2, 0);
			leftDrive1.configPeakOutputForward(1,10);
			leftDrive2.configPeakOutputForward(1,10);
			rightDrive1.configPeakOutputForward(1,10);
			rightDrive2.configPeakOutputForward(1,10);
			leftDrive1.configPeakOutputReverse(-1,10);
			leftDrive2.configPeakOutputReverse(-1,10);
			rightDrive1.configPeakOutputReverse(-1,10);
			rightDrive2.configPeakOutputReverse(-1,10);
			
			leftDrive1.setSensorPhase(false);
			
			leftDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10);
			rightDrive1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10);

			m_drive = new DifferentialDrive(leftDrive1, rightDrive1);
			
			m_drive.setSafetyEnabled(false);
			
			statusLeft = new MotionProfileStatus();
	        statusRight = new MotionProfileStatus();
	        
		} catch (Exception e) {
			System.err
					.println("An error occurred in the DriveTrain constructor");
		}
	}

	public void setSpeed(double speed) {
		// if (speed == 0) {
		setControlMode(DriveTrainControlMode.JOYSTICK, speed);
		/*
		 * } else { setControlMode(DriveTrainControlMode.TEST);
		 * rightDrive1.set(speed); leftDrive1.set(speed); }
		 */
	}

	public void driveWithJoystick() {
		if (controlMode != DriveTrainControlMode.JOYSTICK || m_drive == null)
			return;

		//m_moveInput = OI.getInstance().getDriverGamepad().getLeftYAxis();
		m_moveInput = -(OI.getInstance().getDriverGamepad().getRightTriggerAxis()-OI.getInstance().getDriverGamepad().getLeftTriggerAxis());
		m_steerInput = OI.getInstance().getDriverGamepad().getLeftXAxis();
		/*if(m_moveInput<0) {
			m_steerInput = OI.getInstance().getDriverGamepad().getLeftXAxis();
		}else if(m_moveInput>0) {
			m_steerInput = -OI.getInstance().getDriverGamepad().getLeftXAxis();
		}else {
			m_steerInput = OI.getInstance().getDriverGamepad().getLeftXAxis();
		}*/
/*		m_moveOutput = joystickSensitivityAdjust(m_moveInput, DRIVER_JOY1_C1,
				DRIVER_JOY1_C2, DRIVER_JOY1_C3);
		m_steerOutput = joystickSensitivityAdjust(m_steerInput, DRIVER_JOY1_C1,
				DRIVER_JOY1_C2, DRIVER_JOY1_C3);*/

		m_drive.curvatureDrive(m_moveInput, -m_steerInput, true);
	}

	public void setControlMode(DriveTrainControlMode controlMode, double speed) {
		this.controlMode = controlMode;
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			leftDrive1.set(ControlMode.PercentOutput, speed);
			rightDrive1.set(ControlMode.PercentOutput, speed);
		} else if (controlMode == DriveTrainControlMode.TEST) {
			leftDrive1.set(ControlMode.Velocity, speed);
			rightDrive1.set(ControlMode.Velocity, speed);
		} else if (controlMode == DriveTrainControlMode.HOLD) {

		} else if (controlMode == DriveTrainControlMode.AUTON) {
			leftDrive1.set(ControlMode.MotionProfile,speed);
			rightDrive1.set(ControlMode.MotionProfile,speed);
		}
	}

	@Override
	public void initDefaultCommand() {
		// This needs to be here for reasons.
	}

	@Override
	public void controlLoopUpdate() {
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			driveWithJoystick();
		} else if (controlMode == DriveTrainControlMode.AUTON) {
			//executeMovement();
			leftDrive1.getMotionProfileStatus(statusLeft);
			rightDrive1.getMotionProfileStatus(statusRight);
		}

	}

	@Override
	public void setPeriodMs(long periodMs) {
		m_periodMs = periodMs;

	}

	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.COMPETITION) {
			SmartDashboard.putNumber("Current Left Robot Drive Position: ",
					leftDrive1.getSelectedSensorPosition(0));
			SmartDashboard.putNumber("Current Right Robot Drive Position: ",
					rightDrive1.getSelectedSensorPosition(0));
			SmartDashboard.putNumber("Current Left Robot Drive EncVelocity: ",
					leftDrive1.getSelectedSensorVelocity(0));
			SmartDashboard.putNumber("Current Right Robot Drive EncVelocity: ",
					rightDrive1.getSelectedSensorVelocity(0));
			SmartDashboard.putNumber("Current Right Robot Drive Error: ",
							rightDrive1.getClosedLoopError(0));
			SmartDashboard.putNumber("Current Left Robot Drive Error: ",
							leftDrive1.getClosedLoopError(0));
			SmartDashboard.putBoolean("isFinished", isFinished());
			SmartDashboard.putBoolean("isRunning", isRunning);
			SmartDashboard.putString("Left Drive TALON MODE: ", leftDrive1.getControlMode().toString());
			SmartDashboard.putString("Right Drive TALON MODE: ", rightDrive1.getControlMode().toString());
		}
	}
	
	public static boolean isFinished() {
		return
	            isRunning &&
	            statusLeft.activePointValid &&
	            statusLeft.isLast &&
	            statusRight.activePointValid &&
	            statusRight.isLast;
	}
	
	public static void setRunning(boolean isRunning) {
		DriveTrain.isRunning = isRunning;
	}
	
	public static void startFilling(double[][] profile, int totalCnt, char side) {
		/* create an empty point */
		TrajectoryPoint point = new TrajectoryPoint();
		System.out.println("In start filling");
		/* did we get an underrun condition since last time we checked ? */
		if (side=='L'&&statusLeft.hasUnderrun) {
			/* better log it so we know about it */
			instrumentation.OnUnderrun();
			/*
			 * clear the error. This flag does not auto clear, this way 
			 * we never miss logging it.
			 */
			leftDrive1.clearMotionProfileHasUnderrun(0);
		}else if (side=='R'&&statusRight.hasUnderrun) {
				instrumentation.OnUnderrun();
				rightDrive1.clearMotionProfileHasUnderrun(0);
		}
		/*
		 * just in case we are interrupting another MP and there is still buffer
		 * points in memory, clear it.
		 */
		if(side=='L')
			leftDrive1.clearMotionProfileTrajectories();
		else
			rightDrive1.clearMotionProfileTrajectories();
		
		/* This is fast since it's just into our TOP buffer */
		for (int i = 0; i < totalCnt; ++i) {
			double positionRot = profile[i][0];
			double velocityRPM = profile[i][1];
			/* for each point, fill our structure and pass it to API */
			point.position = positionRot;// * 4096; //Convert Revolutions to Units
			point.velocity = velocityRPM;// * 4096 / 600.0; //Convert RPM to Units/100ms
			point.headingDeg = 0; /* future feature - not used in this example*/
			point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
			point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
			point.timeDur = GetTrajectoryDuration((int)profile[i][2]);
			point.zeroPos = false;
			if (i == 0)
				point.zeroPos = true; /* set this to true on the first point */

			point.isLastPoint = false;
			if ((i + 1) == totalCnt)
				point.isLastPoint = true; /* set this to true on the last point  */
			if(side=='L')
				leftDrive1.pushMotionProfileTrajectory(point);
			else	
				rightDrive1.pushMotionProfileTrajectory(point);
			
		}
		
		
	}
	/**
	 * Find enum value if supported.
	 * @param durationMs
	 * @return enum equivalent of durationMs
	 */
	private static TrajectoryDuration GetTrajectoryDuration(int durationMs)
	{	 
		/* create return value */
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		/* convert duration to supported type */
		retval = retval.valueOf(durationMs);
		/* check that it is valid */
		if (retval.value != durationMs) {
			DriverStation.reportError("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead", false);		
		}
		/* pass to caller */
		return retval;
	}
	
	public static void startMP(){
		SetValueMotionProfile setOutput = getSetValue();
		
		leftDrive1.set(ControlMode.MotionProfile,setOutput.value);
		rightDrive1.set(ControlMode.MotionProfile, setOutput.value);
		leftDrive1.processMotionProfileBuffer();
		rightDrive1.processMotionProfileBuffer();
		setRunning(true);
		System.out.println("In StartMP");
		//System.out.println(setValue.toString());
	}
	
	public static SetValueMotionProfile getSetValue() {
		return setValue;
	}

	public static void setSetValue(SetValueMotionProfile newVal) {
		setValue = newVal;
	}
	
	public static void resetPosition(){
		leftDrive1.setSelectedSensorPosition(0, 0, 10);
		rightDrive1.setSelectedSensorPosition(0, 0, 10);
	}
	
	public static void resetMP(){
		leftDrive1.clearMotionProfileTrajectories();
		rightDrive1.clearMotionProfileTrajectories();
		setValue = SetValueMotionProfile.Disable;
		leftDrive1.set(setValue.value);
		rightDrive1.set(setValue.value);
	}
}