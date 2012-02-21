package com.view.web.library;

import org.hibernate.search.annotations.*;

import javax.persistence.*;

import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "List_PV_Theme")
@Indexed
public class ViewTheme implements Serializable {

	@Id
	@DocumentId
	private int NetSuiteInternalID;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "pv_theme_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")), })
	private String pv_theme;

	@Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"))
	private String inactive;

	//Getters and Setters//
	public int getNetSuiteInternalID() {
		return NetSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		NetSuiteInternalID = netSuiteInternalID;
	}

	public String getPv_theme() {
		return pv_theme;
	}

	public void setPv_theme(String pv_theme) {
		this.pv_theme = pv_theme;
	}

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}
	//End Getters and Setters//
	
}
