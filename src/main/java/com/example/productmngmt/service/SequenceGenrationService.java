package com.example.productmngmt.service;


public interface SequenceGenrationService {
	
	Long generateProductSequence(String seqName);
	
	Long generateUserSequence(String seqName);
	
	Long generateBlackListSequence(String seqName);
}
