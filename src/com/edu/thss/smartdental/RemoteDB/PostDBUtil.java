/*
 * AuthorBy:qiaocy
 * 
 */
package com.edu.thss.smartdental.RemoteDB;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDBUtil {
	private ArrayList<String> parametername = new ArrayList<String>();
	private ArrayList<String> parametervalue = new ArrayList<String>();
	private ArrayList<String> resultinfo = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	/**
	 * 获取圈子内全部帖子信息
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllPostInfo(int id) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("id");
		parametervalue.add(Integer.toString(id));
		try{
			
			resultinfo = Soap.GetWebService("selectAllPostByDoctor", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("postname", "postname");
		tempHash.put("postcontent", "postcontent");
		tempHash.put("time", "time");
		tempHash.put("author", "author");
		tempHash.put("PostId", "PostId");
		tempHash.put("tag", "tag");
		tempHash.put("doctorid", "doctorid");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 7) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("postname", resultinfo.get(j));
			hashMap.put("postcontent", resultinfo.get(j + 1));
			hashMap.put("time", resultinfo.get(j + 2));
			hashMap.put("author", resultinfo.get(j + 3));
			hashMap.put("PostId", resultinfo.get(j + 4));
			hashMap.put("tag", resultinfo.get(j + 5));
			hashMap.put("doctorid", resultinfo.get(j + 6));
			list.add(hashMap);
		}

		return list;
	}

	/**
	 * 通过id获取帖子
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectPostById(int PostId) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("PostId");
		parametervalue.add(Integer.toString(PostId));
		try{
			
			resultinfo = Soap.GetWebService("selectPostById", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("postname", "postname");
		tempHash.put("postcontent", "postcontent");
		tempHash.put("time", "time");
		tempHash.put("author", "author");
		tempHash.put("PostId", "PostId");
		tempHash.put("tag", "tag");
		tempHash.put("doctorid", "doctorid");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 7) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("postname", resultinfo.get(j));
			hashMap.put("postcontent", resultinfo.get(j + 1));
			hashMap.put("time", resultinfo.get(j + 2));
			hashMap.put("author", resultinfo.get(j + 3));
			hashMap.put("PostId", resultinfo.get(j + 4));
			hashMap.put("tag", resultinfo.get(j + 5));
			hashMap.put("doctorid", resultinfo.get(j + 6));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 * 获取一个用户的全部帖子
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectAllPostByUser(String username) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametervalue.add(username);
		try{
			
			resultinfo = Soap.GetWebService("selectAllPostByUser", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("postname", "postname");
		tempHash.put("postcontent", "postcontent");
		tempHash.put("time", "time");
		tempHash.put("author", "author");
		tempHash.put("PostId", "PostId");
		tempHash.put("tag", "tag");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 6) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("postname", resultinfo.get(j));
			hashMap.put("postcontent", resultinfo.get(j + 1));
			hashMap.put("time", resultinfo.get(j + 2));
			hashMap.put("author", resultinfo.get(j + 3));
			hashMap.put("PostId", resultinfo.get(j + 4));
			hashMap.put("tag", resultinfo.get(j + 5));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 * 获得用户收藏帖子列表
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectcollectPostid(String userName) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("userName");
		parametervalue.add(userName);
		try{
			
			resultinfo = Soap.GetWebService("selectcollectPostid", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("postid", "postname");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 1) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("postid", resultinfo.get(j));
			list.add(hashMap);
		}

		return list;
	}
	/**
	 * 收藏帖子
	 * 
	 * @return
	 */
	public String collectpost(String username,int PostId){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametername.add("PostId");
		parametervalue.add(username);
		parametervalue.add(Integer.toString(PostId));
		try{
			resultinfo = Soap.GetWebService("collectpost", parametername, parametervalue);
		}
		catch(Exception e) {
		}		
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 取消收藏
	 * 
	 * @return
	 */
	public String deletecollectpost(String username,int PostId){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametername.add("PostId");
		parametervalue.add(username);
		parametervalue.add(Integer.toString(PostId));
		try{
			resultinfo = Soap.GetWebService("deletecollectpost", parametername, parametervalue);
		}
		catch(Exception e) {
		}		
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 发新帖子
	 * 
	 * @return
	 */
	public String insertPost(String PostName, String postContent,String username, String doctorid, String tag) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("PostName");
		parametername.add("postContent");
		parametername.add("username");
		parametername.add("doctorid");
		parametername.add("tag");
		parametervalue.add(PostName);
		parametervalue.add(postContent);
		parametervalue.add(username);
		parametervalue.add(doctorid);
		parametervalue.add(tag);
		try{
			resultinfo = Soap.GetWebService("insertPost", parametername, parametervalue);
		}
		catch(Exception e) {
		}		
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 删除帖子
	 * 
	 * @return
	 */
	public String deletePost(int postId) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("postId");
		parametervalue.add(Integer.toString(postId));
		try{
			resultinfo = Soap.GetWebService("deletePost", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
}
