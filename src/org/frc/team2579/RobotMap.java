package org.frc.team2579;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// USB Port IDs
	public static final int DRIVER_GAMEPAD_USB_ID = 0;
	public static final int OPERATOR_GAMEPAD_USB_ID = 1;

	// MOTORS
	public static final int DRIVETRAIN_LEFT_MOTOR1_CAN_ID = 0;
	public static final int DRIVETRAIN_LEFT_MOTOR2_CAN_ID = 0;
	public static final int DRIVETRAIN_RIGHT_MOTOR1_CAN_ID = 1;
	public static final int DRIVETRAIN_RIGHT_MOTOR2_CAN_ID = 1;

	public static final int LEFT_INTAKE_PWM = 0;
	public static final int RIGHT_INTAKE_PWM = 1;                                       

	public static final int ARM_MOTOR1_CAN_ID = 2;
	public static final int ARM_MOTOR2_CAN_ID = 2;
	public static final int ARM_MOTOR3_CAN_ID = 3;

	// PNEUMATICS
	public static final int RIGHT_INTAKE_UP_PCM_ID = 7;
	public static final int RIGHT_INTAKE_DOWN_PCM_ID = 0;
	
	public static final int LEFT_INTAKE_UP_PCM_ID = 4;
	public static final int LEFT_INTAKE_DOWN_PCM_ID = 3;

}