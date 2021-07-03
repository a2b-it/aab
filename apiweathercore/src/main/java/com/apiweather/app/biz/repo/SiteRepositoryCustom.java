package com.apiweather.app.biz.repo;

import java.util.List;

import com.apiweather.app.biz.model.Site;

public interface SiteRepositoryCustom {
	
	public Long getMaxId ();
	
	
	public List<Site> findAllLikeThis(Site site);
}
