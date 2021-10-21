/**
 * 
 */
package ma.alakhdarbank.apps.rest;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 *
 */
@RestController
@Getter
@Setter
public class JobsController {
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("processJob")
	Job ccbFileJob;

	
	@RequestMapping("/invokejob")
	public ResponseEntity<String> startJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		// some parameters
		JobParametersBuilder jobBuilder = new JobParametersBuilder();
		jobBuilder.addString("json.file.path", "F:\\Workspaces\\ccb\\json");
		jobBuilder.addString("json.file.name", "cpts_output.json");		
		JobExecution jobExecution = (JobExecution) jobLauncher.run(ccbFileJob, jobBuilder.toJobParameters());
		return new ResponseEntity(jobExecution.getExitStatus(), HttpStatus.OK);

	}
}
