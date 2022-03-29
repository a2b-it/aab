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
import ma.alakhdarbank.apps.ScheduledJob;

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
	ScheduledJob scheduledJob;

	
	@RequestMapping("/send/start")
	public ResponseEntity<String> startSendJob() {
		// some parameters
		scheduledJob.setSendJobEnabled(true);
		return new ResponseEntity("JOB Schedule Enabled", HttpStatus.OK);

	}
	
	@RequestMapping("/send/stop")
	public ResponseEntity<String> stopSendJob() {
		// some parameters
		scheduledJob.setSendJobEnabled(false);
		return new ResponseEntity("JOB Schedule Disabled", HttpStatus.OK);

	}
	
	@RequestMapping("/ctr/start")
	public ResponseEntity<String> startJob() {
		// some parameters
		scheduledJob.setCtrJobEnabled(true);
		return new ResponseEntity("JOB Schedule Enabled", HttpStatus.OK);

	}
	
	@RequestMapping("/ctr/stop")
	public ResponseEntity<String> stopJob() {
		// some parameters
		scheduledJob.setCtrJobEnabled(false);
		return new ResponseEntity("JOB Schedule Disabled", HttpStatus.OK);

	}
	
	@RequestMapping("/send/status")
	public ResponseEntity<String> sendStatus() {
		// some parameters
		//scheduledJob.setJobEnabled(false);
		return new ResponseEntity(scheduledJob.getSendStatus(), HttpStatus.OK);

	}
	
	@RequestMapping("/ctr/status")
	public ResponseEntity<String> ctrStatus() {
		// some parameters
		//scheduledJob.setJobEnabled(false);
		return new ResponseEntity(scheduledJob.getCtrStatus(), HttpStatus.OK);

	}
}
