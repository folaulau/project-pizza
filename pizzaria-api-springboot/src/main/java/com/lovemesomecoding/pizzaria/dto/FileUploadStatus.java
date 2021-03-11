package com.lovemesomecoding.pizzaria.dto;

public class FileUploadStatus {

	private boolean uploaded;
	private String url;
	
	public FileUploadStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isUploaded() {
		return uploaded;
	}
	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
