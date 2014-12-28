package com.edu.thss.smartdental.RemoteDB;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsDBUtil {
	private ArrayList<String> parametername = new ArrayList<String>();
	private ArrayList<String> parametervalue = new ArrayList<String>();
	private ArrayList<String> resultinfo = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	/**
	 * 鑾峰緱鐢ㄦ埛鍏ㄩ儴鏂版秷鎭�
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectAllUnreadNewsByUsername(String username) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametervalue.add(username);
		try{			
			resultinfo = Soap.GetWebService("selectAllUnreadNewsByUsername", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("newsId", "newsId");
		tempHash.put("username", "username");
		tempHash.put("content", "content");
		tempHash.put("replytouser", "replytouser");
		tempHash.put("postId", "postId");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 5) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("newsId", resultinfo.get(j));
			hashMap.put("username", resultinfo.get(j + 1));
			hashMap.put("content", resultinfo.get(j + 2));
			hashMap.put("replytouser", resultinfo.get(j + 3));
			hashMap.put("postId", resultinfo.get(j + 4));
			list.add(hashMap);
		}

		return list;
	}
	/**
	 * 鑾峰彇鐢ㄦ埛鍏ㄩ儴宸茶娑堟伅
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectAllReadNewsByUsername(String username) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametervalue.add(username);
		try{
			
			resultinfo = Soap.GetWebService("selectAllReadNewsByUsername", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("newsId", "newsId");
		tempHash.put("username", "username");
		tempHash.put("content", "content");
		tempHash.put("replytouser", "replytouser");
		tempHash.put("postId", "postId");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 5) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("newsId", resultinfo.get(j));
			hashMap.put("username", resultinfo.get(j + 1));
			hashMap.put("content", resultinfo.get(j + 2));
			hashMap.put("replytouser", resultinfo.get(j + 3));
			hashMap.put("postId", resultinfo.get(j + 4));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 * 鏂板缓涓�鏉℃秷鎭�
	 * 
	 * @return
	 */
	public String insertNews(String username, String replytouser,String newscontent,int postId) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametername.add("replytouser");
		parametername.add("newscontent");
		parametername.add("postId");
		parametervalue.add(username);
		parametervalue.add(replytouser);
		parametervalue.add(newscontent);
		parametervalue.add(Integer.toString(postId));
		try{
			resultinfo = Soap.GetWebService("insertNews", parametername, parametervalue);
		}
		catch(Exception e) {
		}	
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 鏇存柊娑堟伅涓哄凡璇�
	 * 
	 * @return
	 */
	public String haveread(String newsid) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("newsid");
		parametervalue.add(newsid);
		try{
			resultinfo = Soap.GetWebService("haveread", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
}
