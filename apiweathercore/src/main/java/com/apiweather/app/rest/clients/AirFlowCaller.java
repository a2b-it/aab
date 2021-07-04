package com.apiweather.app.rest.clients;

import com.apiweather.app.inner.model.airflow.Dag;
import com.apiweather.app.inner.model.airflow.ListDag;

public interface AirFlowCaller {
	
	public ListDag ListDAGs ();
	
	public Dag GetBasicInformation ();
	
	public Dag getJobStatus ();
}
