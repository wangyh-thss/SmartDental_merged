package com.edu.thss.smartdental.RemoteDB;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentDBUtil {
	private ArrayList<String> parametername = new ArrayList<String>();
	private ArrayList<String> parametervalue = new ArrayList<String>();
	private ArrayList<String> resultinfo = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	/**
	 * 获取帖子内全部评论
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllComments(int PostId) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("PostId");
		parametervalue.add(Integer.toString(PostId));
			
		try{
			resultinfo = Soap.GetWebService("selectAllCommentsByPostId", parametername, parametervalue);
		}
		catch(Exception e) {
		}
			

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("commentusername", "commentusername");
		tempHash.put("commenttype", "commenttype");
		tempHash.put("commentcontent", "commentcontent");
		tempHash.put("replytouser", "replytouser");
		tempHash.put("time","time");
		tempHash.put("commentid","commentid");
		tempHash.put("postid","postid");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 7) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("commentusername", resultinfo.get(j));
			hashMap.put("commenttype", resultinfo.get(j + 1));
			hashMap.put("commentcontent", resultinfo.get(j + 2));
			hashMap.put("replytouser", resultinfo.get(j + 3));
			hashMap.put("time", resultinfo.get(j + 4));
			hashMap.put("commentid", resultinfo.get(j + 5));
			hashMap.put("postid", resultinfo.get(j + 6));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 * 获取某个帖子
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getCommentById(int CommentId) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("commentId");
		parametervalue.add(Integer.toString(CommentId));
			
		try{
			resultinfo = Soap.GetWebService("selectCommentById", parametername, parametervalue);
		}
		catch(Exception e) {
		}
			

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("commentusername", "commentusername");
		tempHash.put("commenttype", "commenttype");
		tempHash.put("commentcontent", "commentcontent");
		tempHash.put("replytouser", "replytouser");
		tempHash.put("time","time");
		tempHash.put("commentid","commentid");
		tempHash.put("postid","postid");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 7) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("commentusername", resultinfo.get(j));
			hashMap.put("commenttype", resultinfo.get(j + 1));
			hashMap.put("commentcontent", resultinfo.get(j + 2));
			hashMap.put("replytouser", resultinfo.get(j + 3));
			hashMap.put("time", resultinfo.get(j + 4));
			hashMap.put("commentid", resultinfo.get(j + 5));
			hashMap.put("postid", resultinfo.get(j + 6));
			list.add(hashMap);
		}

		return list;
	}
	/**
	 * 新增评论
	 * 
	 * @return
	 */
	public String insertComment(String postid, String commentContent, String username, String CommentType, String ReplyUserName) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("postId");
		parametername.add("commentContent");
		parametername.add("username");
		parametername.add("CommentType");
		parametername.add("ReplyUserName");
		parametervalue.add(postid);
		parametervalue.add(commentContent);
		parametervalue.add(username);
		parametervalue.add(CommentType);	
		parametervalue.add(ReplyUserName);
		try{
			resultinfo = Soap.GetWebService("insertComment", parametername, parametervalue);
		}
		catch(Exception e) {
		}	
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 删除评论
	 * 
	 * @return
	 */
	public String deleteComment(String commentId) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("id");
		parametervalue.add(commentId);
		try{
			resultinfo = Soap.GetWebService("deleteComment", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
}
