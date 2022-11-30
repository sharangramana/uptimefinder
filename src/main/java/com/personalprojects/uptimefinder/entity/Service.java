package com.personalprojects.uptimefinder.entity;

import com.personalprojects.uptimefinder.model.ServiceDto;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Service")
public class Service implements Serializable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;
	@Column(name = "website_url")
	private String website_url;
	@Column(name = "frequency")
	private String frequency;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "createdAt")
	private Timestamp createdAt;

	@Column(name = "updatedAt")
	private Timestamp updatedAt;

	public Service() {
	}

	public Service(ServiceDto serviceDto) {
		this.setId(serviceDto.getId());
		this.setName(serviceDto.getName());
		this.setWebsite_url(serviceDto.getWebsiteUrl());
		this.setFrequency(serviceDto.getFrequency());
		this.setEnabled(true);
		this.setCreatedAt(serviceDto.getCreatedAt());
		this.setUpdatedAt(serviceDto.getUpdatedAt());
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

	public String getWebsite_url() {
		return website_url;
	}

	public void setWebsite_url(String website_url) {
		this.website_url = website_url;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ServiceDto toDto() {
		ServiceDto serviceDto = new ServiceDto(id, name, website_url, enabled, frequency, createdAt, updatedAt);
		return serviceDto;
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
}
