package com.personalprojects.uptimefinder.batch;

import com.personalprojects.uptimefinder.model.ServiceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Timestamp;

public class ServiceProcessor implements ItemProcessor<ServiceDto, ServiceDto> {
    private static final Logger log = LoggerFactory.getLogger(ServiceProcessor.class);

    @Override
    public ServiceDto process(final ServiceDto serviceDto) throws Exception {

        final String name = serviceDto.getName();
        final String websiteUrl = serviceDto.getWebsiteUrl();
        final String frequency = serviceDto.getFrequency();
        final boolean enabled = true;
        final Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

        final ServiceDto transformedServiceDTO = new ServiceDto(name, websiteUrl, frequency);
        transformedServiceDTO.setEnabled(true);
        serviceDto.setCreatedAt(currentTimeStamp);

        log.info("Converting (" + serviceDto + ") into (" + transformedServiceDTO + ")");

        log.info("Scheduling the jobs");


        return transformedServiceDTO;
    }
}
