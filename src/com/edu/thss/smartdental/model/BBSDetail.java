package com.edu.thss.smartdental.model;

public class BBSDetail {
	public String title;
	public String content;
	public String time;
	public String tab;
	public String author;
	public boolean isDeletable;
	
	public BBSDetail(String title, String content, String time, String author){
		this.title = title;
		this.content = content;
		this.time = time.substring(0, time.length()-3);
		this.author = author;
		this.tab = "";
		this.isDeletable = false;
	}
}
