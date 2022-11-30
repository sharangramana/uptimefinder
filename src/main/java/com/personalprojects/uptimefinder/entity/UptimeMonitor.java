package com.personalprojects.uptimefinder.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "UptimeMonitorTable")
public class UptimeMonitor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "website_url")
	private String website_url;

	@Column(name = "serviceId")
	private String serviceId;

	@Column(name = "status")
	private String status;

	@Column(name = "uptime")
	private Timestamp uptime;

	@Column(name = "downtime")
	private Timestamp downtime;

	@Column(name = "responseTime")
	private double responseTime;

	public UptimeMonitor() {
	}

	public UptimeMonitor(String website_url, String serviceId, String status, Timestamp uptime, Timestamp downtime,
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
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
