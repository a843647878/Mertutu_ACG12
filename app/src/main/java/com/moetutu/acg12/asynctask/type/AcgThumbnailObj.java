package com.moetutu.acg12.asynctask.type;

import java.io.Serializable;

public class AcgThumbnailObj implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8564229432789801882L;
	private String thumbnail;
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	private String medium;

}
