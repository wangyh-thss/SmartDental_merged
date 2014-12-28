package com.edu.thss.smartdental.model.general;

import android.app.Activity;

public class GetUrlByTag {
	private String username;
	
	public GetUrlByTag(String un) {
		// TODO Auto-generated constructor stub
		this.username = un;
	}
	
	public String GetUrl(int tag) {
		String base = "http://166.111.80.119/userinfo?user=" + username;
		if (tag == 0) {
			return (base + "&hide=0");
		}
		else if (tag == 1) {
			return (base + "&read=0&hide=0");
		}
		else if (tag == 2) {
			return (base + "&hide=1");
		}
		else {
			return (base + "&record=1&hide=0");
		}
	}
}
