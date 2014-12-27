package com.edu.thss.smartdental.model.general;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.edu.thss.smartdental.CallBackInterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpGetProcess {
	private Handler handler;
	private String result;
	private String url;
	private CallBackInterface call_back_function;
	
	public HttpGetProcess(Handler h, String r, String u, CallBackInterface cb) {
		// TODO Auto-generated constructor stub
		this.handler = h;
		this.result = r;
		this.url = u;
		this.call_back_function = cb;
	}
	
	public void start() {
		handler = new Handler(){

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
		        String getURL = url;
		        HttpGet get = new HttpGet(getURL);
		        String tString = "";
		        try {
					HttpResponse responseGet = client.execute(get);  
			        HttpEntity resEntityGet = responseGet.getEntity();  
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
