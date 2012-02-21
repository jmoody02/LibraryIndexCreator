package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the 
 * list_age_level table
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
@Table(name="list_age_level")
@Indexed
@FilterDef(name="activeAges") 
@Filters({
	@Filter(name="activeAges", condition="inactive = 'false'")
})
public class AgeLevel implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 8576894669819894202L;

@Id
  @DocumentId
	private int netSuiteInternalID;

  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
   @Field(name="ageLevel_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  @Column(name="age_level")
  private String ageLevel;
  
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public String getAgeLevel() {return ageLevel;}
  
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }
  public void setAgeLevel(String s) { ageLevel = s; }

  public String toString() { return "[AgeLevel:" + ageLevel+ "]"; }
}
