package com.edu.thss.smartdental.model.general;

public class GetUrlByTag {
	public String GetUrl(int tag) {
		String base = "http://166.111.80.119/userinfo?user=baoye";
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
