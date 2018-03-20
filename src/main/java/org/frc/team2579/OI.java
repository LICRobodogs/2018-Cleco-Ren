package org.frc.team2579;

import org.frc.team2579.commands.ArmGearboxPistonPosition;
import org.frc.team2579.commands.ArmGracefulDown;
import org.frc.team2579.commands.ArmHome;
import org.frc.team2579.commands.ArmPistonPosition;
import org.frc.team2579.commands.ArmPosition;
import org.frc.team2579.commands.ArmScaleScore;
import org.frc.team2579.commands.ArmSwitchScore;
import org.frc.team2579.commands.IntakeDown;
import org.frc.team2579.commands.IntakeInnerWheelPosition;
import org.frc.team2579.commands.IntakePosition;
import org.frc.team2579.commands.IntakeQuickInOut;
import org.frc.team2579.commands.IntakeRotate;
import org.frc.team2579.commands.IntakeSpeed;
import org.frc.team2579.commands.IntakeSpeedOff;
import org.frc.team2579.commands.IntakeUnstuck;
import org.frc.team2579.commands.IntakeUp;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.controller.GamePad.DPadButton;
import org.frc.team2579.controller.GamePadTriggerButton;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;

import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmGearboxState;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

public class OI {
	private static OI instance;

	private GamePad m_driverGamepad;
	private GamePad m_operatorGamepad;

	private OI() {
		m_driverGamepad = new GamePad(RobotMap.DRIVER_GAMEPAD_USB_ID);
		m_operatorGamepad = new GamePad(RobotMap.OPERATOR_GAMEPAD_USB_ID);

		// DRIVER CONTROLS
		/*
		 * JoystickButton armReload = new
		 * JoystickButton(m_driverGamepad.getJoyStick(),GamePad.LEFT_BUMPER_BUTTON);
		 * armReload.whenPressed(new ArmPistonPosition(ArmPistonState.RELOAD));
		 * 
		 * JoystickButton armShoot = new
		 * JoystickButton(m_driverGamepad.getJoyStick(),GamePad.RIGHT_BUMPER_BUTTON);
		 * armShoot.whenPressed(new ArmPistonPosition(ArmPistonState.SHOOT));
		 * 
		 * JoystickButton armHome = new
		 * JoystickButton(m_driverGamepad.getJoyStick(),GamePad.A_BUTTON);
		 * armHome.whenPressed(new ArmHome());
		 * 
		 * JoystickButton armSwitch = new
		 * JoystickButton(m_driverGamepad.getJoyStick(),GamePad.B_BUTTON);
		 * armSwitch.whenPressed(new ArmSwitchScore());
		 * 
		 * JoystickButton armScale = new
		 * JoystickButton(m_driverGamepad.getJoyStick(),GamePad.Y_BUTTON);
		 * armScale.whenPressed(new ArmScaleScore());
		 * 
		 * DPadButton armGearboxDogArm = new DPadButton(m_driverGamepad,
		 * DPadButton.Direction.RIGHT); armGearboxDogArm.whenPressed(new
		 * ArmGearboxPistonPosition(ArmGearboxState.ARM_DOG));
		 * 
		 * DPadButton armGearboxDogClimb = new DPadButton(m_driverGamepad,
		 * DPadButton.Direction.LEFT); armGearboxDogClimb.whenPressed(new
		 * ArmGearboxPistonPosition(ArmGearboxState.CLIMB_DOG));
		 */

		// OPERATOR CONTROLS
		GamePadTriggerButton intakeOut = new GamePadTriggerButton(m_operatorGamepad, GamePad.LEFT_TRIGGER_AXIS);
		intakeOut.whileHeld(new IntakeSpeed());
		intakeOut.whenReleased(new IntakeSpeedOff());

		GamePadTriggerButton intakeIn = new GamePadTriggerButton(m_operatorGamepad, GamePad.RIGHT_TRIGGER_AXIS);
		intakeIn.whileHeld(new IntakeSpeed());
		intakeIn.whenReleased(new IntakeSpeedOff());

		DPadButton intakeUp = new DPadButton(m_operatorGamepad, DPadButton.Direction.UP);
		intakeUp.whenPressed(new IntakeUp());

		DPadButton intakeDown = new DPadButton(m_operatorGamepad, DPadButton.Direction.DOWN);
		intakeDown.whenPressed(new IntakeDown());

		JoystickButton intakeUnstuck = new JoystickButton(m_operatorGamepad.getJoyStick(), GamePad.B_BUTTON);
		intakeUnstuck.whenPressed(new IntakeQuickInOut());

		/*
		 * JoystickButton clawRelease = new
		 * JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.LEFT_BUMPER_BUTTON);
		 * clawRelease.whenPressed(new ArmPistonPosition(ArmPistonState.RELEASE));
		 * 
		 * JoystickButton clawGrab = new
		 * JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.RIGHT_BUMPER_BUTTON);
		 * clawGrab.whenPressed(new ArmPistonPosition(ArmPistonState.GRAB));
		 */
		JoystickButton intakeArmIn = new JoystickButton(m_operatorGamepad.getJoyStick(), GamePad.A_BUTTON);
		intakeArmIn.whileActive(new IntakeInnerWheelPosition(IntakePistonState.IN));

		JoystickButton intakeArmOut = new JoystickButton(m_operatorGamepad.getJoyStick(), GamePad.X_BUTTON);
		intakeArmOut.whileActive(new IntakeInnerWheelPosition(IntakePistonState.OUT));

		// Pneumatics Diagonostics

		Button testIntakeUp = new InternalButton();
		testIntakeUp.whenPressed(new IntakePosition(IntakePistonState.UP));
		SmartDashboard.putData("Intake Outer UP", testIntakeUp);

		Button testIntakeDown = new InternalButton();
		testIntakeDown.whenPressed(new IntakePosition(IntakePistonState.DOWN));
		SmartDashboard.putData("Intake Outer DOWN", testIntakeDown);

		Button testInnerIntakeIn = new InternalButton();
		testInnerIntakeIn.whenPressed(new IntakeInnerWheelPosition(IntakePistonState.IN));
		SmartDashboard.putData("Intake Inner IN", testInnerIntakeIn);

		Button testInnerIntakeOut = new InternalButton();
		testInnerIntakeOut.whenPressed(new IntakeInnerWheelPosition(IntakePistonState.OUT));
		SmartDashboard.putData("Intake Inner OUT", testInnerIntakeOut);

		/*
		 * Button testClawGrab = new InternalButton(); testClawGrab.whenPressed(new
		 * ArmPistonPosition(ArmPistonState.GRAB));
		 * SmartDashboard.putData("Arm Claw Grab", testClawGrab);
		 * 
		 * Button testClawRelease = new InternalButton();
		 * testClawRelease.whenPressed(new ArmPistonPosition(ArmPistonState.RELEASE));
		 * SmartDashboard.putData("Arm Claw Release", testClawRelease);
		 * 
		 * Button testArmShoot = new InternalButton(); testArmShoot.whenPressed(new
		 * ArmPistonPosition(ArmPistonState.SHOOT)); SmartDashboard.putData("Arm Shoot",
		 * testArmShoot);
		 * 
		 * Button testArmReload = new InternalButton(); testArmReload.whenPressed(new
		 * ArmPistonPosition(ArmPistonState.RELOAD));
		 * SmartDashboard.putData("Arm Reload", testArmReload);
		 * 
		 * Button testArmGearboxArmDog = new InternalButton();
		 * testArmGearboxArmDog.whenPressed(new
		 * ArmGearboxPistonPosition(ArmGearboxState.ARM_DOG));
		 * SmartDashboard.putData("Gearbox ARM Dog", testArmGearboxArmDog);
		 * 
		 * Button testArmGearboxClimbDog = new InternalButton();
		 * testArmGearboxClimbDog.whenPressed(new
		 * ArmGearboxPistonPosition(ArmGearboxState.CLIMB_DOG));
		 * SmartDashboard.putData("Gearbox CLIMB Dog", testArmGearboxClimbDog);
		 */
	}

	public GamePad getDriverGamepad() {
		return m_driverGamepad;
	}

	public GamePad getOperatorGamepad() {
		return m_operatorGamepad;
	}

	public static OI getInstance() {
		if (instance == null) {
			instance = new OI();
		}
		return instance;
	}
}
