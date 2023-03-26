package com.axelar.fastsign;

import com.axelar.fastsign.data.DataRecord;
import com.axelar.fastsign.service.RecordService;
import com.axelar.fastsign.service.SigningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class FastsignApplication {

	static final int batchSize = 10;
	static final int threads = 25;
	private static final Logger logger = LoggerFactory.getLogger(FastsignApplication.class);
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(FastsignApplication.class, args);
		SigningService signingService = applicationContext.getBean(SigningService.class);
		RecordService recordService = applicationContext.getBean(RecordService.class);

		signingService.loadKeys();
		recordService.setBatchSize(batchSize);
		int totalBatch = recordService.getTotalBatch();

		ExecutorService executor = Executors.newFixedThreadPool(threads);

		for(int i=1;i<=totalBatch;i++) {
			executor.execute(new RecordRunnable(i, recordService, signingService));
		}

		executor.shutdown();
	}

	public static class RecordRunnable implements Runnable {
		int batchId;
		RecordService recordService;
		SigningService signingService;
		public RecordRunnable(int batchId, RecordService recordService, SigningService signingService) {
			this.batchId = batchId;
			this.recordService = recordService;
			this.signingService = signingService;
		}
		@Override
		public void run() {
			logger.info("Working on batch {}", batchId);
			List<DataRecord> records = recordService.readBatch(batchId);
			signingService.signRecords(records);
			recordService.writeBatch(records);
		}
	}
	@Autowired Environment env;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("driverClassName"));
		dataSource.setUrl(env.getProperty("url"));
		dataSource.setUsername(env.getProperty("uname"));
		dataSource.setPassword(env.getProperty("password"));
		return dataSource;
	}
}
