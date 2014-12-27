package com.edu.thss.smartdental.model;

public class ImageElement {
	public String name;
	public String description;
	public String time;
	public String department;
	public boolean isMarked;
	public boolean isHidden;
	public boolean isRead;
	public String caseID;
	
	public ImageElement(String n,String d,String t){
		this.name = n;
		this.description = d;
		this.time =t;
		this.department = "";
		this.isHidden = false;
		this.isMarked =false;
		this.isRead = false;
		this.caseID = "";
	}
	
	public ImageElement(String n, String d, String t, boolean h, boolean m, boolean r, String caseid) {
		// TODO Auto-generated constructor stub
		this.name = n;
		this.description = d;
		this.time =t;
		this.department = "";
		this.isHidden = h;
		this.isMarked = m;
		this.isRead = r;
		this.caseID = caseid;
	}
}
