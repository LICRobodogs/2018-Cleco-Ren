/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team2579;

import org.frc.team2579.commands.auton.CenterSwitchAuton;
import org.frc.team2579.commands.auton.LeftSideScale;
import org.frc.team2579.commands.auton.RightSideScale;
import org.frc.team2579.commands.auton.StraightOnly;
import org.frc.team2579.commands.auton.TwoCubeCenterAuton;
import org.frc.team2579.subsystems.Arm;
import org.frc.team2579.subsystems.DriveTrain;
import org.frc.team2579.subsystems.Intake;
import org.frc.team2579.subsystems.Intake.IntakePistonState;
import org.frc.team2579.subsystems.Arm.ArmControlMode;
import org.frc.team2579.subsystems.Arm.ArmPistonState;
import org.frc.team2579.subsystems.DriveTrain.DriveTrainControlMode;
import org.frc.team2579.utility.ControlLooper;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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

	@SuppressWarnings("unused")
	private boolean hasRun;

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
		hasRun = false;
		driveTrain = new DriveTrain();
		intake = new Intake();
		arm = new Arm();
		oi = new OI();
		controlLoop.addLoopable(driveTrain);
		controlLoop.addLoopable(arm);
		controlLoop.addLoopable(intake);
		/*
		 * m_chooser.addDefault("Default Auto", kDefaultAuto);
		 * m_chooser.addObject("My Auto", kCustomAuto);
		 * SmartDashboard.putData("Auto choices", m_chooser);
		 */
		setupAutonChooser();
		//CameraServer.getInstance().startAutomaticCapture();
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

		driveTrain.setControlMode(DriveTrainControlMode.JOYSTICK, 0);
		driveTrain.setPeriodMs(10);
		//controlLoop.start();

		/*autonomousCommand = autonChooser.getSelected();
		if (autonomousCommand != null) {
			if(autonomousCommand instanceof CenterSwitchAuton) {
				((CenterSwitchAuton) autonomousCommand).preInit();
			}
			autonomousCommand.start();
		}
		*/
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
		autonomousCommand = autonChooser.getSelected();
		if(!hasRun) { hasRun = true;
			if(autonomousCommand instanceof CenterSwitchAuton || autonomousCommand instanceof TwoCubeCenterAuton) {
				Timer.delay(2);
				if(DriverStation.getInstance().getGameSpecificMessage().charAt(0)==('L'))
					driveTrain.setSpeed(-0.3,0.65);
				else
					driveTrain.setSpeed(-0.65, 0.3);
				Timer.delay(1.0);
				driveTrain.setSpeed(-0.4, 0.4);
				Timer.delay(1.25);
				driveTrain.setSpeed(0, 0);
				intake.setSpeed(-0.55);
				Timer.delay(5);
				intake.setSpeed(0);
				if(autonomousCommand instanceof TwoCubeCenterAuton) {
					driveTrain.setSpeed(0.4, -0.4);
					Timer.delay(1.25);
					if(DriverStation.getInstance().getGameSpecificMessage().charAt(0)==('L'))
						driveTrain.setSpeed(0.3,-0.5);
					else
						driveTrain.setSpeed(0.5, -0.3);
					Timer.delay(1.0);
					intake.setIntakePiston(IntakePistonState.OUT);
					intake.setSpeed(-0.7);
					driveTrain.setSpeed(-0.4, 0.4);
					Timer.delay(1.5);
					driveTrain.setSpeed(0, 0);
					Timer.delay(3.0);
					intake.setSpeed(0.0);
				}
			}else if(autonomousCommand instanceof StraightOnly){
				Timer.delay(2.0);
				driveTrain.setSpeed(-0.4, 0.4);
				Timer.delay(1.9);
				driveTrain.setSpeed(0, 0);
				Timer.delay(5);
			}else if(autonomousCommand instanceof LeftSideScale){
				if(DriverStation.getInstance().getGameSpecificMessage().charAt(1)==('L')) {
					intake.setIntakePiston(IntakePistonState.DOWN);
					intake.setIntakePiston(IntakePistonState.OUT);
					intake.setSpeed(-0.7);
					Timer.delay(0.5);
					arm.setArmAngle(ArmControlMode.SENSORED, 210);
					Timer.delay(2.0);
					driveTrain.setSpeed(-0.4, 0.4);
					Timer.delay(4.0);
					driveTrain.setSpeed(0, 0);
					arm.setArmPiston(ArmPistonState.RELEASE);
					arm.setArmPiston(ArmPistonState.SHOOT);
					Timer.delay(1.0);
					driveTrain.setSpeed(0.4, -0.4);
					Timer.delay(1.9);
					driveTrain.setSpeed(0, 0);
					arm.setArmAngle(ArmControlMode.MANUAL, 0);
				}else {
					Timer.delay(2.0);
					driveTrain.setSpeed(-0.4, 0.4);
					Timer.delay(1.9);
					driveTrain.setSpeed(0, 0);
					Timer.delay(5);
				}
			}else if(autonomousCommand instanceof RightSideScale) {
				if(DriverStation.getInstance().getGameSpecificMessage().charAt(1)==('R')) {
					intake.setIntakePiston(IntakePistonState.DOWN);
					intake.setIntakePiston(IntakePistonState.OUT);
					intake.setSpeed(-0.7);
					Timer.delay(0.5);
					arm.setArmAngle(ArmControlMode.SENSORED, 210);
					Timer.delay(2.0);
					driveTrain.setSpeed(-0.4, 0.4);
					Timer.delay(4.0);
					driveTrain.setSpeed(0, 0);
					arm.setArmPiston(ArmPistonState.RELEASE);
					arm.setArmPiston(ArmPistonState.SHOOT);
					Timer.delay(1.0);
					driveTrain.setSpeed(0.4, -0.4);
					Timer.delay(1.9);
					driveTrain.setSpeed(0, 0);
					arm.setArmAngle(ArmControlMode.MANUAL, 0);
				}else {
					Timer.delay(2.0);
					driveTrain.setSpeed(-0.4, 0.4);
					Timer.delay(1.9);
					driveTrain.setSpeed(0, 0);
					Timer.delay(5);
				}
			}
		}
	}

	public void teleopInit() {
		//autonomousCommand.cancel();
		Scheduler.getInstance().removeAll();
		driveTrain.stopProfileDrive();
		driveTrain.zeroEncoders();
		Robot.driveTrain.setControlMode(DriveTrainControlMode.JOYSTICK, 0);
		arm.setControlMode(ArmControlMode.MANUAL);
		driveTrain.setPeriodMs(10);
		controlLoop.start();
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
		updateStatus();
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
		autonChooser.addObject("Straight Only", new StraightOnly());
		autonChooser.addDefault("Center Switch", new CenterSwitchAuton());
		autonChooser.addObject("Left Side Scale", new LeftSideScale());
		autonChooser.addObject("Right Side Scale", new RightSideScale());
		autonChooser.addObject("TWO CUBE Center Switch", new TwoCubeCenterAuton());
		autonChooser.addObject("Do Nothing", new CommandGroup());
		SmartDashboard.putData("Auton Setting", autonChooser);
	}
}
