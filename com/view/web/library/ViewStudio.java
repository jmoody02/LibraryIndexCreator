package com.view.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import com.playaway.web.library.OverrideSimilarity;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "List_PV_Studio")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
@FilterDef(name="active") 
@Filters({
	@Filter(name="active", condition="inactive = 'false'")
})
public class ViewStudio implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6384059939337537732L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "viewStudio_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "viewStudio_exact_sanitized", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition="no_specials_lower_case_analyzer"))
	})
	@Column(name="PV_Studio")
	private String viewStudio;

	@Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"))
	private String inactive;

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String getStudio() {
		return viewStudio;
	}

	public void setStudio(String s) {
		viewStudio = s;
	}

	public String toString() {
		return "[Genre:" + viewStudio + "]";
	}

	public String getViewStudio() {
		return viewStudio;
	}

	public void setViewStudio(String viewStudio) {
		this.viewStudio = viewStudio;
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
	
}
