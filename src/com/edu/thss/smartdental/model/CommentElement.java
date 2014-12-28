package com.edu.thss.smartdental.model;

public class CommentElement {
	public String content;
	public String time;
	public String author;
	public int id;
	public boolean isDeletable;
	public int postId;
	public String type;
	public String toName;
	
	public CommentElement(String content, String time, String author, int id, int postId,String type,String toName, String UserName){
		this.content = content;
		this.time = time;
		this.author = author;
		this.isDeletable = false;
		this.id = id;
		this.postId = postId;
		this.type = type;
		this.toName = toName;
		if (type.equals("reply")){
			this.content = "to "+toName+": "+this.content;
		}
		if(UserName.equals(author)){
			this.isDeletable = true;
		}
	}

}
