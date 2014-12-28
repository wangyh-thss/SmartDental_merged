package com.edu.thss.smartdental.model;

public class BBSElement {
	public String title;
	public String preview;
	public String content;
	public String time;
	public String tab;
	public String author;
	public String id;
	public boolean isCollected;
	public boolean isDeletable;
	
	public BBSElement(String title,String content, String time, String author, boolean isCollected, boolean isDeletable, String id){
		this.title = title;
		this.content = content;
		if (content.length()<=20){
			this.preview = content;
		}else{
			this.preview = content.substring(0, 20);
		}
		this.time =time.substring(0, time.length()-3);
		this.tab = "";
		this.author = author;
		this.isCollected = isCollected;
		this.isDeletable = isDeletable;
		this.id = id;
	}
}
