package org.frc.team2579;

import org.frc.team2579.commands.ArmPistonPosition;
import org.frc.team2579.commands.ArmPosition;
import org.frc.team2579.commands.IntakeDown;
import org.frc.team2579.commands.IntakeSpeed;
import org.frc.team2579.commands.IntakeSpeedOff;
import org.frc.team2579.commands.IntakeUp;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;

import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	private static OI instance;

	private GamePad m_driverGamepad;
	private GamePad m_operatorGamepad;

	private OI() {
		m_driverGamepad = new GamePad(RobotMap.DRIVER_GAMEPAD_USB_ID);
		m_operatorGamepad = new GamePad(RobotMap.OPERATOR_GAMEPAD_USB_ID);
		
		JoystickButton intakeOut = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.LEFT_BUMPER_BUTTON);
        intakeOut.whileHeld(new IntakeSpeed(Intake.INTAKE_EJECT_SPEED));
        intakeOut.whenReleased(new IntakeSpeedOff());
        
        JoystickButton intakeIn = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.RIGHT_BUMPER_BUTTON);
        intakeIn.whileHeld(new IntakeSpeed(Intake.INTAKE_LOAD_SPEED));
        intakeIn.whenReleased(new IntakeSpeedOff());
        
        JoystickButton intakeUp = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.LEFT_TRIGGER_AXIS);
        intakeUp.whileActive(new IntakeUp());
        
        JoystickButton intakeDown = new JoystickButton(m_driverGamepad.getJoyStick(),GamePad.RIGHT_TRIGGER_AXIS);
        intakeDown.whileActive(new IntakeDown());
        
        JoystickButton armReload = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.LEFT_TRIGGER_AXIS);
        armReload.whileActive(new ArmPistonPosition(ArmPistonState.RELOAD));
        
        JoystickButton armShoot = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.RIGHT_TRIGGER_AXIS);
        armShoot.whileActive(new ArmPistonPosition(ArmPistonState.SHOOT));
        
        JoystickButton armHome = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.A_BUTTON);
        armHome.whenPressed(new ArmPosition(ArmControlMode.SENSORED,0));
        
        JoystickButton armSwitch = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.B_BUTTON);
        armSwitch.whenPressed(new ArmPosition(ArmControlMode.SENSORED,Arm.SWITCH_ANGLE_SETPOINT));
        
        JoystickButton armScale = new JoystickButton(m_operatorGamepad.getJoyStick(),GamePad.Y_BUTTON);
        armScale.whenPressed(new ArmPosition(ArmControlMode.SENSORED,Arm.SCALE_ANGLE_SETPOINT));
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
