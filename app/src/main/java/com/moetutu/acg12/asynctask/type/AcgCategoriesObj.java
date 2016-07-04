package com.moetutu.acg12.asynctask.type;

import java.io.Serializable;

public class AcgCategoriesObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2035660811824488741L;
	
	private int term_id;
	public int getTerm_id() {
		return term_id;
	}
	public void setTerm_id(int term_id) {
		this.term_id = term_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public int getTerm_group() {
		return term_group;
	}
	public void setTerm_group(int term_group) {
		this.term_group = term_group;
	}
	public int getTerm_taxonomy_id() {
		return term_taxonomy_id;
	}
	public void setTerm_taxonomy_id(int term_taxonomy_id) {
		this.term_taxonomy_id = term_taxonomy_id;
	}
	public String getTaxonomy() {
		return taxonomy;
	}
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public int getCat_ID() {
		return cat_ID;
	}
	public void setCat_ID(int cat_ID) {
		this.cat_ID = cat_ID;
	}
	public int getCategory_count() {
		return category_count;
	}
	public void setCategory_count(int category_count) {
		this.category_count = category_count;
	}
	public String getCategory_description() {
		return category_description;
	}
	public void setCategory_description(String category_description) {
		this.category_description = category_description;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getCategory_nicename() {
		return category_nicename;
	}
	public void setCategory_nicename(String category_nicename) {
		this.category_nicename = category_nicename;
	}
	public int getCategory_parent() {
		return category_parent;
	}
	public void setCategory_parent(int category_parent) {
		this.category_parent = category_parent;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String name;
	private String slug;
	private int term_group;
	private int term_taxonomy_id;
	private String taxonomy;
	private String description;
	private int parent;
	private int count;
	private String filter;
	private int cat_ID;
	private int category_count;
	private String category_description;
	
	private String cat_name;
	private String category_nicename;
	private int category_parent;
	private String url;
	
	

}
