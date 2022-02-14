/**
 * 
 */
package ma.akhdarbank.apps;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import ma.akhdarbank.apps.dao.BatchRepository;

/**
 * @author a.bouabidi
 *
 */
@Component
public class ScheduledJob {
	@Setter	
	@Getter
	private boolean isPrepareJobEnabled = false;
	
	@Setter	
	@Getter
	private boolean isGetDataJobEnabled = false;
	
	
	@Autowired
	@Qualifier("prepareDataJob")
	Job prepareDataJob;

	@Autowired
	@Qualifier("getDataJob")
	Job getDataJob;

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private BatchRepository batchRepo;
	
	private final int s = 1000;		
	
	private final int m = 60 * s ;
	
	private final int h = 60 * m;
	
	

	//@Scheduled(fixedDelay = 12 * h)
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
			
			System.out.println("Job Getting Ctr Data Exit Status :: " + execution.getExitStatus());
		}
	}

	
	//@Scheduled(fixedDelay = 1 * m)
	public void runPrepareDataJob() throws Exception {
		if (isPrepareJobEnabled) {
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			// JobParameters jobParameters = new JobParametersBuilder().addString("time",
			// LocalDateTime.now().toString()).toJobParameters();
			jobBuilder.addString("time", LocalDateTime.now().toString());
			// jobBuilder.addString("login", "login");
			// jobBuilder.addString("password", "password");
			
			JobExecution execution = jobLauncher.run(prepareDataJob, jobBuilder.toJobParameters());
			System.out.println("Job Sendind Data Exit Status :: " + execution.getExitStatus());
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
