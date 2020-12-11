package com.example.productmngmt.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.productmngmt.model.CustomSequence;

@Service
public class SequenceGenrationService {

	private MongoOperations mongoOperations;

	@Autowired
	public SequenceGenrationService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public long generateProductSequence(String seqName) {
		Query query = new Query();
		CustomSequence counter = mongoOperations.findAndModify(query.addCriteria(Criteria.where("_id").is(seqName)),
				new Update().inc("proSeq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				CustomSequence.class);
		return !Objects.isNull(counter) ? counter.getProSeq() : 1;
	}
	
	public long generateUserSequence(String seqName) {
		Query query = new Query();
		CustomSequence counter = mongoOperations.findAndModify(query.addCriteria(Criteria.where("_id").is(seqName)),
				new Update().inc("userSeq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				CustomSequence.class);
		return !Objects.isNull(counter) ? counter.getUserSeq() : 1;
	}
}
