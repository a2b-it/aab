package com.apiweather.app.inner.model.airflow;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDag {
	
	public List<Dag> dags;
    public int total_entries;
}
