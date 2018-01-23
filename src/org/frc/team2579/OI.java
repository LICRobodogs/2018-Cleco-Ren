package org.frc.team2579;

import org.frc.team2579.commands.IntakeSpeed;
import org.frc.team2579.commands.IntakeSpeedOff;
import org.frc.team2579.controller.GamePad;
import org.frc.team2579.subsystems.Intake;

import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	private static OI instance;

	private GamePad m_driverGamepad;
	private GamePad m_operatorGamepad;

	private OI() {
		m_driverGamepad = new GamePad(RobotMap.DRIVER_GAMEPAD_USB_ID);
		m_operatorGamepad = new GamePad(RobotMap.OPERATOR_GAMEPAD_USB_ID);
		
		JoystickButton intakeIn = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.LEFT_BUMPER_BUTTON);
        intakeIn.whileHeld(new IntakeSpeed(Intake.INTAKE_LOAD_SPEED));
        intakeIn.whenReleased(new IntakeSpeedOff());
        
        JoystickButton intakeOut = new JoystickButton(m_driverGamepad.getJoyStick(), GamePad.RIGHT_BUMPER_BUTTON);
        intakeOut.whileHeld(new IntakeSpeed(Intake.INTAKE_EJECT_SPEED));
        intakeOut.whenReleased(new IntakeSpeedOff());
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
