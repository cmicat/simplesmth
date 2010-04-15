package com.vivilab.smth.model;

import java.util.List;

public class Board {

	private List articles;
	private int ppage;
	private int npage;
	private int ftype;
	
	public List getArticles() {
		return articles;
	}
	public void setArticles(List articles) {
		this.articles = articles;
	}
	public int getPpage() {
		return ppage;
	}
	public void setPpage(int ppage) {
		this.ppage = ppage;
	}
	public int getNpage() {
		return npage;
	}
	public void setNpage(int npage) {
		this.npage = npage;
	}
	public void setFtype(int ftype) {
		this.ftype = ftype;
	}
	public int getFtype() {
		return ftype;
	}
	
	
}
