/**
 * 
 */
package ma.alakhdarbank.apps;

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

/**
 * @author a.bouabidi
 *
 */
@Component
public class ScheduledJob {
	@Setter	
	@Getter
	private boolean isJobEnabled = false;
	
	
	@Autowired
	@Qualifier("sendingJob")
	Job sendingJob;

	@Autowired
	@Qualifier("ctrJob")
	Job ctrJob;

	@Autowired
	private JobLauncher jobLauncher;
	
	private final int s = 1000;		
	
	private final int m = 60 * s ;
	
	private final int h = 60 * m;
	
	

	@Scheduled(fixedDelay = 1 * m)
	public void runCtrJob() throws Exception {
		if (isJobEnabled) {
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			// JobParameters jobParameters = new JobParametersBuilder().addString("time",
			// LocalDateTime.now().toString()).toJobParameters();
			jobBuilder.addString("time", LocalDateTime.now().toString());
			// jobBuilder.addString("login", "login");
			// jobBuilder.addString("password", "password");
			jobBuilder.addString("pub.cert.path", "F:\\WORK\\CCBV2\\publique.bin");
			JobExecution execution = jobLauncher.run(ctrJob, jobBuilder.toJobParameters());			
			System.out.println("Job Getting Ctr Data Exit Status :: " + execution.getExitStatus());
		}
	}

	@Scheduled(fixedDelay = 1 * m)
	public void runSending() throws Exception {
		if (isJobEnabled) {
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			// JobParameters jobParameters = new JobParametersBuilder().addString("time",
			// LocalDateTime.now().toString()).toJobParameters();
			jobBuilder.addString("json.file.path", "F:\\Workspaces\\apigeo\\ccb\\json\\");
			jobBuilder.addString("json.file.name", "cpts_output.json");
			jobBuilder.addString("time", LocalDateTime.now().toString());
			// jobBuilder.addString("login", "login");
			// jobBuilder.addString("password", "password");
			jobBuilder.addString("pub.cert.path", "F:\\WORK\\CCBV2\\publique.bin");
			JobExecution execution = jobLauncher.run(sendingJob, jobBuilder.toJobParameters());
			System.out.println("Job Sendind Data Exit Status :: " + execution.getExitStatus());
		}
	}
	
	public String getStatus () {
		//JobExplorer explorer =
		return (isJobEnabled)?"Enabled":"Disabled";
	}
}
