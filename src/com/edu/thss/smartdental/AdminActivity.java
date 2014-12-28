/*
 * Author: Zhang Kai
*/

package com.edu.thss.smartdental;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;
import android.util.Log;
import android.database.Cursor;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.edu.thss.smartdental.RemoteDB.UserDBUtil;

public class AdminActivity extends FragmentActivity {

	RadioGroup radioGroup;
	UserDBUtil db = new UserDBUtil();
	private Notification notification;
	private NotificationManager nManager;
	private Notification.Builder nBuilder;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		radioGroup = (RadioGroup)findViewById(R.id.admin_tab);
		radioGroup.check(R.id.add_single);
		changeFragment(0);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.add_single: 
					changeFragment(0); 
					break;
				case R.id.add_many:
					changeFragment(1);
					break;
				}
			}} );
		nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nBuilder = new Notification.Builder(AdminActivity.this);
		nBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
		nBuilder.setTicker("来自SmartDental的消息");
		nBuilder.setContentTitle("SmartDental");
		nBuilder.setSmallIcon(R.drawable.ic_launcher);
	}
	
	private void changeFragment(int position){
		Fragment fragment  = null;
		switch (position){
		case 0: 
			fragment = new AddOneFragment();
			break;
		case 1: 
			fragment = new AddManyFragment();
			break;
		default:
			break;
		}
		if(fragment != null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.doctor_frame, fragment).commit();
		}
		else{
			Log.e("AdminActivity", "Error in creating fragment");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
		switch (resultCode) {
		case RESULT_OK:      
			Uri uri = data.getData();
			String path = getPath(this, uri);
			try {
				FileReader fileReader = new FileReader(path);
				BufferedReader reader = new BufferedReader(fileReader);
				String buf;
				int sum = 0, error = 0;
				while (true) {
					try {
						buf = reader.readLine();
						if (buf == null)
							break;
						try {
							String[] doctor = buf.split(",");
							if (!db.insertUser(doctor[0], doctor[1], "doctor").equals("true"))
								error++;
							sum++;
						} catch (Exception e) {
							nBuilder.setContentText("文件格式有误");
							notification = nBuilder.build();
							nManager.notify(0, notification);
							break;
						}
					} catch (IOException e) {
						nBuilder.setContentText("打开文件失败");
						notification = nBuilder.build();
						nManager.notify(0, notification);
						break;
					}
				}
				nBuilder.setContentText("导入了" + String.valueOf(sum) + "条记录，" + String.valueOf(error) + "个错误");
				notification = nBuilder.build();
				nManager.notify(1, notification);
			} catch (FileNotFoundException e) {
				nBuilder.setContentText("文件不存在");
				notification = nBuilder.build();
				nManager.notify(1, notification);
			}
			break;
	    }
		super.onActivityResult(requestCode, resultCode, data);
	}

	private static String getPath(Context context, Uri uri) {		 
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			cursor = context.getContentResolver().query(uri, projection,null, null, null);
			int column_index = cursor.getColumnIndexOrThrow("_data");
			if (cursor.moveToFirst())
				return cursor.getString(column_index);
		}
		else
			if ("file".equalsIgnoreCase(uri.getScheme()))
				return uri.getPath();
		return null;
	    }
}
