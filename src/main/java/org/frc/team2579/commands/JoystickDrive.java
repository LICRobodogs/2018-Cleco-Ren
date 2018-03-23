package org.frc.team2579.commands;

import org.frc.team2579.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class JoystickDrive extends Command {

	@Override
    protected boolean isFinished() {
        return false;
    }

    public JoystickDrive(){
        requires(Robot.driveTrain);
    }

    public void initialize(){
        
    }

    public void execute() {
        double move = Robot.oi.getMoveInput();
        double steer = Robot.oi.getSteerInput();

        Robot.driveTrain.drive(move, steer);
    }

}
