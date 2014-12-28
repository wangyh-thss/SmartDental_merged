/*
 * Author: Zhang Kai
*/

package com.edu.thss.smartdental;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.edu.thss.smartdental.RemoteDB.UserDBUtil;

public class RegisterActivity extends Activity {

	Button register_btn, login_btn;
	EditText username_edit, password_edit, repeat_password_edit;
	UserDBUtil db = new UserDBUtil();
	SharedPreferences preferences = null;
	Editor editor = null;
	ProgressDialog pd;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		preferences = getSharedPreferences("setting", MODE_PRIVATE);
		editor = preferences.edit();
		username_edit = (EditText)findViewById(R.id.username_edit);
		password_edit = (EditText)findViewById(R.id.password_edit);
		repeat_password_edit = (EditText)findViewById(R.id.repeat_password_edit);
		register_btn = (Button)findViewById(R.id.register_btn);
		register_btn.setOnClickListener(registerListener);
		login_btn = (Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(loginListener);
	}
	
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) { 
            switch (msg.what) {
            case 0:
            	pd.dismiss();
            	break;
            case 1:
            	Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
            	break;
            case 2:
            	Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
            	break;
            case 3:
            	Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
            	break;
            case 4:
            	Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
            	break;
            case 5:
            	Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            	break;
            case 6:
            	Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
            	break;
            case 7:
            	Toast.makeText(RegisterActivity.this, "连不上服务器", Toast.LENGTH_LONG).show();
            	break;
            case 8:
            	Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_LONG).show();
            	break;
            default:
            	break;	
            }
        }  
    };
    
	private class registerThread implements Runnable{
		@Override
		public void run() {
			Message message1 = new Message();
			Message message2 = new Message();
			String username = username_edit.getText().toString(), password = password_edit.getText().toString(), repeat_password = repeat_password_edit.getText().toString();
			if (username.equals("")) {
				message1.what = 0;
				handler.sendMessage(message1);
				message2.what = 1;
				handler.sendMessage(message2);
				return;
			}
			if (password.equals("")) {
				message1.what = 0;
				handler.sendMessage(message1);
				message2.what = 2;
				handler.sendMessage(message2);
				return;
			}
			if (!repeat_password.equals(password)) {
				message1.what = 0;
				handler.sendMessage(message1);
				message2.what = 3;
				handler.sendMessage(message2);
				return;
			}
			if (username.equals(getString(R.string.admin_username))) {
				message1.what = 0;
				handler.sendMessage(message1);
				message2.what = 4;
				handler.sendMessage(message2);
				return;
			}
			String t = db.insertUser(username, password, "patient");
			message1.what = 0;
			handler.sendMessage(message1);
			if (t.equals("true")) {
				t = db.login(username, password);
				editor.putInt("userid", Integer.parseInt(t));
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();
				message2.what = 5;
				handler.sendMessage(message2);
			}
			else {
				if (t.equals("username exists")) {
					message2.what = 6;
					handler.sendMessage(message2);
				}
				else
					if (t.equals("fail to connect to Database")) {
						message2.what = 7;
						handler.sendMessage(message2);
					}
					else {
						message2.what = 8;
						handler.sendMessage(message2);
					}
				return;
			}
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, JoinCircleActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private OnClickListener registerListener = new OnClickListener() {
		public void onClick(View v) {
			pd = ProgressDialog.show(RegisterActivity.this, "", "注册中");
	        new Thread(new registerThread()).start();
		}
	};
	
	private OnClickListener loginListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	};
}
