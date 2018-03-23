/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team2579;

import org.frc.team2579.commands.auton.CenterSwitchAuton;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.DriveTrain;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.DriveTrain.DriveTrainControlMode;
import org.frc.team2579.utility.ControlLooper;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	public static DriveTrain driveTrain;
	public static Intake intake;
	public static Arm arm;

	Command autonomousCommand;
	public static SendableChooser<Command> autonChooser;

	public static final ControlLooper controlLoop = new ControlLooper("Main control loop", 10);
	public static OI oi;

	public static enum OperationMode {
		TEST, COMPETITION
	};

	public static OperationMode operationMode = OperationMode.COMPETITION;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		driveTrain = new DriveTrain();
		intake = new Intake();
		arm = new Arm();
		controlLoop.addLoopable(driveTrain);
		controlLoop.addLoopable(arm);
		controlLoop.addLoopable(intake);
		/*
		 * m_chooser.addDefault("Default Auto", kDefaultAuto);
		 * m_chooser.addObject("My Auto", kCustomAuto);
		 * SmartDashboard.putData("Auto choices", m_chooser);
		 */
		setupAutonChooser();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		// DriveTrain.resetMP();
		// DriveTrain.resetPosition();
		driveTrain.resetTalons();

		driveTrain.setControlMode(DriveTrainControlMode.AUTON, 0);
		driveTrain.setPeriodMs(10);
		//controlLoop.start();

		autonomousCommand = autonChooser.getSelected();
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

	public void teleopInit() {
		//autonomousCommand.cancel();
		Scheduler.getInstance().removeAll();
		driveTrain.stopProfileDrive();
		driveTrain.zeroEncoders();
		driveTrain.setControlMode(DriveTrainControlMode.JOYSTICK, 0);
		arm.setControlMode(ArmControlMode.MANUAL);
		driveTrain.setPeriodMs(10);
		//controlLoop.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		updateStatus();
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void disabledInit() {
		arm.resetArmEncoder();
		Scheduler.getInstance().removeAll();
		driveTrain.stopProfileDrive();
	}

	public void disabledPeriodic() {
		// updateStatus();
		// DriveTrain.resetMP();
		// DriveTrain.resetPosition();
		Scheduler.getInstance().run();
	}

	public void updateStatus() {
		arm.updateStatus(operationMode);
		driveTrain.updateStatus(operationMode);
	}

	public void setupAutonChooser() {
		autonChooser = new SendableChooser<>();
		autonChooser.addDefault("Switch Left", new CenterSwitchAuton());
		autonChooser.addObject("Do Nothing", new CommandGroup());
		SmartDashboard.putData("Auton Setting", autonChooser);
	}
}
