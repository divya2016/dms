package com.dms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
@Configuration
public class MongoConfig {
	@Autowired MongoDbFactory mongoDbFactory;
	@Autowired MongoConverter mongoConverter;
	@Autowired MongoClient mongoClient;
	@Bean
	public GridFsTemplate gridFsTemplate() {
		return new GridFsTemplate(mongoDbFactory, mongoConverter);
	}
	
	@Bean
	@PostConstruct
	public GridFSBucket gridFsBucket() {
		MongoDatabase myDatabase = mongoClient.getDatabase("dms");
		GridFSBucket gridFSBucket = GridFSBuckets.create(myDatabase);
		return gridFSBucket;
	}
	
}