package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the 
 * list_awards table
 * Even though this class is a plain old java object (POJO), its 
 * annotated such that Hibernate and Hibernate Search know how to 
 * persist/create it and index it, respectively.  It will most
 * likely be accessed as a member of the MasterTitle object.
 * 
 * @see LibrarySearchService
 * @see MasterTitle
 *
 * @author Rob Tandy rtandy@playaway.com
 */

@Entity
@Table(name="list_awards")
@Indexed
@FilterDef(name="activeAwards") 
@Filters({
	@Filter(name="activeAwards", condition="inactive = 'false'")
})
public class Award implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -6502432443340915854L;
@Id
  @DocumentId

  private int netSuiteInternalID;
  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
   @Field(name="award_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  @Column(name="awards")
  private String award;
 
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public String getAward() {return award;}
  
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }
  public void setAward(String s) {award=s;} 

  public String toString() { return "[Award:" + award + "]"; }
}
