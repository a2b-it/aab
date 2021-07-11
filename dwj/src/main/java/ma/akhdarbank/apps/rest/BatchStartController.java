package ma.akhdarbank.apps.rest;

import java.util.HashMap;
import java.util.Map;



import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
public class BatchStartController {
	@Autowired
	JobLauncher jobLauncher;
	
	Job processJob;
	
	@GetMapping("/job")
    public void startJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
	//some parameters
    Map<String, JobParameter> parameters = new HashMap<>();
    JobExecution jobExecution = (JobExecution) jobLauncher.run(processJob, new JobParameters(parameters));
    
	
	}    
	
	

}
