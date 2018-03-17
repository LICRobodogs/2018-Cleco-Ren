package org.frc.team2579.commands.auton;

import org.frc.team2579.commands.auton.paths.SwitchLeftSideLeftPath;
import org.frc.team2579.commands.auton.paths.SwitchLeftSideRightPath;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterSwitchAuton extends CommandGroup {
	public CenterSwitchAuton() {
		addSequential(new DriveMP(SwitchLeftSideLeftPath.Points,SwitchLeftSideLeftPath.kNumPoints,SwitchLeftSideRightPath.Points,SwitchLeftSideRightPath.kNumPoints));
	}
}
