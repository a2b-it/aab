package com.apiweather.app.jobs.rest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@RestController
@AllArgsConstructor
@Slf4j
public class JobsController {
	@Autowired
	JobLauncher jobLauncher;

	Job dssFileJob;

	@GetMapping("/runjob")
	public ResponseEntity<String> startJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		// some parameters
		JobParametersBuilder jobBuilder = new JobParametersBuilder();
		jobBuilder.addString("dss.param.station", "Ennzala");
		jobBuilder.addString("dss.file.path", "F:/Workspaces/apigeo/apibatchs/MyDSS_file.dss");
		jobBuilder.addString("dss.log.path", "F:/Workspaces/apigeo/apibatchs/logs/MyDSS_file.log");
		JobExecution jobExecution = (JobExecution) jobLauncher.run(dssFileJob, jobBuilder.toJobParameters());
		return new ResponseEntity(jobExecution.getExitStatus(), HttpStatus.OK);

	}

}
