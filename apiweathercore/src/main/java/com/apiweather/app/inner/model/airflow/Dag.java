package com.apiweather.app.inner.model.airflow;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Dag {
	public String dag_id;
    public String root_dag_id;
    public boolean is_paused;
    public boolean is_active;
    public boolean is_subdag;
    public String fileloc;
    public String file_token;
    public List<String> owners;
    public String description;
    public ScheduleInterval schedule_interval;
    public List<Tag> tags;
    
    
    public enum DagStatus {    	
    	success,
    	running,
    	failed,
    	upstream_failed,
    	skipped,
    	up_for_retry,
    	up_for_reschedule,
    	queued,
    	none,
    	scheduled,
    	removed
    }
    
	
}
