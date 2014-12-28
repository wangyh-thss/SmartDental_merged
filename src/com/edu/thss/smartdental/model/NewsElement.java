package com.edu.thss.smartdental.model;

public class NewsElement {

	private String newsID;
	private String postID;
	private String content;
	private String userName;
	
	public NewsElement(String newsID, String postID, String content, String userName) {
		this.newsID = newsID;
		this.postID = postID;
		this.content = content;
		this.userName = userName;
	}
	
	public String getNewsID() {
		return this.newsID;
	}
	
	public String getPostID() {
		return this.postID;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
}
