package com.dms.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.dms.data.model.Counters;

@Service
public class CommonUtils {
	
	public static final int COLLECTION_COUNTER=1;
	
	@Autowired MongoTemplate mongoTemplate;
	
	public <T>Long getNextSequenceId(String collectionName,Class<T> entityClass){
		if (null == collectionName || collectionName.isEmpty()) {
			return null;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("name", collectionName);
		Counters counter = findOne(params, Counters.class);
		if (counter != null) {
			counter.setSeqId(counter.getSeqId()+1);
		}else{
			counter = new Counters();
			counter.setSeqId(COLLECTION_COUNTER);
			counter.setName(collectionName);
		}
		mongoTemplate.save(counter);
		return counter.getSeqId().longValue();
	}
	
	public <T> T findOne(Map<String, Object> params, Class<T> entityClass) {
		Query query = new Query();
		if (params == null || params.isEmpty()) {
			return null;
		} else {
			for (Entry<String, Object> entry : params.entrySet()) {
				query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
			}
		}
		return mongoTemplate.findOne(query, entityClass);
	}
	
	public <T> void save(T entityToSave) {
		if (entityToSave == null) {
			return;
		} else {
			mongoTemplate.save(entityToSave);
		}
	}
}
