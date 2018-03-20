package org.frc.team2579.utility;

import org.frc.team2579.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class FollowProfile extends Command {

	private Profile[] profiles;

	public FollowProfile(String profileName) {
		requires(Robot.driveTrain);
		ProfileLoader profileLoader = new ProfileLoader();
		profiles = profileLoader.loadProfile(profileName);
	}

	public void initialize() {
		Robot.driveTrain.setProfiles(profiles);
		Robot.driveTrain.startProfileDrive();
	}

	// We don't need an execute method because all the profile following happens in
	// Notifiers, on a different thread
	
	public void execute() {
		Robot.driveTrain.executeDrive();
	}

	@Override
	public boolean isFinished() {
		return Robot.driveTrain.isFinished();
	}

	public void end() {
		Robot.driveTrain.stopProfileDrive();
	}

	public void interrupted() {
		end();
	}
}
