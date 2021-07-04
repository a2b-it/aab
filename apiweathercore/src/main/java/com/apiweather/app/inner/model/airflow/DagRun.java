package com.apiweather.app.inner.model.airflow;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DagRun {
	
	public String dag_run_id;
    public String dag_id;
    public Date execution_date;
    public Date start_date;
    public Date end_date;
    public String state;
    public boolean external_trigger;
    public Conf conf;
    
    public class Conf{
    }
}
