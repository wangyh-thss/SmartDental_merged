package com.edu.thss.smartdental.RemoteDB;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadPicture {
	private ArrayList<String> parametername = new ArrayList<String>();
	private ArrayList<String> parametervalue = new ArrayList<String>();
	private ArrayList<String> resultinfo = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();
	
	//上传图片
	public String uploadpicture(String picbuff,String picname,String username){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("picbuff");
		parametername.add("picname");
		parametername.add("username");
		parametervalue.add(picbuff);
		parametervalue.add(picname);
		parametervalue.add(username);
		try{
			resultinfo = Soap.GetWebService("UploadFile", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
	
	//下载图片
	public String downloadpicture(String picname){
		parametername.clear();
		parametervalue.clear();
		resultinfo.clear();
		
		parametername.add("filename");
		parametervalue.add(picname);
		try{
			resultinfo = Soap.GetWebService("DownloadFile", parametername, parametervalue);
		}
		catch(Exception e) {
		}
		if(resultinfo.size() == 0){
			return "fail to connect to Database";
		}
		return resultinfo.get(0);
	}
}
