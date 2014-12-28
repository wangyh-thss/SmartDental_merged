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
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.edu.thss.smartdental.RemoteDB.UserDBUtil;

public class LoginActivity extends Activity {

	Button login_btn, register_btn;
	EditText username, password;
	UserDBUtil db = new UserDBUtil();
	SharedPreferences preferences = null;
	Editor editor = null;
	ProgressDialog pd;
	
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
            .detectDiskReads()  
            .detectDiskWrites()  
            .detectNetwork()  
            .penaltyLog()  
            .build());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		preferences = getSharedPreferences("setting", MODE_PRIVATE);
		editor = preferences.edit();
		username = (EditText)findViewById(R.id.username_edit);
		password = (EditText)findViewById(R.id.password_edit);
		username.setText(preferences.getString("username", ""));
		password.setText(preferences.getString("password", ""));
		login_btn = (Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(loginListener);
		register_btn = (Button)findViewById(R.id.register_btn);
		register_btn.setOnClickListener(registerListener);
	}
	
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) { 
            switch (msg.what) {
            case 0:
            	pd.dismiss();
            	break;
            case 1:
            	Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_LONG).show();
            	break;
            case 2:
            	Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
            	break;
            case 3:
            	Toast.makeText(LoginActivity.this, "连不上服务器", Toast.LENGTH_LONG).show();
            	break;
            case 4:
            	Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_LONG).show();
            	break;
            default:
            	break;	
            }
        }  
    };
    
	private class loginThread implements Runnable{
		@Override
		public void run() {
			Intent intent = new Intent();
			Message message1 = new Message();
			Message message2 = new Message();
			if (username.getText().toString().equals(getString(R.string.admin_username)))
				if (password.getText().toString().equals(getString(R.string.admin_password))) {
					editor.putString("username", username.getText().toString());
					editor.putString("password", password.getText().toString());
					editor.commit();
					intent.setClass(LoginActivity.this, AdminActivity.class);
				}
				else {
					message1.what = 0;
					handler.sendMessage(message1);
					message2.what = 2;
					handler.sendMessage(message2);
					return;
				}
			else {
				String t = db.login(username.getText().toString(), password.getText().toString());
				message1.what = 0;
				handler.sendMessage(message1);
				if (t.matches("\\d+")) {
					editor.putInt("userid", Integer.parseInt(t));
					editor.putString("username", username.getText().toString());
					editor.putString("password", password.getText().toString());
					editor.commit();
					intent.setClass(LoginActivity.this, MainActivity.class);
				}
				else {
					if (t.equals("user does not exist"))
						message2.what = 1;
					else
						if (t.equals("wrong password"))
							message2.what = 2;
						else
							if (t.equals("fail to connect to Database"))
								message2.what = 3;
							else
								message2.what = 4;
					handler.sendMessage(message2);
					return;
				}
			}
			startActivity(intent);
			finish();
		}
	};
	
	private OnClickListener loginListener = new OnClickListener() {
		public void onClick(View v) {
			pd = ProgressDialog.show(LoginActivity.this, "", "登录中");
	        new Thread(new loginThread()).start();
		}
	};
	
	private OnClickListener registerListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
		}
	};
}