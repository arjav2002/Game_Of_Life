package com.arjav.gameoflife.client;

public class FrameTimer {

	long last;
	
	public FrameTimer(long last) {
		this.last = last;
	}
	
	public long mark() {
		long tmp = last;
		last = System.nanoTime();
		return last - tmp;
	}
	
}
