package com.personalprojects.uptimefinder.batch;

import com.personalprojects.uptimefinder.model.ServiceDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class CsvFileToDatabaseConfig {

    @Autowired
    public JobBuilderFactory jobBuilder;

    @Autowired
    public StepBuilderFactory stepBuilder;

    @Autowired
    public DataSource dataSource;

    // begin reader, writer, and processor


    @Bean
    public FlatFileItemReader<ServiceDto> csvServiceDetailsReader(){
        FlatFileItemReader<ServiceDto> reader = new FlatFileItemReader<ServiceDto>();
        reader.setResource(new ClassPathResource("animescsv.csv"));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "name", "websiteUrl", "frequency" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(ServiceDto.class);
            }});
        }});
        return reader;
    }


    @Bean
    ItemProcessor<ServiceDto, ServiceDto> csvServiceDetailsProcessor() {
        return new ServiceProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<ServiceDto> csvServiceDetailsWriter() {
        JdbcBatchItemWriter<ServiceDto> csvAnimeWriter = new JdbcBatchItemWriter<>();
        csvAnimeWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        csvAnimeWriter.setSql("INSERT INTO Service (name, website_url, frequency, enabled, createdAt, updatedAt) VALUES (:name, :website_url, :frequency, :enabled, :createdAt, :updatedAt)");
        csvAnimeWriter.setDataSource(dataSource);
        return csvAnimeWriter;
    }

    // end reader, writer, and processor

    // begin job info
    @Bean
    public Step csvFileToDatabaseStep() {
        return stepBuilder.get("csvFileToDatabaseStep")
                .<ServiceDto, ServiceDto>chunk(1)
                .reader(csvServiceDetailsReader())
                .processor(csvServiceDetailsProcessor())
                .writer(csvServiceDetailsWriter())
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .build();
    }

    @Bean
    Job csvFileToDatabaseJob(JobCompletionNotificationListener listener) {
        return jobBuilder.get("csvFileToDatabaseJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(csvFileToDatabaseStep())
                .end()
                .build();
    }
    // end job info
}
