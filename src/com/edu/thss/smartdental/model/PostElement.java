/*
 * 锟斤拷锟竭ｏ拷锟斤拷锟斤拷锟�
 * 锟斤拷锟节ｏ拷2014锟斤拷12锟斤拷7锟斤拷
 */
package com.edu.thss.smartdental.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.edu.thss.smartdental.R.string;
import com.edu.thss.smartdental.RemoteDB.PostDBUtil;

public class PostElement {
	private String title;
	private String content;
	private String author;
	private String tab;
	private String doctorName;
	private Date date;
	private Boolean onlyToDoctor;
	private static final PostDBUtil db = new PostDBUtil();
	
	public PostElement() {
	}
	
	public PostElement (String title, String content, String author, String tab, String doctorName, Date date, Boolean onlyToDoctor) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.tab = tab;
		this.date = date;
		this.doctorName = doctorName;
		this.onlyToDoctor = onlyToDoctor;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void setOnlyToDoctor(Boolean onlyToDoctor) { 
		this.onlyToDoctor = onlyToDoctor;
	}
	
	public Boolean getOnlyToDoctor() { 
		return this.onlyToDoctor;
	}
	
	public String getDoctor() {
		return this.doctorName;
	}
	
	
	public String insertToDB() {
		return db.insertPost(this.title, this.content, this.author, this.doctorName, this.tab);
	}
}
