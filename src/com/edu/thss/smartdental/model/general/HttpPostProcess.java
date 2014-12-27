package com.edu.thss.smartdental.model.general;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.edu.thss.smartdental.CallBackInterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpPostProcess {
	private Handler handler;
	private String result;
	private String url;
	private CallBackInterface call_back_function;
	private String caseID;
	private String para;
	private String val;
	
	public HttpPostProcess(Handler han, String res, String u, CallBackInterface cb, String cid, String p, String v) {
		// TODO Auto-generated constructor stub
		this.handler = han;
		this.result = res;
		this.url = u;
		this.call_back_function = cb;
		this.caseID = cid;
		this.para = p;
		this.val = v;
	}
	
	public void start() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle data = msg.getData();
		        result = data.getString("result");
		        call_back_function.CallBack(result);
			}
			
		};
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient();
				String postURL = url;
				HttpPost post = new HttpPost(postURL);
				String tString = "";
		        try {
		        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        	nameValuePairs.add(new BasicNameValuePair("caseid", caseID));
		        	nameValuePairs.add(new BasicNameValuePair(para, val));
		        	post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse responsePost = client.execute(post);
			        HttpEntity resEntityGet = responsePost.getEntity();  
			        if (resEntityGet != null) {  
			        	tString = EntityUtils.toString(resEntityGet);
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
		        Message message = new Message();
		        Bundle data = new Bundle();
		        data.putString("result", tString);
		        message.setData(data);
		        handler.sendMessage(message);
			}
		};
		new Thread(runnable).start();
	}
}
