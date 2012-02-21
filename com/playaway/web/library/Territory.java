package com.playaway.web.library;

import org.hibernate.search.annotations.*;
import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the list_international table. Even though
 * this class is a plain old java object (POJO), its annotated such that
 * Hibernate and Hibernate Search know how to persist/create it and index it,
 * respectively. It will most likely be accessed as a member of the MasterTitle
 * object.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 * 
 * @author Jonathan Moody jmoody@playaway.com
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "list_international")
public class Territory implements Serializable {
	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "value_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	@Column(name = "value")
	private String value;

	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f))
	@Column(name = "canada")
	private String canada;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCanada() {
		return canada;
	}

	public void setCanada(String canada) {
		this.canada = canada;
	}

	public String toString() {
		return "[Value:" + value + "]";
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
}
