package com.personalprojects.uptimefinder.batch;

import com.personalprojects.uptimefinder.model.ServiceDto;
import com.personalprojects.uptimefinder.service.UptimeMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.UUID;

public class ServiceProcessor implements ItemProcessor<ServiceDto, ServiceDto> {

    @Autowired
    UptimeMonitorService uptimeMonitorService;

    private static final Logger log = LoggerFactory.getLogger(ServiceProcessor.class);

    @Override
    public ServiceDto process(final ServiceDto serviceDto) throws Exception {

        final String id = UUID.randomUUID().toString();
        final String name = serviceDto.getName();
        final String websiteUrl = serviceDto.getWebsiteUrl();
        final String frequency = serviceDto.getFrequency();
        final boolean enabled = true;
        final Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

        final ServiceDto transformedServiceDTO = new ServiceDto(id, name, websiteUrl, frequency);

        transformedServiceDTO.setEnabled(true);
        transformedServiceDTO.setCreatedAt(currentTimeStamp);
        transformedServiceDTO.setUpdatedAt(currentTimeStamp);

        log.info("Converting (" + serviceDto + ") into (" + transformedServiceDTO + ")");

        log.info("Scheduling the jobs");
        uptimeMonitorService.submitJobToScheduler(frequency, id, websiteUrl);

        return transformedServiceDTO;
    }
}
