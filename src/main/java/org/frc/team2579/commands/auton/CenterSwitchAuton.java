package org.frc.team2579.commands.auton;

import org.frc.team2579.commands.IntakeSpeed;
import org.frc.team2579.commands.IntakeSpeedOff;
import org.frc.team2579.utility.FollowProfile;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CenterSwitchAuton extends CommandGroup {
	public CenterSwitchAuton() {
		addSequential(new FollowProfile(DriverStation.getInstance().getGameSpecificMessage().charAt(0)==('L')?"SwitchL":"SwitchR"));
		//addSequential(new DriveMP(SwitchLeftSideLeftPath.Points, SwitchLeftSideLeftPath.kNumPoints, SwitchLeftSideRightPath.Points, SwitchLeftSideRightPath.kNumPoints));
		addSequential(new IntakeSpeed(0.7));
		addSequential(new WaitCommand(3));
		addSequential(new IntakeSpeedOff());
	}
}
