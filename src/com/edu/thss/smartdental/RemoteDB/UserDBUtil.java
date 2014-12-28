/*
 * AuthorBy:qiaocy
 * 
 */
package com.edu.thss.smartdental.RemoteDB;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDBUtil {
	private ArrayList<String> parametername = new ArrayList<String>();
	private ArrayList<String> parametervalue = new ArrayList<String>();
	private ArrayList<String> resultinfo = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	
	
	/**
	 * 获取圈子内全部患者
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllpatients(String DoctorName) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("doctorname");
		parametervalue.add(DoctorName);
		try{
			resultinfo = Soap.GetWebService("selectAllUserByDoctor", parametername, parametervalue);
		}
		catch(Exception e) {
		}

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("userid", "userid");
		tempHash.put("username", "username");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 2) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("userid", resultinfo.get(j));
			hashMap.put("username", resultinfo.get(j + 1));
			list.add(hashMap);
		}

		return list;
	}
	/**
	 * 获取全部医生
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getAllDoctors() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
			
		try{
			resultinfo = Soap.GetWebService("selectAllDoctors", parametername, parametervalue);
		}
		catch(Exception e) {
		}
			

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("doctorid", "doctorid");
		tempHash.put("doctorname", "doctorname");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 2) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("doctorid", resultinfo.get(j));
			hashMap.put("doctorname", resultinfo.get(j + 1));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 *  获得一个用户所有圈子医生列表
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> selectDoctorsByname(String userName) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("userName");
		parametervalue.add(userName);
			
		try{
			resultinfo = Soap.GetWebService("selectDoctorsByname",parametername, parametervalue);
		}
		catch(Exception e) {
		}
			

		HashMap<String, String> tempHash = new HashMap<String, String>();
		tempHash.put("doctorid", "doctorid");
		tempHash.put("doctorname", "doctorname");
		list.add(tempHash);
		
		for (int j = 0; j < resultinfo.size(); j += 2) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("doctorid", resultinfo.get(j));
			hashMap.put("doctorname", resultinfo.get(j + 1));
			list.add(hashMap);
		}

		return list;
	}
	
	/**
	 * 注册用户
	 * 
	 * @return
	 */
	public String insertUser(String username, String password,String identity) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametername.add("password");
		parametername.add("ident");
		parametervalue.add(username);
		parametervalue.add(password);
		parametervalue.add(identity);
		try{
			resultinfo = Soap.GetWebService("insertUser", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 登录
	 * if log in success return true
	 * else if user not exist return user does not exist
	 * else if wrong password return wrong password
	 * else return inner error
	 * @return
	 */
	public String login(String username, String password)
	{
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametername.add("password");
		parametervalue.add(username);
		parametervalue.add(password);
		try{
			resultinfo = Soap.GetWebService("login", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 删除一个用户
	 * 
	 * @return
	 */
	public String deleteUser(String username) {

		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("username");
		parametervalue.add(username);
		try{
			resultinfo = Soap.GetWebService("deleteUser", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 加入圈子
	 * 
	 * @return
	 */
	public String joinCircle(String username,String circlepassword,String doctorname){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametername.add("doctorpassword");
		parametername.add("doctorname");
		parametervalue.add(username);
		parametervalue.add(circlepassword);
		parametervalue.add(doctorname);
		try{
			resultinfo = Soap.GetWebService("joinDoctor", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 设置用户密码
	 * 
	 * @return
	 */
	public String setuserPassword(String oldpassword, String newpassword, String doctorname){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("oldpassword");
		parametername.add("newpassword");
		parametername.add("username");
		parametervalue.add(oldpassword);
		parametervalue.add(newpassword);
		parametervalue.add(doctorname);
		try{
			resultinfo = Soap.GetWebService("SetuserPassword", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 获取用户身份
	 * 
	 * @return
	 */
	public String getuseridentity(String username)
	{
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametervalue.add(username);
		try{
			resultinfo = Soap.GetWebService("Getuseridentity", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 获取圈子密码
	 * 
	 * @return
	 */
	public String getCirclePassword(String doctorname)
	{
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("doctorname");
		parametervalue.add(doctorname);
		try{
			resultinfo = Soap.GetWebService("GetCirclePassword", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	/**
	 * 设置圈子密码
	 * 
	 * @return
	 */
	public String setcirclePassword(String newpassword, String doctorname){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("newpassword");
		parametername.add("doctorname");
		parametervalue.add(newpassword);
		parametervalue.add(doctorname);
		try{
			resultinfo = Soap.GetWebService("SetcirclePassword", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	/**
	 * 踢出圈子
	 * 
	 * @return
	 */
	public String kickout(String username, String doctorname){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		parametername.add("username");
		parametername.add("doctorname");
		parametervalue.add(username);
		parametervalue.add(doctorname);
		try{
			resultinfo = Soap.GetWebService("kickout", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	
}
