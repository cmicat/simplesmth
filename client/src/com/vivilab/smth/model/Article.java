package com.vivilab.smth.model;

import java.util.List;

public class Article {

	private String id;
	private String title;
	private String info;
	private String content;
	private String topid;
	private String author;
	private String date;
	private int hasAttach;
	private String atthUrl;
	
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
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setHasAttach(int hasAttach) {
		this.hasAttach = hasAttach;
	}
	public int getHasAttach() {
		return hasAttach;
	}
	public void setAtthUrl(String atthUrl) {
		this.atthUrl = atthUrl;
	}
	public String getAtthUrl() {
		return atthUrl;
	}
	
}
