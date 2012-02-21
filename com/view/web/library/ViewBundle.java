package com.view.web.library;

import org.hibernate.search.annotations.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.apache.solr.analysis.*;

import com.playaway.web.library.OverrideSimilarity;
import com.playaway.web.library.TwoWayDB2DateStringBridge;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;


/**
 * This class is represents a row in the Playaway View (pv_library) table. Even
 * though this class is a plain old java object (POJO), its annotated such that
 * Hibernate and Hibernate Search know how to persist/create it and index it,
 * respectively. Tables that would be joined to MTR to get complete information,
 * such as author, etc become class member objects here.
 * 
 * Noteworthly is that authors, narrators, and translaters are all represented
 * by Author objects as they contain the same info and come from the same table.
 * 
 * @see Author
 * @see ViewGenre
 * 
 * @author J Moody
 */ 
@SuppressWarnings("serial")
// Entity, Table, Id are annotations for Hibernate
// Indexed,DocumentId, Field are annotation for Hibernate Search
@Entity
@Table(name = "vwPV_Bundles_Library")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
@AnalyzerDefs({
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
		@AnalyzerDef(name = "no_specials_lower_case_analyzer", tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class), filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = TrimFilterFactory.class),
				@TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
						@Parameter(name = "pattern", value = "[\\W&&[^\\s]]"),
						@Parameter(name = "replacement", value = ""),
						@Parameter(name = "replace", value = "all") }),
				@TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
						@Parameter(name = "pattern", value = "\\s+"),
						@Parameter(name = "replacement", value = "_"),
						@Parameter(name = "replace", value = "all") }) }) })
public class ViewBundle implements Serializable {
	private transient String recordType = "VIEW";

	@Transient
	public String getRecordType() {
		return recordType;
	}

	@Transient
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private int catEntryID;
	
	public int getCatEntryID() {
		return catEntryID;
	}

	public void setCatEntryID(int catEntryID) {
		this.catEntryID = catEntryID;
	}

	public int getPv_library_item() {
		return pv_library_item;
	}

	public void setPv_library_item(int pv_library_item) {
		this.pv_library_item = pv_library_item;
	}

	public int getPv_item_type() {
		return pv_item_type;
	}

	public void setPv_item_type(int pv_item_type) {
		this.pv_item_type = pv_item_type;
	}

	public String getPv_bundle_title() {
		return pv_bundle_title;
	}

	public void setPv_bundle_title(String pv_bundle_title) {
		this.pv_bundle_title = pv_bundle_title;
	}

	public String getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(String launchDate) {
		this.launchDate = launchDate;
	}

	public String getIsbn13_library() {
		return isbn13_library;
	}

	public void setIsbn13_library(String isbn13_library) {
		this.isbn13_library = isbn13_library;
	}

	public String getItem_sku() {
		return item_sku;
	}

	public void setItem_sku(String item_sku) {
		this.item_sku = item_sku;
	}

	public String getPv_price_usa() {
		return pv_price_usa;
	}

	public void setPv_price_usa(String pv_price_usa) {
		this.pv_price_usa = pv_price_usa;
	}

	public String getPv_price_canada() {
		return pv_price_canada;
	}

	public void setPv_price_canada(String pv_price_canada) {
		this.pv_price_canada = pv_price_canada;
	}

	@Id
	@DocumentId
	// primary key of PV_Library table
	private int pv_library_item;

	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private int pv_item_type;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "title_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")),
			@Field(name = "title_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "title", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "title_sort", index = Index.UN_TOKENIZED, store = Store.YES) })
	private String pv_bundle_title;

	// launch date
	@Column(name = "launch_date")
	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "pubStreetDate_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "pubStreetDate", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "launchDate_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	@FieldBridge(impl = TwoWayDB2DateStringBridge.class)
	private String launchDate;

	// library isbn number
	@Fields({ 
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "libraryISBN_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "isbn13_library_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	private String isbn13_library;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "item_sku_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name="sku_exact", index = Index.TOKENIZED, store=Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "sku", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")) })
	private String item_sku;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PV_Rating")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewRating viewRating;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PV_Age_Level")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewAgeLevel viewAgeLevel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PV_Genre")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewGenre viewGenre;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PV_Studio")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewStudio viewStudio;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pv_grade_level")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewGradeLevel viewGradeLevel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pv_edu_theme")
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private ViewEducationalTheme viewEducationalTheme;
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=ViewFilm.class)
	@JoinTable(name="Jn_PV_Items", joinColumns=@JoinColumn(name="BundleItemID"), inverseJoinColumns=@JoinColumn(name="FilmItemID"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<ViewFilm> films;
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=ViewTheme.class)
	@JoinTable(name="Split_PV_Theme", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="split_pv_theme"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<ViewTheme> themes;
	
	@ManyToMany(cascade = CascadeType.ALL, targetEntity=ViewReleaseCategory.class)
	@JoinTable(name="vwCategorySearch_Library_View", joinColumns=@JoinColumn(name="NetSuiteInternalID"), inverseJoinColumns=@JoinColumn(name="CategoryID"))
	@NotFound(action = NotFoundAction.IGNORE)
	@IndexedEmbedded
	private List<ViewReleaseCategory> releaseCategories;
	
	public ViewGenre getViewGenre() {
		return viewGenre;
	}

	public void setViewGenre(ViewGenre viewGenre) {
		this.viewGenre = viewGenre;
	}

	public ViewStudio getViewStudio() {
		return viewStudio;
	}

	public void setViewStudio(ViewStudio viewStudio) {
		this.viewStudio = viewStudio;
	}

	public ViewGradeLevel getViewGradeLevel() {
		return viewGradeLevel;
	}

	public void setViewGradeLevel(ViewGradeLevel viewGradeLevel) {
		this.viewGradeLevel = viewGradeLevel;
	}

	public ViewAgeLevel getViewAgeLevel() {
		return viewAgeLevel;
	}

	public ViewRating getViewRating() {
		return viewRating;
	}

	public void setViewRating(ViewRating viewRating) {
		this.viewRating = viewRating;
	}

	public void setViewAgeLevel(ViewAgeLevel viewAgeLevel) {
		this.viewAgeLevel = viewAgeLevel;
	}

	public ViewEducationalTheme getViewEducationalTheme() {
		return viewEducationalTheme;
	}

	public void setViewEducationalTheme(ViewEducationalTheme viewEducationalTheme) {
		this.viewEducationalTheme = viewEducationalTheme;
	}
	
	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "price_usa_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "libraryPrice_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	private String pv_price_usa;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "price_canada_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
	private String pv_price_canada;

	public List<ViewFilm> getFilms() {
		return films;
	}

	public void setFilms(List<ViewFilm> films) {
		this.films = films;
	}
	
	public List<ViewTheme> getThemes() {
		return themes;
	}

	public void setThemes(List<ViewTheme> themes) {
		this.themes = themes;
	}

	public List<ViewReleaseCategory> getReleaseCategories() {
		return releaseCategories;
	}

	public void setReleaseCategories(List<ViewReleaseCategory> releaseCategories) {
		this.releaseCategories = releaseCategories;
	}

	public String getBundleDuration() {
		Calendar timeSum = Calendar.getInstance();
		timeSum.set(0, 0, 0, 0, 0, 0);
		for(int i=0; i<films.size(); i++) {
			ViewFilm tmpFilm = films.get(i);
			String filmDuration = tmpFilm.getPv_actual_duration();
			String[] timeSegments = filmDuration.split(":");
			timeSum.add(Calendar.HOUR_OF_DAY, new Integer(timeSegments[0]).intValue());
			timeSum.add(Calendar.MINUTE, new Integer(timeSegments[1]).intValue());
			timeSum.add(Calendar.SECOND, new Integer(timeSegments[2]).intValue());
		}
		return timeSum.getTime().toString();
	}

	
	// @ManyToMany(cascade = CascadeType.ALL, targetEntity=Author.class)
	// @JoinTable(name="Split_Authors",
	// joinColumns=@JoinColumn(name="NetSuiteInternalID"),
	// inverseJoinColumns=@JoinColumn(name="split_author"))
	// @NotFound(action = NotFoundAction.IGNORE)
	// @IndexedEmbedded
	// private List<Author> authors;

	// @ManyToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "age_level")
	// @NotFound(action = NotFoundAction.IGNORE)
	// @IndexedEmbedded
	// private AgeLevel ageLevel;

	/*
	 * this will hold the catEntryID from commerce. This gets populated by the
	 * LibrarySearch object when it builds its lucene index.
	 */
	/*@Transient
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private int catEntryID;*/

}
