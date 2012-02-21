package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the list_publisher table. Even though this
 * class is a plain old java object (POJO), its annotated such that Hibernate
 * and Hibernate Search know how to persist/create it and index it,
 * respectively. It will most likely be accessed as a member of the MasterTitle
 * object.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 * 
 * @author Rob Tandy rtandy@playaway.com
 */

@Entity
@Table(name = "list_publisher")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
@FilterDef(name = "activePublishers")
@Filters({ @Filter(name = "activePublishers", condition = "inactive = 'false'") })
public class Publisher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6025290032833008182L;
	@Id
	@DocumentId
	private int netSuiteInternalID;
	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "publisher_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "publisher_exact_sanitized", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "no_specials_lower_case_analyzer")) })
	private String publisher;

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String s) {
		publisher = s;
	}

	public String toString() {
		return "[Publisher:" + publisher + "]";
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
}
