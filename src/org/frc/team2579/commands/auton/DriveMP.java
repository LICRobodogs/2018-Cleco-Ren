package org.frc.team2579.commands.auton;

import org.frc.team2579.subsystems.DriveTrain;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.*;

import edu.wpi.first.wpilibj.command.Command;

public class DriveMP extends Command{
	
	private double[][] leftPoints, rightPoints;
	private int leftNumPoints, rightNumPoints;
	
	public DriveMP(double[][]leftPoints,int leftNumPoints,double[][]rightPoints,int rightNumPoints){
		this.leftPoints = leftPoints;
		this.leftNumPoints = leftNumPoints;
		this.rightPoints = rightPoints;
		this.rightNumPoints = rightNumPoints;
	}

	protected void initialize(){
		DriveTrain.resetPosition();
		DriveTrain.startFilling(leftPoints, leftNumPoints,'L');
		DriveTrain.startFilling(rightPoints, rightNumPoints,'R');
		DriveTrain.setSetValue(SetValueMotionProfile.Enable);
	}

	protected void execute() {
		DriveTrain.startMP();
    }
		
	protected void end() {
    	DriveTrain.resetMP();
    	DriveTrain.setSetValue(SetValueMotionProfile.Disable);
    }
	@Override
	protected boolean isFinished() {
		//if(DriveTrain._status.hasUnderrun)
		//	return true;
		System.out.println(DriveTrain.isFinished());
		return DriveTrain.isFinished();
	}
}
