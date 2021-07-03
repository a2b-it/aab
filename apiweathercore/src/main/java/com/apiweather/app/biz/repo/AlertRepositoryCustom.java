package com.apiweather.app.biz.repo;

import java.util.List;

import com.apiweather.app.biz.model.Alert;

public interface AlertRepositoryCustom {
	
	public Long getMaxId ();
	
	
	public List<Alert> findAllLikeThis(Alert alert);
}
