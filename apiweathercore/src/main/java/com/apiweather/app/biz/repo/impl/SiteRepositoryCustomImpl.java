package com.apiweather.app.biz.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.apiweather.app.biz.model.Site;
import com.apiweather.app.biz.repo.SiteRepositoryCustom;

public class SiteRepositoryCustomImpl implements SiteRepositoryCustom {

	@Autowired
    private MongoTemplate mongoTemplate;
	

	public Long getMaxId() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        Site maxObject = mongoTemplate.findOne(query, Site.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getSiteId();
    }


	
 
    /*@Override
    public long updateEmployee(String empNo, String fullName, Date hireDate) {
        Query query = new Query(Criteria.where("empNo").is(empNo));
        Update update = new Update();
        update.set("fullName", fullName);
        update.set("hireDate", hireDate);
 
        UpdateResult result = this.mongoTemplate.updateFirst(query, update, Employee.class);
 
        if (result != null) {
            return result.getModifiedCount();
        }
 
        return 0;
    }*/
}
