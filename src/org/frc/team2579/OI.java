package org.frc.team2579;

import org.frc.team2579.commands.ArmGearboxPistonPosition;
import org.frc.team2579.commands.ArmPistonPosition;
import org.frc.team2579.commands.ArmPosition;
import org.frc.team2579.commands.IntakeDown;
import org.frc.team2579.commands.IntakeInnerWheelPosition;
import org.frc.team2579.commands.IntakePosition;
import org.frc.team2579.commands.IntakeSpeed;
import org.frc.team2579.commands.IntakeSpeedOff;
import org.frc.team2579.commands.IntakeUp;
import org.frc.team2579.controller.GamePad;
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
		
		//JoystickButton intakeOut = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.LEFT_TRIGGER_AXIS);
        //intakeOut.whileHeld(new IntakeSpeed(m_driverGamepad.getJoyStick().getRawAxis(GamePad.LEFT_TRIGGER_AXIS)));
        //intakeOut.whenReleased(new IntakeSpeedOff());
        
        //JoystickButton intakeIn = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.RIGHT_TRIGGER_AXIS);
        //intakeIn.whileHeld(new IntakeSpeed(m_driverGamepad.getJoyStick().getRawAxis(GamePad.RIGHT_TRIGGER_AXIS)));
        //intakeIn.whenReleased(new IntakeSpeedOff());
        
        JoystickButton intakeUp = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.LEFT_BUMPER_BUTTON);
        intakeUp.whileActive(new IntakeUp());
        
        JoystickButton intakeDown = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.RIGHT_BUMPER_BUTTON);
        intakeDown.whileActive(new IntakeDown());
        
        JoystickButton intakeInnerIn = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.A_BUTTON);
        intakeInnerIn.whileActive(new IntakeInnerWheelPosition(IntakePistonState.IN));
        
        JoystickButton intakeInnerOut = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.Y_BUTTON);
        intakeInnerOut.whileActive(new IntakeInnerWheelPosition(IntakePistonState.OUT));
        
        JoystickButton armReload = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.LEFT_BUMPER_BUTTON);
        armReload.whileActive(new ArmPistonPosition(ArmPistonState.RELOAD));
        
        JoystickButton armShoot = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.RIGHT_BUMPER_BUTTON);
        armShoot.whileActive(new ArmPistonPosition(ArmPistonState.SHOOT));
        
        JoystickButton armHome = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.A_BUTTON);
        armHome.whenPressed(new ArmPosition(ArmControlMode.SENSORED,0));
        
        JoystickButton armSwitch = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.B_BUTTON);
        armSwitch.whenPressed(new ArmPosition(ArmControlMode.SENSORED,Arm.SWITCH_ANGLE_SETPOINT));
        
        JoystickButton armScale = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.Y_BUTTON);
        armScale.whenPressed(new ArmPosition(ArmControlMode.SENSORED,Arm.SCALE_ANGLE_SETPOINT));
	
        JoystickButton clawGrab = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.X_BUTTON);
        clawGrab.whenPressed(new ArmPistonPosition(ArmPistonState.GRAB));
        
        JoystickButton intakeIn = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.START_BUTTON);
        intakeIn.whileActive(new IntakeInnerWheelPosition(IntakePistonState.IN));
        
        JoystickButton intakeOut = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.BACK_BUTTON);
        intakeOut.whileActive(new IntakeInnerWheelPosition(IntakePistonState.OUT));
        
        //Pneumatics Diagonostics
        
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
		
		Button testClawGrab = new InternalButton();
		testClawGrab.whenPressed(new ArmPistonPosition(ArmPistonState.GRAB));
		SmartDashboard.putData("Arm Claw Grab", testClawGrab);
		
		Button testClawRelease = new InternalButton();
		testClawRelease.whenPressed(new ArmPistonPosition(ArmPistonState.RELEASE));
		SmartDashboard.putData("Arm Claw Release", testClawRelease);
		
		Button testArmShoot = new InternalButton();
		testArmShoot.whenPressed(new ArmPistonPosition(ArmPistonState.SHOOT));
		SmartDashboard.putData("Arm Shoot", testArmShoot);
		
		Button testArmReload = new InternalButton();
		testArmReload.whenPressed(new ArmPistonPosition(ArmPistonState.RELOAD));
		SmartDashboard.putData("Arm Reload", testArmReload);
		
		Button testArmGearboxArmDog = new InternalButton();
		testArmGearboxArmDog.whenPressed(new ArmGearboxPistonPosition(ArmGearboxState.ARM_DOG));
		SmartDashboard.putData("Gearbox ARM Dog", testArmGearboxArmDog);
		
		Button testArmGearboxClimbDog = new InternalButton();
		testArmGearboxClimbDog.whenPressed(new ArmGearboxPistonPosition(ArmGearboxState.CLIMB_DOG));
		SmartDashboard.putData("Gearbox CLIMB Dog", testArmGearboxClimbDog);
	
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
