package com.playaway.web.library;

import org.hibernate.search.annotations.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.apache.solr.analysis.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;


/**
 * This class is represents a row in the MasterTitleRecord (mtr) table. Even
 * though this class is a plain old java object (POJO), its annotated such that
 * Hibernate and Hibernate Search know how to persist/create it and index it,
 * respectively. Tables that would be joined to MTR to get complete information,
 * such as author, etc become class member objects here.
 * 
 * Noteworthly is that authors, narrators, and translaters are all represented
 * by Author objects as they contain the same info and come from the same table.
 * 
 * @see Author
 * @see Genre
 * 
 * @author Rob Tandy rtandy@playaway.com
 */
@SuppressWarnings("serial")
// Entity, Table, Id are annotations for Hibernate
// Indexed,DocumentId, Field are annotation for Hibernate Search
@Entity
// @Table(name="Master_Title_Record")
@Table(name = "vwMTR_Library")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
@AnalyzerDefs( {
		@AnalyzerDef(name = "phonetic_analyzer", tokenizer = @TokenizerDef(factory = HTMLStripStandardTokenizerFactory.class), filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = TrimFilterFactory.class),
				@TokenFilterDef(factory = PhoneticFilterFactory.class, params = {
						@Parameter(name = "encoder", value = "DoubleMetaphone"),
						@Parameter(name = "inject", value = "true") }) }),
		@AnalyzerDef(name = "text_analyzer", tokenizer = @TokenizerDef(factory = HTMLStripStandardTokenizerFactory.class), filters = {
			    //@TokenFilterDef(factory = StandardFilterFactory.class),
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				//@TokenFilterDef(factory = StopFilterFactory.class),
				@TokenFilterDef(factory = TrimFilterFactory.class) }),
		@AnalyzerDef(name = "lower_case_analyzer", tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class), filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = TrimFilterFactory.class) }),
		@AnalyzerDef(name = "no_specials_lower_case_analyzer", tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class), 
				filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = TrimFilterFactory.class), 
				@TokenFilterDef(factory = PatternReplaceFilterFactory.class,
						params = {  
							@Parameter(name="pattern", value="[\\W&&[^\\s]]"),
							@Parameter(name="replacement", value=""),
							@Parameter(name="replace", value="all")
						}),
				@TokenFilterDef(factory = PatternReplaceFilterFactory.class,
						params = {  
							@Parameter(name="pattern", value="\\s+"),
							@Parameter(name="replacement", value="_"),
							@Parameter(name="replace", value="all")
						})
				})
		} )
public class MasterTitle implements Serializable {
	private transient String recordType = "MTR";
	@Transient
	public String getRecordType() {
		return recordType;
	}

	@Id
	@DocumentId
	// primary key of MTR table
	private int netSuiteInternalID;

	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "title_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")),
			@Field(name = "title_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")), 
			@Field(name = "title_sort", index = Index.UN_TOKENIZED, store = Store.YES) })
	private String title;
	
	@Fields( {
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(1.0f)),
			@Field(name = "description_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")) })
			@Field(name = "description_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	private String description;

	@Fields( {
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="sku_exact", index = Index.TOKENIZED, store=Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	private String sku;

	// price displayed on library web site
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="libraryPrice_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	@Column(name = "playaway_slp")
	private String libraryPrice;

	// thumbnail of playaway
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	@Column(name = "link_webimg_thumb")
	private String webThumb;

	// lib item code such that we can display a detail page
	@Column(name = "library_item")
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private String libraryItem;

	// library isbn number
	@Column(name = "isbn13_library")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="libraryISBN_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	private String libraryISBN;

	// launch date
	@Column(name = "launch_date")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="launchDate_exact",index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	@FieldBridge(impl = TwoWayDB2DateStringBridge.class)
	private String launchDate;

	@Column(name = "series")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
		@Field(name="series_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	private String series;

	@Column(name = "Release_Type_Web")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="releaseType_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	private String releaseType;
	
	/*@Column(name= "Category_Web")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="releaseCategory_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))		
	})
	private String releaseCategory;*/
	
	@Column(name = "Publisher_Street_Date")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name = "pubStreetDate_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
	})
	@FieldBridge(impl = TwoWayDB2DateStringBridge.class)
	private String pubStreetDate;

	/*@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "author")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Author author;*/

	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Author.class)
	@JoinTable(name="Split_Authors", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_author"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	@Boost(2.0f)
	private List<Author> authors;
		
	/*@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "awards")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Award award;*/
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Award.class)
	@JoinTable(name="Split_Awards", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_awards"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<Award> awards;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Featured.class)
	@JoinTable(name="Split_Feature_Lists", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_feature_list"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<Featured> featuredTitles;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "abridgement")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Abridgement abridgement;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "age_level")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private AgeLevel ageLevel;

	/*@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "narrator")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Author narrator;*/
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Author.class)
	@JoinTable(name="Split_Narrators", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_narrator"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	@Boost(2.0f)
	private List<Author> narrators;
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Author.class)
	@JoinTable(name="Split_Translators", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_translator"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	@Boost(2.0f)
	private List<Author> translators;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "publisher")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Publisher publisher;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "genre_primary")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Genre primaryGenre;
 
	/*@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "genre_secondary")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Genre secondaryGenre;*/
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=Genre.class)
	@JoinTable(name="Split_Genre_Secondary", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_genre_secondary"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<Genre> secondary_genres;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "titleseries")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private TitleSeries titleSeries;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "territory_availability")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private Territory territory;
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=ReleaseCategory.class)
	@JoinTable(name="vwCategorySearch_Library", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="CategoryID"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<ReleaseCategory> releaseCategories;
	
	/*
	 * this will hold the catEntryID from commerce. This gets populated by the
	 * LibrarySearch object when it builds its lucene index.
	 */
	@Transient
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private int catEntryID;

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getSku() {
		return sku;
	}

	public String getLibraryPrice() {
		return libraryPrice;
	}

	public String getLibraryItem() {
		return libraryItem;
	}

	public String getLibraryISBN() {
		return libraryISBN;
	}

	public String getWebThumb() {
		return webThumb;
	}

	public String getReleaseType(){
		return releaseType;
	}
	
	public List<Author> getAuthors() {
        return authors;
    }
	
	public List<Author> getNarrators() {
		return narrators;
	}

	public List<Author> getTranslators() {
		return translators;
	}

	public Genre getPrimaryGenre() {
		return primaryGenre;
	}

	public List<Genre> getSecondary_genres() {
		return secondary_genres;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public int getNetSuiteInternalID() {
		return netSuiteInternalID;
	}

	public int getCatEntryID() {
		return catEntryID;
	}

	public Abridgement getAbridgement() {
		return abridgement;
	}

	public AgeLevel getAgeLevel() {
		return ageLevel;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public String getLaunchDate() {
		return launchDate;
	}

	public String getSeries() {
		return series;
	}

	public String getPubStreetDate() {
		return pubStreetDate;
	}

	public TitleSeries getTitleSeries(){
		return titleSeries;
	}
	
	public Territory getTerritory() {
		return territory;
	}
	
	public List<ReleaseCategory> getReleaseCategories() {
		return releaseCategories;
	}

	public void setReleaseCategories1(List<ReleaseCategory> rc) {
		this.releaseCategories = rc;
	}

	public void setTitle(String s) {
		title = s;
	}

	public void setDescription(String s) {
		description = s;
	}
	public void setAuthors(List<Author> authorList) {
		authors = authorList;
	}

	public void setSku(String s) {
		sku = s;
	}

	public void setLibraryPrice(String s) {
		libraryPrice = s;
	}

	public void setLibraryItem(String s) {
		libraryItem = s;
	}

	public void setLibraryISBN(String s) {
		libraryISBN = s;
	}

	public void setWebThumb(String s) {
		webThumb = s;
	}

	public void setReleaseType(String s) {
		releaseType = s;
	}

	public void setNarrators(List<Author> narrators) {
		this.narrators = narrators;
	}
	
	public void setTranslators(List<Author> translators) {
		this.translators = translators;
	}

	public void setPrimaryGenre(Genre g) {
		primaryGenre = g;
	}

	public void setSecondary_genres(List<Genre> secondaryGenres) {
		secondary_genres = secondaryGenres;
	}

	public void setPublisher(Publisher p) {
		publisher = p;
	}

	public void setNetSuiteInternalID(int s) {
		netSuiteInternalID = s;
	}

	public void setCatEntryID(int i) {
		catEntryID = i;
	}

	public void setAbridgement(Abridgement a) {
		abridgement = a;
	}

	public void setAgeLevel(AgeLevel a) {
		ageLevel = a;
	}

	public void setAwards(List<Award> awardList) {
		this.awards = awardList;
	}

	public void setLaunchDate(String ld) {
		launchDate = ld;
	}

	public void setSeries(String s) {
		series = s;
	}

	public void setPubStreetDate(String sd) {
		pubStreetDate = sd;
	}
	
	public void setTitleSeries(TitleSeries s){
		titleSeries = s;
	}
	 
	public void setTerritory(Territory t) {
		territory = t;
	}

	public String toString() {
		String s = new String();
		s += "NetSuiteInternalID: " + netSuiteInternalID + "\n";
		s += "Title: " + title + "\n";
		s += "library Price: " + libraryPrice + "\n";
		s += "webThumb: " + webThumb + "\n";
		s += "SKU: " + sku + "\n";
		s += "libraryItem: " + libraryItem + "\n";
		s += "libraryISBN: " + libraryISBN + "\n";
		s += "series: " + series + "\n";
		s += "Author: " + authors.get(0) + "\n";
		s += "Publisher: " + publisher + "\n";
		s += "PrimaryGenre: " + primaryGenre + "\n";
		s += "Abridgement: " + abridgement + "\n";
		s += "AgeLevel : " + ageLevel + "\n";
		s += "catEntryID: " + catEntryID + "\n";
		s += "launchDate: " + launchDate + "\n";
		s += "pubStreetDate: " + pubStreetDate + "\n";
		s += "releaseType: " + releaseType + "\n";
		s += "titleSeries: " + titleSeries + "\n";
		return s;
	}

	public List<Featured> getFeaturedTitles() {
		return featuredTitles;
	}

	public void setFeaturedTitles(List<Featured> featuredTitles) {
		this.featuredTitles = featuredTitles;
	}
}
