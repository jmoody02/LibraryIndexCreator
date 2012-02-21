package com.playaway.web.library;

import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="list_web_category")
@Indexed
public class ReleaseCategory implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -7981133291421616582L;

@Id
  @DocumentId
  private int categoryID;
  
  @Fields( {
   @Field(index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "text_analyzer"), boost=@Boost(2.0f)),
   @Field(name="category_exact", index=Index.TOKENIZED, store=Store.YES, analyzer= @Analyzer(definition = "lower_case_analyzer"))
  })
  @Column(name="category")
  private String category;
 
  

  public int getCategoryID() {
	return categoryID;
}



public void setCategoryID(int categoryID) {
	this.categoryID = categoryID;
}



public String getCategory() {
	return category;
}



public void setCategory(String category) {
	this.category = category;
}



public String toString() { return "[Category:" + category + "]"; }
}
