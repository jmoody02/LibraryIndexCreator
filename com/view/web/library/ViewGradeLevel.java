package com.view.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "List_PV_Grade_Level")
@Indexed
@FilterDef(name="active") 
@Filters({
	@Filter(name="active", condition="inactive = 'false'")
})
public class ViewGradeLevel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6384059939337537732L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "viewGrade_Level_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "viewGrade_Level_exact_sanitized", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition="no_specials_lower_case_analyzer"))
	})
	@Column(name="PV_Grade_Level")
	private String viewGradeLevel;

	@Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"))
	private String inactive;

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String toString() {
		return "[Genre:" + viewGradeLevel + "]";
	}

	public String getViewGradeLevel() {
		return viewGradeLevel;
	}

	public void setViewGradeLevel(String viewGradeLevel) {
		this.viewGradeLevel = viewGradeLevel;
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
	
}
