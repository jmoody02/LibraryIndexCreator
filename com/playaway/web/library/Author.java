package com.playaway.web.library;

import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is represents a row in the 
 * list_author_narrator_translator table.
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

@SuppressWarnings("serial")
@Entity
@Table(name="list_author_narrator_translator")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
public class Author implements Serializable {
  @Id
  @DocumentId

  private int netSuiteInternalID;
  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(1.0f)),
   @Field(name="name_phonetic", index=Index.TOKENIZED, store=Store.YES,analyzer= @Analyzer(definition = "phonetic_analyzer")),
   @Field(name="name_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer")),
   @Field(name="name_exact_sanitized", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition="no_specials_lower_case_analyzer"))
  })
  private String name;
  private String prefix;
  
  @Fields({
	  @Field(index=Index.TOKENIZED, store=Store.YES),
	  @Field(name="firstName_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  private String firstName;
  
  @Fields({
	  @Field(index=Index.TOKENIZED, store=Store.YES),
	  @Field(name="lastName_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer")),
	  @Field(name="lastName_search", index=Index.UN_TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  private String lastName;
  
  private String suffix;
 
  public int getNetSuiteInternalID() { return netSuiteInternalID;}
  public String getPrefix() {return prefix;}
  public String getName() {return name;}
  public String getFirstName() {return firstName;}
  public String getLastName() {return lastName;}
  public String getSuffix() {return suffix;}
  
  public void setNetSuiteInternalID(int s) { netSuiteInternalID = s; }
  public void setPrefix(String s) {prefix=s;} 
  public void setName(String s) {name=s;} 
  public void setFirstName(String s) {firstName=s;} 
  public void setLastName(String s) {lastName=s;} 
  public void setSuffix(String s) {suffix=s;} 
  public void setOmitNorms(String omitNorms){
	  omitNorms="True";
  }

  public String toString() { return "[Author:" + firstName+" "+lastName + "]"; }
}
