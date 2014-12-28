package com.edu.thss.smartdental;

import java.util.Date;

import com.edu.thss.smartdental.model.PostElement;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PostBBSActivity extends Activity {
	private ActionBar actionBar;
	private EditText edit_bbs_content;
	private EditText edit_bbs_title;
	private Spinner edit_tab_spinner;
	private ArrayAdapter adapter;
	private SharedPreferences preferences = null;
	private Editor editor = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		preferences = getSharedPreferences("setting", Activity.MODE_PRIVATE);
		editor = preferences.edit();
		setContentView(R.layout.activity_post_bbs);
		edit_bbs_content = (EditText)findViewById(R.id.edit_bbs_content);
		edit_tab_spinner = (Spinner)findViewById(R.id.edit_tab_spinner);
		adapter = ArrayAdapter.createFromResource(PostBBSActivity.this, R.array.tab_names, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_tab_spinner.setAdapter(adapter);
		edit_tab_spinner.setVisibility(View.VISIBLE);
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
			PostBBS();
		default:
			break;
		}
		return true;
	}
	
	private void PostBBS() {
		String title = edit_bbs_title.getText().toString();
		String content = edit_bbs_content.getText().toString();
		String tabName = edit_tab_spinner.getSelectedItem().toString();
		if (title.equals("")) {
			Toast.makeText(PostBBSActivity.this, "请填写帖子标题", Toast.LENGTH_LONG).show();
			return;
		}
		if (content.equals("")) {
			Toast.makeText(PostBBSActivity.this, "请填写帖子内容", Toast.LENGTH_LONG).show();
			return;
		}
		PostElement postElement = new PostElement(title, content, preferences.getString("username", ""), tabName, preferences.getString("current_circle_id", ""), new Date(), false);
		AlertDialog.Builder builder = new AlertDialog.Builder(PostBBSActivity.this);
		postElement.insertToDB();
		builder.setMessage("发布成功")
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		builder.show();
	}
}