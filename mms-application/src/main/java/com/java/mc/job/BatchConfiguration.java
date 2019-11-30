package com.java.mc.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.java.mc.bean.BatchJob;
import com.java.mc.job.processor.BatchItemProcessor;
import com.java.mc.job.reader.BatchItemReader;
import com.java.mc.job.writer.BatchItemWriter;

@Configuration
public class BatchConfiguration{
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	@Value("${vg.batch.job.name}")
	private String jobName;
	
	@Value("${vg.batch.step.name}")
	private String stepName;
	
	@Value("${vg.batch.chunk.count}")
	private Short chunkCount;

	@Bean
	public ItemReader<BatchJob> reader() {
		return new BatchItemReader();
	}

	@Bean
	public ItemProcessor<BatchJob, BatchJob> processor() {
		return new BatchItemProcessor();
	}

	@Bean
	public ItemWriter<BatchJob> writer() {
		return new BatchItemWriter();
	}

	@Bean
	public Job mailJob(JobBuilderFactory jobs, Step step, JobExecutionListener listener) {
		return jobs.get(this.jobName).incrementer(new RunIdIncrementer()).listener(listener).flow(step).end().build();
	}

	@Bean
	public Step mailStep(StepBuilderFactory steps, ItemReader<BatchJob> reader, ItemWriter<BatchJob> writer,
			ItemProcessor<BatchJob, BatchJob> processor) {
		return steps.get(this.stepName).allowStartIfComplete(true).<BatchJob, BatchJob> chunk(this.chunkCount).reader(reader).processor(processor).writer(writer).build();
	}

}
