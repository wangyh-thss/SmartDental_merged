package com.edu.thss.smartdental;


import com.edu.thss.smartdental.RemoteDB.CommentDBUtil;
import com.edu.thss.smartdental.RemoteDB.PostDBUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;


public class PostReplyActivity extends Activity {
	private ActionBar actionBar;
	private EditText edit_reply_content;
	private String post_id;
	private String username;
	private static PostDBUtil db = new PostDBUtil();
	private static CommentDBUtil conmmentDB = new CommentDBUtil();
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_post_reply);
		edit_reply_content = (EditText)findViewById(R.id.edit_reply_content);
		post_id = getIntent().getExtras().getString("postId");
		username = getIntent().getExtras().getString("username");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.post_menu, menu);
		return true;
	}
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.cancel_button:
			finish();
			break;
		case R.id.post_button:
			String result;
			if (edit_reply_content.getText().toString().equals("")) {
				result = "评论不能为空";
			}
			else {
				result = conmmentDB.insertComment(post_id, edit_reply_content.getText().toString(), username, "回帖", "1");
			}
			if (result.equals("true")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PostReplyActivity.this);
				builder.setMessage("发布成功")
					   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				builder.show();
			}
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(PostReplyActivity.this);
				if (result.equals("false")) result = "连不上服务器";
				builder.setMessage(result)
					   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});
				builder.show();
			};
		default:
			break;
		}
		return true;
	}
}
