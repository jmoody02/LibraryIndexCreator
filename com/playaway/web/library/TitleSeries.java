package com.playaway.web.library;

import org.hibernate.search.annotations.*;

import javax.persistence.*;

import java.io.Serializable;

/**
 * This class is represents a row in the list_title_series table Even though
 * this class is a plain old java object (POJO), its annotated such that
 * Hibernate and Hibernate Search know how to persist/create it and index it,
 * respectively.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 * 
 * @author Jonathan Moody jmoody@playaway.com
 */

@Entity
@Table(name="list_title_series")
// @Indexed
@Similarity(impl = OverrideSimilarity.class)

public class TitleSeries implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5545713355322923933L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(3.0f)),
			@Field(name = "titleSeries_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "titleSeries_exact_sanitized", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition="no_specials_lower_case_analyzer"))
	})
	@Column(name = "titleseries")
	private String titleSeries;

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int s) {
		netSuiteInternalID = s;
	}

	public String getTitleSeries() {
		return titleSeries;
	}

	public void setTitleSeries(String titleSeries) {
		this.titleSeries = titleSeries;
	}
	
	public String toString() {
		return "[TitleSeries:" + titleSeries + "]";
	}
}
