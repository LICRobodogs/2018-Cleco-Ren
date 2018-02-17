package org.frc.team2579.subsystems;

//import org.frc.team2579.utility.CANTalonEncoder;
import org.frc.team2579.OI;
import org.frc.team2579.Robot;
import org.frc.team2579.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import org.frc.team2579.utility.ControlLoopable;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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

	private double m_moveOutput = 0.0;
	private double m_steerOutput = 0.0;

	// NavX

	//private AHRS mxp = new AHRS(SPI.Port.kMXP);

	// Set control mode
	private DriveTrainControlMode controlMode = DriveTrainControlMode.JOYSTICK;

	// Robot Intrinsics
	public static double m_periodMs;


/*	private final PIDOutput headingControlPIDOut = new PIDOutput() {
		public void pidWrite(double d) {
		}
	};

	PIDController headingControlPID = new PIDController(HEADING_CONTROL_P,
			HEADING_CONTROL_I, HEADING_CONTROL_D, mxp, headingControlPIDOut,
			HEADING_CONTROL_PERIOD);

*/
	private WPI_TalonSRX leftDrive1;
	private WPI_VictorSPX leftDrive2;

	private WPI_TalonSRX rightDrive1;
	private WPI_VictorSPX rightDrive2;

	private DifferentialDrive m_drive;

	//private List<double[]> autonDesiresList = new ArrayList<double[]>();

	//private double[] vTime = new double[5];

	public DriveTrain() {
		try {
			leftDrive1 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID);
			leftDrive2 = new WPI_VictorSPX(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID);
			//leftDrive1.setPID(LEFT_P, LEFT_I, LEFT_D);

			rightDrive1 = new WPI_TalonSRX(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID);
			rightDrive2 = new WPI_VictorSPX(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID);
			//rightDrive1.setPID(RIGHT_P, RIGHT_I, RIGHT_D);

			leftDrive2.follow(leftDrive1);

			rightDrive2.follow(rightDrive1);

			leftDrive1.setNeutralMode(NeutralMode.Brake);

			rightDrive1.setNeutralMode(NeutralMode.Brake);
			
/*			leftDrive1
					.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
			rightDrive1
					.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);*/

			m_drive = new DifferentialDrive(leftDrive1, rightDrive1);
			
			m_drive.setSafetyEnabled(false);

			/*headingControlPID.setInputRange(-180.0f, 180.0f);
			headingControlPID.setOutputRange(-180.0f, 180.0f);
			headingControlPID.setAbsoluteTolerance(HEADING_CONTROL_MAX_ERROR);
			headingControlPID.setContinuous();*/

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

		m_moveInput = OI.getInstance().getDriverGamepad().getLeftYAxis();
		//m_steerInput = OI.getInstance().getDriverGamepad().getRightXAxis();
		if(m_moveInput<-0.1) {
			m_steerInput = OI.getInstance().getDriverGamepad().getLeftXAxis();
		}else if(m_moveInput>0.1) {
			m_steerInput = -OI.getInstance().getDriverGamepad().getLeftXAxis();
		}else {
			m_steerInput = OI.getInstance().getDriverGamepad().getLeftXAxis();
		}
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

/*	private double joystickSensitivityAdjust(double rawInput, double C1,
			double C2, double C3) {
		// Accepts raw joystick input, outputs adjusted values based on
		// nonlinear, 2nd order polynomial mapping.

		double adjustedOutput = 0.0;

		if (rawInput < 0) {
			adjustedOutput = -(C1 * Math.pow(rawInput, 2) + C2 * rawInput + C3);
		} else if (rawInput > 0) {
			adjustedOutput = (C1 * Math.pow(rawInput, 2) + C2 * rawInput + C3);
		}

		return adjustedOutput;

	}*/

/*	public void addMoveDistance(double distanceDesire) {
		// accepts distance (inches), adds move distance command to auton
		// routine
		// note that distance gets transformed to ticks
		double distanceDesireTicks = distanceDesire * ENCODER_TICKS_TO_INCHES;
		double[] waypoint = new double[] { distanceDesireTicks, 0 };
		autonDesiresList.add(waypoint);
	}

	public void addTurnAngle(double angleDesire) {
		// accepts absolute heading angle (degrees), adds turn command to auton
		// routine
		// note that angle gets transformed to radians
		double angleDesireRad = angleDesire * Math.PI / 180;
		double[] waypoint = new double[] { 0, angleDesireRad };
		autonDesiresList.add(waypoint);
	}

	public void executeMovement() {
		double[] waypoint = autonDesiresList.get(0);
		if (waypoint[0] == 0.0) {
			// turn desired
		} else if (waypoint[1] == 0.0) {
			// straight desired
		} else {
			System.out.println("Auton routine waypoints exhausted.");
		}
	}

	private void generateProfile(CANTalon drive, double setPoint) {
		CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
		drive.clearMotionProfileTrajectories();
		double t, m;
		if (drive.getEncVelocity() < (.9*MAX_AUTON_VELOCITY)) {
			if (setPoint <= MAX_ACCEL_DIST) {
				t = 0;
				m = (0 - drive.getSpeed()) / m_periodMs;
				if(Math.abs(m)>MAX_AUTON_ACCEL)
					m = -MAX_AUTON_ACCEL;
				for (int i = 0; i < vTime.length; i++) {
					t += 10;
					vTime[i] = m * (t) + drive.getSpeed();
					point.position = (drive.getSpeed() * t)
							+ (.5 * m * (t * t));
					point.velocity = (vTime[i]);
					point.timeDurMs = 10;
					point.profileSlotSelect = 0;

					point.zeroPos = false;
					if (i == 0)
						point.zeroPos = true;

					point.isLastPoint = false;
					if ((i + 1) == vTime.length)
						point.isLastPoint = true;

					drive.pushMotionProfileTrajectory(point);
				}
			} else if (setPoint >= MAX_ACCEL_DIST) {
				t = 0;
				m = (MAX_AUTON_VELOCITY - drive.getSpeed()) / m_periodMs;
				if(Math.abs(m)>MAX_AUTON_ACCEL)
					m = MAX_AUTON_ACCEL;
				for (int i = 0; i < vTime.length; i++) {
					t += 10;
					vTime[i] = m * (t) + drive.getSpeed();
					point.position = (drive.getSpeed() * t)
							+ (.5 * m * (t * t));
					point.velocity = (vTime[i]);
					point.timeDurMs = 10;
					point.profileSlotSelect = 0;

					point.zeroPos = false;
					if (i == 0)
						point.zeroPos = true;

					point.isLastPoint = false;
					if ((i + 1) == vTime.length)
						point.isLastPoint = true;

					drive.pushMotionProfileTrajectory(point);
				}
			}
		}
	}

	private void generateDistancePath(double targetDistance) {

	}

	private void generateTurnPath(double targetAngle) {
		double leftVel = leftDrive1.getEncVelocity();
		double rightVel = rightDrive1.getEncVelocity();
		double leftPos = leftDrive1.getEncPosition();
		double rightPos = rightDrive1.getEncPosition();
		double mxpYaw = mxp.getYaw() * Math.PI / 180;

		double deltaLeftRight = (targetAngle - mxpYaw) * WHEEL_TO_WHEEL_DIST
				* ENCODER_TICKS_TO_INCHES; // positive is left wheel moving
											// more, in ticks

		if (Math.abs(deltaLeftRight) < 100) {
			// REMOVE POINT
		} else {
			getNextPoint();
		}
		if (Math.abs(deltaLeftRight) > MAX_ACCEL_DIST) {

		}
	}*/

	private void getNextPoint() {

	}

	@Override
	public void controlLoopUpdate() {
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			driveWithJoystick();
		} else if (controlMode == DriveTrainControlMode.AUTON) {
			//executeMovement();
		}

	}

	@Override
	public void setPeriodMs(long periodMs) {
		m_periodMs = periodMs;

	}

	public void updateStatus(Robot.OperationMode operationMode) {
		if (operationMode == Robot.OperationMode.COMPETITION) {
			/*SmartDashboard.putNumber("Current Robot Angle: ", mxp.getYaw());
			SmartDashboard.putNumber("Current Left Robot Drive Position: ",
					leftDrive1.getPosition());
			SmartDashboard.putNumber("Current Right Robot Drive Position: ",
					rightDrive1.getPosition());
			SmartDashboard.putNumber("Current Left Robot Drive EncVelocity: ",
					leftDrive1.getEncVelocity());
			SmartDashboard.putNumber("Current Right Robot Drive EncVelocity: ",
					rightDrive1.getEncVelocity());*/
		}
	}

}