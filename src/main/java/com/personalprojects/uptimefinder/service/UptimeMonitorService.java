package com.personalprojects.uptimefinder.service;

import com.personalprojects.uptimefinder.entity.Service;
import com.personalprojects.uptimefinder.utils.UptimeStatus;
import com.personalprojects.uptimefinder.utils.GeneralHelper;
import com.personalprojects.uptimefinder.entity.UptimeMonitor;
import com.personalprojects.uptimefinder.model.ServiceDto;
import com.personalprojects.uptimefinder.model.UptimeMonitorDto;
import com.personalprojects.uptimefinder.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class UptimeMonitorService {

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private ScheduledExecutorService scheduledExecutor;

	private Map<String, ScheduledFuture<?>> scheduledJobMap;

	/**
	 * Register a service created by user for Monitoring a Website is up/down.
	 * 
	 * @param serviceDto
	 * @return ServiceDto
	 */
	public ServiceDto registerService(ServiceDto serviceDto) {
		String url = GeneralHelper.trimHttpFromUrl(serviceDto.getWebsiteUrl());
		serviceDto.setWebsiteUrl(url);
		serviceDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		serviceDto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		Service service = serviceRepository.saveService(new Service(serviceDto));
		submitJobToScheduler(service.getFrequency(), service.getId(), service.getWebsite_url());
		return service.toDto();
	}

	/**
	 * To disable any service, disabled service will be stopped to do Monitoring
	 * 
	 * @param serviceDto
	 */
	public void disableService(ServiceDto serviceDto) {
		serviceRepository.updateService(false, serviceDto.getId());
		cancelScheduledJob(GeneralHelper.trimHttpFromUrl(serviceDto.getWebsiteUrl()));
	}

	/**
	 * To enable or activate any disabled service, enable service will be resumed to do
	 * Monitoring
	 * 
	 * @param serviceDto
	 */
	public void enableService(ServiceDto serviceDto) {
		serviceRepository.updateService(true, serviceDto.getId());
		Service service = serviceRepository.getService(serviceDto.getId());
		submitJobToScheduler(service.getFrequency(), service.getId(), service.getWebsite_url());
	}

	/**
	 * Return all created services along with all the details
	 * 
	 * @return List of all created services
	 */
	public List<ServiceDto> getServices(HashMap<String, String> keysToFilter, int page, int pageSize) {
		List<Service> allServices = serviceRepository.getFilteredServices(keysToFilter, page, pageSize);
		List<ServiceDto> serviceDtosList = allServices.stream().map(obj -> obj.toDto()).collect(Collectors.toList());
		return serviceDtosList;
	}


	/**
	 * Fetch the active services from database and submits them for scheduling to
	 * monitor websites
	 */
	private void prepareWebsiteMonitorJobs() {
		List<Service> activeServices = serviceRepository.getActiveServices();
		activeServices.stream().forEach(service -> {
			submitJobToScheduler(service.getFrequency(), service.getId(), service.getWebsite_url());
		});
	}

	/**
	 * Logic has been implemented to take decision of rounding frequency in
	 * minutes/hours as passed by the user
	 * 
	 * @param frequency
	 * @param serviceId
	 * @param websiteUrl
	 */
	private void submitJobToScheduler(String frequency, int serviceId, String websiteUrl) {
		long period = GeneralHelper.getTTLSeconds(frequency);
		ScheduledFuture<?> scheduledJob = executeJobScheduler(serviceId, websiteUrl, period);
		scheduledJobMap.put(websiteUrl, scheduledJob);
	}

	/**
	 * Method pings the particular Url record its response time and up/down status
	 * 
	 * @param monitor
	 */
	private void pingURL(UptimeMonitor monitor) {
		String url = monitor.getWebsite_url();
		try (Socket socket = new Socket()) {
			long start = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(url, 80), 3000);
			long end = System.currentTimeMillis();
			monitor.setResponseTime(end - start);
			monitor.setUptime(GeneralHelper.getFormattedDate(end));
			monitor.setStatus(UptimeStatus.UP.getStatus());
		} catch (IOException e) {
			monitor.setStatus(UptimeStatus.DOWN.getStatus());
			monitor.setDowntime(GeneralHelper.getFormattedDate(System.currentTimeMillis()));
			System.out.println(url + " is Not Reachable");
			e.printStackTrace();
		}
	}

	/**
	 * Method starts the process of creating and scheduling jobs for monitoring
	 * websites soon after the application is up
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		scheduledJobMap = new HashMap<String, ScheduledFuture<?>>();
		this.prepareWebsiteMonitorJobs();
	}

	/**
	 * Returns the website status along with details like - UP/Down status,
	 * uptime/downtime, URL and Average response time
	 * 
	 * @param websiteUrl
	 * @return dto which contains status of Website
	 */
	public UptimeMonitorDto getServiceStatus(String websiteUrl) {
		UptimeMonitor websiteStatus = serviceRepository.getServiceStatus(websiteUrl, 1, 1).get(0);
		String website_url = websiteStatus.getWebsite_url();
		String status = websiteStatus.getStatus();
		Timestamp uptime = websiteStatus.getUptime();
		Timestamp downtime = websiteStatus.getDowntime();
		double responseTime = websiteStatus.getResponseTime();
		UptimeMonitorDto uptimeMonitorDto = new UptimeMonitorDto(website_url, status, uptime, downtime, responseTime);
		return uptimeMonitorDto;
	}

	public List<UptimeMonitorDto> getServiceStatusLogs(String websiteUrl, int page, int pageSize) {
		List<UptimeMonitor> serviceLogs = serviceRepository.getServiceStatus(websiteUrl, page, pageSize);
		List<UptimeMonitorDto> uptimeMonitorDtos = new ArrayList<>();
		serviceLogs.forEach(serviceLog -> {
			uptimeMonitorDtos.add(new UptimeMonitorDto(serviceLog.getWebsite_url(), serviceLog.getStatus(), serviceLog.getUptime(), serviceLog.getDowntime(), serviceLog.getResponseTime()));
		});
		return uptimeMonitorDtos;
	}

	/**
	 * Schedule a Monitoring Job at fixed rate to record the ping/response status of
	 * a website
	 * 
	 * @param serviceId
	 * @param websiteUrl
	 * @param period     in hours/minutes
	 * @return scheduled future job
	 */
	private ScheduledFuture<?> executeJobScheduler(int serviceId, String websiteUrl, long period) {
		ScheduledFuture<?> scheduledJob = scheduledExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				UptimeMonitor monitor = new UptimeMonitor();
				monitor.setServiceId(serviceId);
				monitor.setWebsite_url(websiteUrl);
				pingURL(monitor);
				serviceRepository.saveWebsiteMonitor(monitor);
			}
		}, 0, period, TimeUnit.SECONDS);

		return scheduledJob;
	}

	/**
	 * Cancel the Monitoring Job of website if Service is been set disable/deactivate
	 * 
	 * @param websiteUrl
	 */
	private void cancelScheduledJob(String websiteUrl) {
		ScheduledFuture<?> scheduledFuture = scheduledJobMap.get(websiteUrl);
		boolean isCancelled = scheduledFuture.cancel(true);
		if (isCancelled) {
			scheduledJobMap.remove(websiteUrl);
		}
	}

}
