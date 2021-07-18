/**
 * 
 */
package com.apiweather.app.biz.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.apiweather.app.biz.model.DSSBlock;

/**
 * @author a.bouabidi
 *
 */

@Repository
public class DssFileRepository {
	
	 @Autowired
	 private MongoTemplate mongoTemplate;
	 
	 public List<DSSBlock> readDssFileData (){
		 
		 Query query = new Query();
		 query.addCriteria(Criteria.where("name").is("Eric"));
		// List<User> users = mongoTemplate.find(query, User.class);
		 
		 
		 return null;
	 }

}
