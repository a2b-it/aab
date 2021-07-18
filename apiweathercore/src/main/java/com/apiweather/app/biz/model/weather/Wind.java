package com.apiweather.app.biz.model.weather;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public class Wind {
	private double speed;
    private int deg;
    private double gust;
}
