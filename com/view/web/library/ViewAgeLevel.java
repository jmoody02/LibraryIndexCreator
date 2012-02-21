package com.view.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="List_PV_Age_Level")
@Indexed
@FilterDef(name="activeAges") 
@Filters({
	@Filter(name="activeAges", condition="inactive = 'false'")
})
public class ViewAgeLevel implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5716558100138403849L;

@Id
  @DocumentId
	private int netSuiteInternalID;

  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
   @Field(name="viewAgeLevel_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  @Column(name="PV_Age_Level")
  private String ageLevel;
  
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public String getAgeLevel() {return ageLevel;}
  
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }
  public void setAgeLevel(String s) { ageLevel = s; }

  public String toString() { return "[AgeLevel:" + ageLevel+ "]"; }
}
