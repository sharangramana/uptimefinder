package com.personalprojects.uptimefinder.model;

import java.sql.Timestamp;

public class ServiceDto {

	private String id;
	private String name;
	private String websiteUrl;
	private String frequency;

	private boolean enabled;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public ServiceDto() {
	}

	public ServiceDto(String id, String name, String websiteUrl, String frequency) {
		this.id = id;
		this.name = name;
		this.websiteUrl = websiteUrl;
		this.frequency = frequency;
	}

	public ServiceDto(String id, String name, String websiteUrl, boolean enabled, String frequency, Timestamp createdAt, Timestamp updatedAt) {
		this.id = id;
		this.name = name;
		this.websiteUrl = websiteUrl;
		this.enabled = enabled;
		this.frequency = frequency;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "ServiceDto [id=" + id + ", name=" + name + ", websiteUrl=" + websiteUrl + ", enabledStatus=" + enabled +", frequency=" + frequency
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
