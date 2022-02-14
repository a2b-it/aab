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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.akhdarbank.apps.ScheduledJob;

@RestController
@AllArgsConstructor
@Slf4j
public class BatchStartController {
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired	
	ScheduledJob scheduledJob;		
	
	@GetMapping("/prep")
    public void startPrepareJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
	//some parameters
    Map<String, JobParameter> parameters = new HashMap<>();
    try {
    	scheduledJob.setPrepareJobEnabled(true);
		scheduledJob.runPrepareDataJob();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
    
    @GetMapping("/get")
    public void startGetJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
	//some parameters
    Map<String, JobParameter> parameters = new HashMap<>();
    try {
    	scheduledJob.setGetDataJobEnabled(true);
		scheduledJob.runGetDataJob();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	}    
	
	

}
