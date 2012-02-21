package com.view.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "List_PV_Educational_Theme")
@Indexed
@FilterDef(name = "activeEduThemes")
@Filters({ @Filter(name = "activeEduThemes", condition = "inactive = 'false'") })
public class ViewEducationalTheme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "educationalTheme_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	@Column(name = "PV_Educational_Theme")
	private String educationalTheme;

	public String toString() {
		return "[Educational Theme:" + educationalTheme + "]";
	}
	
	public String getEducationalTheme() {
		return educationalTheme;
	}

	public void setEducationalTheme(String educationalTheme) {
		this.educationalTheme = educationalTheme;
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
}
