package com.edu.thss.smartdental;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.edu.thss.smartdental.RemoteDB.CommentDBUtil;
import com.edu.thss.smartdental.RemoteDB.NewsDBUtil;
import com.edu.thss.smartdental.RemoteDB.PostDBUtil;

import android.app.Activity; 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle; 
import android.view.MotionEvent; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout; 
import android.widget.RelativeLayout;
import android.widget.Toast; 
public class InputActivity extends Activity { 
	private RelativeLayout layout; 
	private LinearLayout input_layout; 
	private EditText edit;
	private Button post_reply_button;
	private String comment_id;
	private String post_id;
	private String toName;
	private String type;
	private String userName;
	private CommentDBUtil commentDB;
	private NewsDBUtil newsDB;
	private PostDBUtil postDB;
	List<HashMap<String, String>> comment;
	private SharedPreferences preferences = null;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		newsDB = new NewsDBUtil();
		commentDB = new CommentDBUtil();
		
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_input); 
		//layout=(RelativeLayout)findViewById(R.id.exit_layout); 
		//input_layout = (LinearLayout)findViewById(R.id.input_layout);
		comment_id=getIntent().getExtras().getString("commentId");
		post_id = getIntent().getExtras().getString("postId");
		type="comment";
		preferences = this.getSharedPreferences("setting",
				Activity.MODE_PRIVATE);
		userName=preferences.getString("username", "");
		
		if(!comment_id.equals("")){
			comment = commentDB.getCommentById(Integer.parseInt(comment_id));
			type="reply";
			toName = comment.get(1).get("commentusername");
		}else{
			postDB = new PostDBUtil();
			toName = postDB.selectPostById(Integer.parseInt(post_id)).get(1).get("author");
		}
		
		edit=(EditText)findViewById(R.id.edit_reply);
		edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        
        post_reply_button = (Button) findViewById(R.id.post_reply);
        //InputMethodManager inputManager =(InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.showSoftInput(edit, 0);
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run() 
            {
                InputMethodManager inputManager =
                    (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit, 0);
            }
        },  

            300);
            
        
		post_reply_button.setOnClickListener(new OnClickListener() { 
			@Override 
			public void onClick(View v) { 
				// TODO Auto-generated method stub 
				
				//commentDB.insertComment(post_id, commentContent, String username, String CommentType, String ReplyUserName);
				
				
				
				String result;
				String newsResult;
				if (edit.getText().toString().equals("")) {
					result = getResources().getString(R.string.message_null_reply);
					newsResult ="false";
				}
				else {
					result = commentDB.insertComment(post_id, edit.getText().toString(), userName, type, toName);
					newsResult = newsDB.insertNews(userName, toName, edit.getText().toString(), Integer.parseInt(post_id));
				}
				if ((result.equals("true")) && (newsResult.equals("true"))) {
					AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
					builder.setMessage(getResources().getString(R.string.message_publish_succeed))
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
							}
						});
					SharedPreferences preferences = getSharedPreferences("setting", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putBoolean("justReplied", true);
					editor.commit();
					builder.show();
				}
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
					result = getResources().getString(R.string.message_link_fail);
					builder.setMessage(result)
						   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						});
					builder.show();
				};
				
				
				//Toast.makeText(getApplicationContext(), toName, 
				//		Toast.LENGTH_SHORT).show(); 
				
			} 
		}); 
		
	} 
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){ 
		finish(); 
		return true; 
	} 

	
} 
