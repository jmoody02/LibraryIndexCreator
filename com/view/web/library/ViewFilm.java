package com.view.web.library;

import org.hibernate.search.annotations.*;

import javax.persistence.*;

import java.io.Serializable;

import com.playaway.web.library.OverrideSimilarity;
import com.playaway.web.library.TwoWayDB2DateStringBridge;

@SuppressWarnings("serial")
@Entity
@Table(name = "vwPV_Films_Library")
@Indexed
@Similarity(impl = OverrideSimilarity.class)
public class ViewFilm implements Serializable {

	@Id
	@DocumentId
	private int pv_library_film_item;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "dvd_release_date_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")), })
	@FieldBridge(impl = TwoWayDB2DateStringBridge.class)
	private String pv_dvd_release_date;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer")),
			@Field(name = "pv_original_film_release_date_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")), })
	@FieldBridge(impl = TwoWayDB2DateStringBridge.class)
	private String pv_original_film_release_date;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(1.0f)),
			@Field(name = "pv_film_title_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")),
			@Field(name = "pv_film_title_exact", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "lower_case_analyzer")),
			@Field(name = "pv_film_title_sort", index = Index.UN_TOKENIZED, store = Store.YES) })
	private String pv_film_title;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "pv_studio_duration_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")) })
	private String pv_studio_duration;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(2.0f)),
			@Field(name = "pv_actual_duration_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")) })
	private String pv_actual_duration;

	@Fields({
			@Field(index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "text_analyzer"), boost = @Boost(1.0f)),
			@Field(name = "film_description_phonetic", index = Index.TOKENIZED, store = Store.YES, analyzer = @Analyzer(definition = "phonetic_analyzer")) })
	private String pv_film_description;

	//Getters and Setters//
	public int getPv_library_film_item() {
		return pv_library_film_item;
	}

	public void setPv_library_film_item(int pv_library_film_item) {
		this.pv_library_film_item = pv_library_film_item;
	}

	public String getPv_dvd_release_date() {
		return pv_dvd_release_date;
	}

	public void setPv_dvd_release_date(String pv_dvd_release_date) {
		this.pv_dvd_release_date = pv_dvd_release_date;
	}

	public String getPv_original_film_release_date() {
		return pv_original_film_release_date;
	}

	public void setPv_original_film_release_date(
			String pv_original_film_release_date) {
		this.pv_original_film_release_date = pv_original_film_release_date;
	}

	public String getPv_film_title() {
		return pv_film_title;
	}

	public void setPv_film_title(String pv_film_title) {
		this.pv_film_title = pv_film_title;
	}

	public String getPv_studio_duration() {
		return pv_studio_duration;
	}

	public void setPv_studio_duration(String pv_studio_duration) {
		this.pv_studio_duration = pv_studio_duration;
	}

	public String getPv_actual_duration() {
		return pv_actual_duration;
	}

	public void setPv_actual_duration(String pv_actual_duration) {
		this.pv_actual_duration = pv_actual_duration;
	}

	public String getPv_film_description() {
		return pv_film_description;
	}

	public void setPv_film_description(String pv_film_description) {
		this.pv_film_description = pv_film_description;
	}
	//End Getters and Setters//
}
