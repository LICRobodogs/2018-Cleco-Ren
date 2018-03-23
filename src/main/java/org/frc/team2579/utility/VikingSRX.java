package org.frc.team2579.utility;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class VikingSRX extends WPI_TalonSRX {

	public Profile profile;
	private int pointsSent;

	public VikingSRX(int port) {
		super(port);
		reset();
		pointsSent = 0;
	}

	public void zeroEncoder() {
		setSelectedSensorPosition(0, 0, 10);
	}

	public boolean sendNextPoint() {
		System.out.println("sendN");
		if (profile == null) {
			System.err.println("Attempted to load null profile to talon.");
			return false;
		}
		if (pointsSent >= profile.size()) {
			System.err.println("Attempted to send more points than were in the profile.");
			return false;
		}

		if (pushMotionProfileTrajectory(profile.getPoint(pointsSent)) == ErrorCode.OK) {
			pointsSent++;
			return true;
		}
		return false;
	}

	public MotionProfileStatus getStatus() {
		MotionProfileStatus status = new MotionProfileStatus();
		getMotionProfileStatus(status);
		return status;
	}

	public void followInit() {
		set(ControlMode.PercentOutput, 0);
		pointsSent = 0;
		zeroEncoder();
		clearMotionProfileTrajectories();
	}

	public void reset() {
		set(ControlMode.PercentOutput, 0);
		clearMotionProfileTrajectories();
		zeroEncoder();
		pointsSent = 0;
		profile = null;
	}

	public int getEncoderPosition() {
		return getSelectedSensorPosition(0);
	}

	public int getEncoderVelocity() {
		return getSelectedSensorVelocity(0);
	}
}
