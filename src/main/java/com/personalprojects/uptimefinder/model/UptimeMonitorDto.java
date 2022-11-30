package com.personalprojects.uptimefinder.model;

import java.sql.Timestamp;

public class UptimeMonitorDto {

	private String website_url;
	private String status;
	private Timestamp uptime;
	private Timestamp downtime;

	private double responseTime;

	public UptimeMonitorDto() {
	}

	public UptimeMonitorDto(String website_url, String status, Timestamp uptime, Timestamp downtime, double responseTime) {
		this.website_url = website_url;
		this.status = status;
		this.uptime = uptime;
		this.downtime = downtime;
		this.responseTime = responseTime;
	}

	public String getWebsite_url() {
		return website_url;
	}

	public void setWebsite_url(String website_url) {
		this.website_url = website_url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUptime() {
		return uptime;
	}

	public void setUptime(Timestamp uptime) {
		this.uptime = uptime;
	}

	public Timestamp getDowntime() {
		return downtime;
	}

	public void setDowntime(Timestamp downtime) {
		this.downtime = downtime;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

}
