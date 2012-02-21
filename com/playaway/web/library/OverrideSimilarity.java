package com.playaway.web.library;

import org.apache.lucene.search.DefaultSimilarity;

@SuppressWarnings("serial")
public class OverrideSimilarity extends DefaultSimilarity{
	@Override
	public float lengthNorm(String field, int numTokens){
		return 1.0F;
	}
}
 