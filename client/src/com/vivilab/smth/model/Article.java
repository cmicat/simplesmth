package com.vivilab.smth.model;

public class Article {

	private String id;
	private String title;
	private String info;
	private String content;
	private String topid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTopid(String topid) {
		this.topid = topid;
	}
	public String getTopid() {
		return topid;
	}
	
}
