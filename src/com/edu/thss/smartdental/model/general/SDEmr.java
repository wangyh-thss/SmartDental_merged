package com.edu.thss.smartdental.model.general;

public class SDEmr {
	public String name;
	public String createTime;
	public String description;
	public int type; //0,1,2
	public String contentPath;
	public boolean isMark;
	public boolean isRead;
	public boolean isHidden;
	/*
	public void getContent(){
		String fileName = "yan.txt"; //文件名字

		String res=""; 
		InputStream in = getResources().getAssets().open(fileName);

		   // \Test\assets\yan.txt这里有这样的文件存在

		int length = in.available();         

		byte [] buffer = new byte[length];        

		in.read(buffer);            

		res = EncodingUtils.getString(buffer, "UTF-8");   
	}
    */	

}
