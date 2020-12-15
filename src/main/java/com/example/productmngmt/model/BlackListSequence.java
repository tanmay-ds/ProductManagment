package com.example.productmngmt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blacklistseq")
public class BlackListSequence {

	@Id
	private String id;
	private Long blackListTokenSeq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getBlackListTokenSeq() {
		return blackListTokenSeq;
	}

	public void setBlackListTokenSeq(Long blackListTokenSeq) {
		this.blackListTokenSeq = blackListTokenSeq;
	}
}
