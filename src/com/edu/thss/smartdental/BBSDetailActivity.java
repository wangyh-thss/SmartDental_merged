package com.edu.thss.smartdental;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.edu.thss.smartdental.RemoteDB.CommentDBUtil;
import com.edu.thss.smartdental.RemoteDB.PostDBUtil;
import com.edu.thss.smartdental.adapter.BBSDetailAdapter;
import com.edu.thss.smartdental.adapter.CommentAdapter;
import com.edu.thss.smartdental.model.BBSDetail;
import com.edu.thss.smartdental.model.CommentElement;

public class BBSDetailActivity extends Activity {
	private ListView list2;
	private ListView list1;
	private ArrayList<BBSDetail> posts;
	private ArrayList<CommentElement> posts1;
	private BBSDetailAdapter bbsAdapter;
	private CommentAdapter commentAdapter;
	private Context context;
	private BBSDetailActivity context1;
	private String post_id;
	private SharedPreferences preferences = null;
	private String author, time, content, title, localUser;
	


	public static final int RESULT_CODE = 1; // ·µ»ØÂë

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bbs_detail); 

		preferences = this.getSharedPreferences("setting",
				Activity.MODE_PRIVATE);

		list2 = (ListView) findViewById(R.id.listView2);
		initPosts();
		context = this.getApplicationContext();
		context1 = this;
		bbsAdapter = new BBSDetailAdapter(posts, context, context1);
		list2.setAdapter(bbsAdapter);

		list1 = (ListView) findViewById(R.id.listView1);
		initPosts1();
		commentAdapter = new CommentAdapter(posts1, context,
				preferences.getString("username", ""));
		list1.setAdapter(commentAdapter);

		SharedPreferences pre = getSharedPreferences("setting", MODE_PRIVATE);
		localUser = pre.getString("username", "");
	}

	private void initPosts() {
		posts = new ArrayList<BBSDetail>();
		PostDBUtil db = new PostDBUtil();
		post_id = getIntent().getExtras().getString("postId");
		
		List<HashMap<String, String>> postFromDB = db.selectPostById(Integer.valueOf(post_id));
		author = postFromDB.get(1).get("author");
		time = postFromDB.get(1).get("time");
		content = postFromDB.get(1).get("postcontent");;
		title = postFromDB.get(1).get("postname");

		BBSDetail i = new BBSDetail(title, content, time, author);
		posts.add(i);
	}

	private void initPosts1() {
		posts1 = new ArrayList<CommentElement>();
		
		CommentDBUtil db = new CommentDBUtil();
		List<HashMap<String, String>> str = db.getAllComments(Integer.parseInt(post_id));
		for(int i = 1; i < str.size(); i++){
			String author = str.get(i).get("commentusername");
			String time = str.get(i).get("time");
			String content = str.get(i).get("commentcontent");
			String commentId = str.get(i).get("commentid");
			String type = str.get(i).get("commenttype");
			String toName=str.get(i).get("replytouser");
			String userName = preferences.getString("username", "");
			CommentElement element = new CommentElement(content, time, author, Integer.parseInt(commentId),Integer.parseInt(post_id),type,toName,userName);
			
			posts1.add(element);
		}
	}
	
	public void beginReply(){
		Intent intent = new Intent();
		intent.setClass(context, PostReplyActivity.class);
		intent.putExtra("postId", post_id);
		intent.putExtra("username", preferences.getString("username", ""));
		startActivity(intent);
	}
	
	public boolean isLocalUser(){
		if(author.equals(localUser))
			return true;
		else {
			return false;
		}
	}
	
	public boolean postCollected(){
		PostDBUtil dbUtil = new PostDBUtil();
		List<HashMap<String, String>> collectedList = dbUtil.selectcollectPostid(localUser);
		
		for(int i = 1; i < collectedList.size(); i++){
			if(collectedList.get(i).get("postid").equals(post_id)){
				return true;
			}
		}
		return false;
	}
	
	public String getPostId(){
		return this.post_id;
	}

	public void collectPost(){
		PostDBUtil dbUtil = new PostDBUtil();
		dbUtil.collectpost(localUser,Integer.parseInt(post_id));
		
	}
	public void cancelCollectPost(){
		PostDBUtil dbUtil = new PostDBUtil();
		dbUtil.deletecollectpost(localUser, Integer.parseInt(post_id));
	}
	
	public boolean collected(String content){
		boolean res = true;
		if(content.equals(getString(R.string.collect))){
			res = false;
		}
		return res;
	}
	
	public void changeState(Button button, boolean now_state) {
		if(now_state == false){
			button.setText(getString(R.string.cancellation));
			return;
		}
		button.setText(getString(R.string.collect));
	}
	
	public void markDelete(){
		SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("justDeleted", true);
		editor.commit();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//commentAdapter.notifyDataSetChanged();
		SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
		boolean justReplied = preferences.getBoolean("justReplied", true);
		if(justReplied){
			//setContentView(R.layout.activity_bbs_detail);
			initPosts1();
			commentAdapter.setList(posts1);
			commentAdapter.notifyDataSetChanged();
			Editor editor = preferences.edit();
			editor.putBoolean("justReplied", false);
			editor.commit();
			
		}
		super.onResume();
	}
}
