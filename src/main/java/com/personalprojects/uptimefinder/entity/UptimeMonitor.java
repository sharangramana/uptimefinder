package com.personalprojects.uptimefinder.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "UptimeMonitorTable")
public class UptimeMonitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String website_url;
	private int serviceId;
	private String status;
	private Timestamp uptime;
	private Timestamp downtime;
	private double responseTime;

	public UptimeMonitor() {
	}

	public UptimeMonitor(String website_url, int serviceId, String status, Timestamp uptime, Timestamp downtime,
						 double responseTime) {
		this.website_url = website_url;
		this.serviceId = serviceId;
		this.status = status;
		this.uptime = uptime;
		this.downtime = downtime;
		this.responseTime = responseTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWebsite_url() {
		return website_url;
	}

	public void setWebsite_url(String website_url) {
		this.website_url = website_url;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
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

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

}
