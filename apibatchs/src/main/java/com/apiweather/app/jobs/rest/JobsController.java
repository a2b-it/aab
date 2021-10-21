package com.apiweather.app.jobs.rest;


import java.io.File;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiweather.app.flow.FtpGetGateway;
import com.apiweather.app.flow.FtpLsGateway;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@RestController
@Slf4j
public class JobsController {
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("dssFileJob")
	Job dssFileJob;
	
	@Autowired
	@Qualifier("dssReadFileJob")
	Job dssReadFileDataStep;
	
	@Autowired
	FtpLsGateway toFtpFlowGateway;
	
	@Autowired
	FtpGetGateway ftpGetGateway;

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
	
	@GetMapping("/ftpLs")
	public ResponseEntity<String> startLsFtp() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {		
		List<String> list = toFtpFlowGateway.list("/");
		return new ResponseEntity(list, HttpStatus.OK);
	}
	
	@GetMapping("/ftpGet")
	public ResponseEntity<String> startGetFtp() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {		
		File file = ftpGetGateway.retreive("README.txt");
		return new ResponseEntity(file.getAbsolutePath(), HttpStatus.OK);
	}
	
	@PostMapping("/dssRead")
	public ResponseEntity<String> startDssRead() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {		
		JobParametersBuilder jobBuilder = new JobParametersBuilder();
		//jobBuilder.addString("dss.param.station", "Ennzala");
		jobBuilder.addString("dss.file.path", "F:\\Workspaces\\apigeo\\apibatchs\\MyDSS_file.dss");
		jobBuilder.addString("dss.log.path", "F:/Workspaces/apigeo/apibatchs/logs/MyDSS_file.log");
		JobExecution jobExecution = (JobExecution) jobLauncher.run(dssReadFileDataStep, jobBuilder.toJobParameters());
		return new ResponseEntity(jobExecution.getExitStatus(), HttpStatus.OK);
	}
	
	
}
