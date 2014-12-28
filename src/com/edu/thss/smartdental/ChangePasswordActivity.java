package com.edu.thss.smartdental;

import com.edu.thss.smartdental.RemoteDB.UserDBUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

	Button confirm_btn, cancel_btn;
	EditText old_password_edit, new_password_edit, repeat_new_password_edit;
	UserDBUtil db = new UserDBUtil();
	SharedPreferences preferences = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		preferences = getSharedPreferences("setting", MODE_PRIVATE);
		old_password_edit = (EditText)findViewById(R.id.old_password_edit);
		new_password_edit = (EditText)findViewById(R.id.new_password_edit);
		repeat_new_password_edit = (EditText)findViewById(R.id.repeat_new_password_edit);
		confirm_btn = (Button)findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(confirmListener);
		cancel_btn = (Button)findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(cancelListener);
	}
	
	private OnClickListener confirmListener = new OnClickListener() {
		public void onClick(View v) {
			String old_password = old_password_edit.getText().toString(), new_password = new_password_edit.getText().toString(), repeat_new_password = repeat_new_password_edit.getText().toString();
			if (!new_password.equals(repeat_new_password)) {
				Toast.makeText(ChangePasswordActivity.this, getString(R.string.new_password_not_same), Toast.LENGTH_LONG).show();
				return;
			}
			String t = db.setuserPassword(old_password, new_password, preferences.getString("username", ""));
			if (t.equals("true")) {
				Toast.makeText(ChangePasswordActivity.this, getString(R.string.change_password_success), Toast.LENGTH_LONG).show();
				finish();
			}
			else
				if (t.equals("wrong password"))
					Toast.makeText(ChangePasswordActivity.this, getString(R.string.old_password_wrong), Toast.LENGTH_LONG).show();
				else
					if (t.equals("fail to connect to Database"))
						Toast.makeText(ChangePasswordActivity.this, getString(R.string.message_link_fail), Toast.LENGTH_LONG).show();
					else
						Toast.makeText(ChangePasswordActivity.this, getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
		}
	};
	
	private OnClickListener cancelListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
}
