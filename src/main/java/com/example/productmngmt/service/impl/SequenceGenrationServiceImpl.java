package com.example.productmngmt.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.productmngmt.model.CustomSequence;
import com.example.productmngmt.service.SequenceGenrationService;

@Service
public class SequenceGenrationServiceImpl implements SequenceGenrationService {

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Long generateProductSequence(String seqName) {
		Query query = new Query();
		CustomSequence counter = mongoOperations.findAndModify(query.addCriteria(Criteria.where("_id").is(seqName)),
				new Update().inc("proSeq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				CustomSequence.class);
		return !Objects.isNull(counter) ? counter.getProSeq() : 1;
	}

	@Override
	public Long generateUserSequence(String seqName) {
		Query query = new Query();
		CustomSequence counter = mongoOperations.findAndModify(query.addCriteria(Criteria.where("_id").is(seqName)),
				new Update().inc("userSeq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				CustomSequence.class);
		return !Objects.isNull(counter) ? counter.getUserSeq() : 1;
	}

	@Override
	public Long generateBlackListSequence(String seqName) {
		Query query = new Query();
		CustomSequence counter = mongoOperations.findAndModify(query.addCriteria(Criteria.where("_id").is(seqName)),
				new Update().inc("blackListTokenSeq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
				CustomSequence.class);
		return !Objects.isNull(counter) ? counter.getBlackListTokenSeq() : 1;
	}
}
