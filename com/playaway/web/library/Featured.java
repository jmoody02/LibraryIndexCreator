package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the 
 * list_featured_title table
 * Even though this class is a plain old java object (POJO), its 
 * annotated such that Hibernate and Hibernate Search know how to 
 * persist/create it and index it, respectively.  It will most
 * likely be accessed as a member of the MasterTitle object.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 *
 * @author Jonathan Moody  jmoody@playaway.com
 */

@Entity
@Table(name="list_featured_title")
@Indexed
@FilterDef(name="activeFeatured") 
@Filters({
	@Filter(name="activeFeatured", condition="inactive = 'false'")
})
public class Featured implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -4646792412685433632L;
@Id
  @DocumentId

  private int netSuiteInternalID;
  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
   @Field(name="featured_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  @Column(name="featuredtitle")
  private String featuredTitle;
 
  public String getFeaturedTitle() {
	return featuredTitle;
  }
  public void setFeaturedTitle(String featuredTitle) {
	this.featuredTitle = featuredTitle;
  }
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }

  public String toString() { return "[Featured Title:" + featuredTitle + "]"; }
}
