/**
 * 
 */
package ma.akhdarbank.apps;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author a.bouabidi
 *
 */
@Component
@Slf4j
public class ScheduledJob {
	@Setter	
	@Getter
	private boolean isPrepareJobEnabled = true;
	
	@Setter	
	@Getter
	private boolean isGetDataJobEnabled = true;
	
	
	@Autowired
	@Qualifier("prepareDataJob")
	Job prepareDataJob;

	@Autowired
	@Qualifier("getDataJob")
	Job getDataJob;

	@Autowired
	private JobLauncher jobLauncher;
	
	
	
	private final int s = 1000;		
	
	private final int m = 60 * s ;
	
	private final int h = 60 * m;

	

	@Scheduled(cron = "${aab.param.sched.get}")
	public void runGetDataJob() throws Exception {
		
		if (isGetDataJobEnabled) {
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			// JobParameters jobParameters = new JobParametersBuilder().addString("time",
			// LocalDateTime.now().toString()).toJobParameters();
			jobBuilder.addString("time", LocalDateTime.now().toString());
			// jobBuilder.addString("login", "login");
			// jobBuilder.addString("password", "password");			
			JobExecution execution = jobLauncher.run(getDataJob, jobBuilder.toJobParameters());
			// schedule run of other job
			
			log.info("Job Getting Ctr Data Exit Status :: " + execution.getExitStatus());
		}
	}

	
	@Scheduled(cron = "${aab.param.sched.prep}")
	public void runPrepareDataJob() throws Exception {
		if (isPrepareJobEnabled) {
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			// JobParameters jobParameters = new JobParametersBuilder().addString("time",
			// LocalDateTime.now().toString()).toJobParameters();
			jobBuilder.addString("time", LocalDateTime.now().toString());
			// jobBuilder.addString("login", "login");
			// jobBuilder.addString("password", "password");
			
			JobExecution execution = jobLauncher.run(prepareDataJob, jobBuilder.toJobParameters());
			log.info("Job Sendind Data Exit Status :: " + execution.getExitStatus());
		}
	}
	
	public String getPrepareJobStatus () {
		//JobExplorer explorer =
		return (isPrepareJobEnabled)?"Enabled":"Disabled";
	}
	
	public String getCtrStatus () {
		//JobExplorer explorer =
		return (isGetDataJobEnabled)?"Enabled":"Disabled";
	}
}
