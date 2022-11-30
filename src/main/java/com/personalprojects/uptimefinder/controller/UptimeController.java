package com.personalprojects.uptimefinder.controller;

import com.personalprojects.uptimefinder.model.ServiceDto;
import com.personalprojects.uptimefinder.model.UptimeMonitorDto;
import com.personalprojects.uptimefinder.service.UptimeMonitorService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/service")
public class UptimeController {

	@Autowired
	private UptimeMonitorService uptimeMonitorService;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	@RequestMapping(value = "/register", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<ServiceDto> registerService(@RequestBody ServiceDto serviceDto) {
		ServiceDto registeredServiceDto = uptimeMonitorService.registerService(serviceDto);
		return new ResponseEntity(registeredServiceDto, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/disable", consumes = "application/json", method = RequestMethod.PUT)
	public ResponseEntity<String> disableService(@RequestBody ServiceDto serviceDto) {
		uptimeMonitorService.disableService(serviceDto);
		return new ResponseEntity("Service Disabled Succesfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/enable", consumes = "application/json", method = RequestMethod.PUT)
	public ResponseEntity<String> enableService(@RequestBody ServiceDto serviceDto) {
		uptimeMonitorService.enableService(serviceDto);
		return new ResponseEntity("Service enabled Succesfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ResponseEntity<UptimeMonitorDto> getServiceStatus(@RequestParam(required = true) String websiteUrl) {
		UptimeMonitorDto websiteStatus = uptimeMonitorService.getServiceStatus(websiteUrl);
		return new ResponseEntity(websiteStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/status/logs", method = RequestMethod.GET)
	public ResponseEntity<List<UptimeMonitorDto>> getServiceStatusLogs(@RequestParam(required = true) String websiteUrl,
																	   @RequestParam(required = true) int page,
																	   @RequestParam(required = true) int pageSize) {
		List<UptimeMonitorDto> serviceStatusLogs = uptimeMonitorService.getServiceStatusLogs(websiteUrl, page, pageSize);
		return new ResponseEntity(serviceStatusLogs, HttpStatus.OK);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ResponseEntity<List<ServiceDto>> getServices(@RequestParam(required = false) String name,
														@RequestParam(required = false) String frequency,
														@RequestParam(required = false) String websiteUrl,
														@RequestParam(required = false) String enabledStatus,
														@RequestParam(required = true) int page,
														@RequestParam(required = true) int pageSize) {

		HashMap<String, String> keysToFilter = new HashMap<>();
		if (name != null && !name.isEmpty()) keysToFilter.put("name", name.trim());
		if (frequency != null && !frequency.isEmpty()) keysToFilter.put("frequency", frequency.trim());
		if (websiteUrl != null && !websiteUrl.isEmpty()) keysToFilter.put("website_url", websiteUrl.trim());
		if (enabledStatus != null && !enabledStatus.isEmpty()) keysToFilter.put("enabled", enabledStatus.trim());

		List<ServiceDto> serviceDtoList = uptimeMonitorService.getServices(keysToFilter, page, pageSize);
		return new ResponseEntity(serviceDtoList, HttpStatus.OK);
	}

	@PostMapping("/register/batch")
	public ResponseEntity<Object> executeBatchJob(@RequestPart("file") MultipartFile multipartFile) throws IOException {

		String fileLocation = new File("src/main/resources/static").getAbsolutePath() + "/" + "services.csv";
		System.out.println(fileLocation);
		FileOutputStream output = new FileOutputStream(fileLocation);
		output.write(multipartFile.getBytes());
		output.close();

//		File file = new File("/Users/sharangramana/Documents/projects/uptimefinder/src/main/resources");
//		multipartFile.transferTo(file);

		JobParameters jobParameters = new JobParametersBuilder()
				.toJobParameters();
		CompletableFuture.runAsync(() -> {
			try {
				jobLauncher.run(job ,jobParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		String response = "Dear user, your data is being processed, a mail will be triggered with the logs once the process is completed!";
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}
}
