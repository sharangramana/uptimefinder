package com.personalprojects.uptimefinder.utils;

public enum UptimeStatus {

	UP("UP"), DOWN("DOWN");

	private String statusValue;

	UptimeStatus(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getStatus() {
		return this.statusValue;
	}
}
