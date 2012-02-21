package com.view.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "List_PV_Film_Rating")
@Indexed
@FilterDef(name = "activeRatings")
@Filters({ @Filter(name = "activeRatings", condition = "inactive = 'false'") })
public class ViewRating implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@DocumentId
	private int netSuiteInternalID;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "filmRating_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	@Column(name = "PV_Film_Rating")
	private String filmRating;

	public String toString() {
		return "[Film Rating:" + filmRating + "]";
	}

	public String getFilmRating() {
		return filmRating;
	}

	public void setFilmRating(String filmRating) {
		this.filmRating = filmRating;
	}
	
	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public void setNetSuiteInternalID(int netSuiteInternalID) {
		this.netSuiteInternalID = netSuiteInternalID;
	}
}
