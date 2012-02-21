package com.playaway.web.library;

import org.hibernate.search.annotations.*;

import org.apache.solr.analysis.*;

import javax.persistence.*;

import java.io.Serializable;


/**
 * This class is represents a row in the Services View (vwServices_Library) table. Even
 * though this class is a plain old java object (POJO), its annotated such that
 * Hibernate and Hibernate Search know how to persist/create it and index it,
 * respectively. 
 * 
 * @author J Moody
 */
@SuppressWarnings("serial")
// Entity, Table, Id are annotations for Hibernate
// Indexed,DocumentId, Field are annotation for Hibernate Search
@Entity
@Table(name = "vwServices_Library")
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
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
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
public class Service implements Serializable {
	private transient String recordType = "SERVICE";

	@Transient
	public String getRecordType() {
		return recordType;
	}

	@Transient
	@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"))
	private int catEntryID;

	@Id
	@DocumentId
	// primary key
	private int idService;

	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name = "sku_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
		private String sku;
	
	@Column(name = "Display_Name")
	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "displayName_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")),
			@Field(name = "displayName_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "title_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "displayName", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "displayName_sort", index = Index.UN_TOKENIZED, store = Store.YES) })
	private String displayName;
	
	@Column(name = "US_Price")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name = "price_usa_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
		@Field(name = "price_usa_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
		@Field(name = "libraryPrice_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")) })
		private String price_usa;
	
	@Fields( {
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
		@Field(name = "description_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")) })
		private String description;

	@Column(name = "libraryISBN")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="libraryISBN_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	public String getLibraryISBN(){
		return "9999999999999";
	}
	
	// launch date
	@Column(name = "launch_date")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="launchDate_exact",index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	public String getLaunchDate() {
		return "1900-01-01 21:00:00:0";
	}
	
	//pubStreetDate
	// launch date
	@Column(name = "pubStreetDate")
	@Fields({
		@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
		@Field(name="pubStreetDate_exact",index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer"))
	})
	public String getPubStreetDate() {
		return "1900-01-01 21:00:00:0";
	}
	
	public int getCatEntryID() {
		return catEntryID;
	}

	public void setCatEntryID(int catEntryID) {
		this.catEntryID = catEntryID;
	}

	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPrice_usa() {
		return price_usa;
	}

	public void setPrice_usa(String price_usa) {
		this.price_usa = price_usa;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
}
