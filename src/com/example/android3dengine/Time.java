package com.example.android3dengine;

public class Time {
	public static final long NANOSECONDS = 1000000000L;
}


///**
// * Each Game object has a unique Timer object
// */
//public class Timer {
//
//	public static final long NANOSECONDS = 1000000000L; // Conversion from nanoseconds to seconds
//
//	public final double MAX_UPDATES_PER_SECOND; // Maximum frames to be rendered per second
//	public final double MIN_SECONDS_PER_UPDATE;
//
//	private long lastTime;
//	private long currentTime;
//	private long passedTime;
//	private double unprocessedTime;
//
//
//	public Timer(double maxFps) {
//		if(maxFps == 0) {
//			Log.e("ANDROID 3D ENGINE", "Maximum FPS is zero at Timer#Timer");
//			System.exit(1);
//		}
//
//		MAX_UPDATES_PER_SECOND = maxFps;
//		MIN_SECONDS_PER_UPDATE = 1.0 / maxFps;
//	}
//
//	public Timer(int maxFps) {
//		this((double) maxFps);
//	}
//
//	public long getLastTime() {
//		return lastTime;
//	}
//
//	public void setLastTime(long lastTime) {
//		this.lastTime = lastTime;
//	}
//
//	public long getCurrentTime() {
//		return currentTime;
//	}
//
//	public void setCurrentTime(long currentTime) {
//		this.currentTime = currentTime;
//	}
//
//	public long getPassedTime() {
//		return passedTime;
//	}
//
//	public void setPassedTime(long passedTime) {
//		this.passedTime = passedTime;
//	}
//
//	public double getUnprocessedTime() {
//		return unprocessedTime;
//	}
//
//	public void setUnprocessedTime(double unprocessedTime) {
//		this.unprocessedTime = unprocessedTime;
//	}
//}