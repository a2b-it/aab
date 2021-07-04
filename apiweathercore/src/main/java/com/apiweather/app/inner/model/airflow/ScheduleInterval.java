package com.apiweather.app.inner.model.airflow;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleInterval {
	public String __type;
    public int days;
    public int seconds;
    public int microseconds;
}
