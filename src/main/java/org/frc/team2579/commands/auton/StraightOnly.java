package org.frc.team2579.commands.auton;

import org.frc.team2579.utility.FollowProfile;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StraightOnly extends CommandGroup {
	public StraightOnly() {
		addSequential(new FollowProfile("StraightOnly"));
		//addSequential(new JoystickDrive());
	}
}
