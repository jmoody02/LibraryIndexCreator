package com.playaway.web.library;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
 
/**
 * This class is represents a row in the 
 * list_version table;
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
@Table(name="list_version")
@Indexed
@FilterDef(name="activeVersions") 
@Filters({
	@Filter(name="activeVersions", condition="inactive = 'false'")
})
public class Abridgement implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -2489127772321598729L;

@Id
  @DocumentId

  private int netSuiteInternalID;
  
  @Fields({
	  @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
	  @Field(name="version_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  private String version;
  
  private String inactive;
  
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public String getVersion() {return version;}
  public String getInactive() {return inactive;}
  
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }
  public void setVersion(String s) { version = s; }
  public void setInactive(String s) { inactive = s; }

  public String toString() { return "[Abridgement:" + version+ "]"; }
}
