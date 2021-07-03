package com.apiweather.app.biz.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Alert;
import com.apiweather.app.biz.repo.AlertRepositoryCustom;

@Qualifier("alertRepositoryCustom")
public class AlertRepositoryImpl implements AlertRepositoryCustom {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoRepository<Alert, Long> repo;

	public Long getMaxId() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        Alert maxObject = mongoTemplate.findOne(query, Alert.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getIdalert();
    }

	
	public List<Alert> findAllLikeThis(Alert alert) {
		Example<Alert> aExl = Example.of(alert);
		List<Alert> rs = repo.findAll(aExl);		 
		return rs;
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
