package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the list_genre table Even though this class
 * is a plain old java object (POJO), its annotated such that Hibernate and
 * Hibernate Search know how to persist/create it and index it, respectively. It
 * will most likely be accessed as a member of the MasterTitle object.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 * 
 * @author Rob Tandy rtandy@playaway.com
 */

@Entity
@Table(name = "list_genre")
@Indexed
@FilterDef(name="active") 
@Filters({
	@Filter(name="active", condition="inactive = 'false'")
})
public class Genre implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3314546862751045421L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "genre_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "genre_exact_sanitized", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition="no_specials_lower_case_analyzer"))
	})
	private String genre;

	@Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"))
	private String inactive;

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String s) {
		genre = s;
	}

	public String toString() {
		return "[Genre:" + genre + "]";
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
}
