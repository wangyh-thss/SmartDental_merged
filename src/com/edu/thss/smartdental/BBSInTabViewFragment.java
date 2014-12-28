/*
 * author: Chen Minghai
 */
package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import com.edu.thss.smartdental.RemoteDB.PostDBUtil;
import com.edu.thss.smartdental.adapter.BBSListAdapter;
import com.edu.thss.smartdental.adapter.ImgListAdapter;
import com.edu.thss.smartdental.model.BBSElement;
import com.edu.thss.smartdental.model.ImageElement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class BBSInTabViewFragment extends Fragment {
	private BBSListAdapter bbsAdapter;
	private EditText editText;
	private ListView list;
	private ArrayList<BBSElement> posts=new ArrayList<BBSElement>();
	private Context context;
	private int UserId;
	
	private Spinner view_spinner;
	private ArrayAdapter tagAdapter; 
	private ProgressDialog pd;
	
	public BBSInTabViewFragment(int id) {
		UserId = id;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bbs_in_view, container, false);
		view_spinner = (Spinner)rootView.findViewById(R.id.bbs_view_spinner);
		tagAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.view_tag_names, android.R.layout.simple_spinner_item);
		tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		view_spinner.setAdapter(tagAdapter);
		view_spinner.setVisibility(View.VISIBLE);
		view_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view,
		            int position, long id) {
		        String str=parent.getItemAtPosition(position).toString();
		        
		        pd = ProgressDialog.show(parent.getContext(), "", getActivity().getResources().getString(R.string.loading));
		        new Thread(new RunThread(str)).start();  
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // TODO Auto-generated method stub
		    }
		});
		
		
    	
		editText = (EditText)rootView.findViewById(R.id.bbs_searchbox);
		editText.addTextChangedListener(filterTextWatcher);
		list = (ListView)rootView.findViewById(R.id.bbs_list);
		context = this.getActivity().getApplicationContext();
		bbsAdapter = new BBSListAdapter(posts,this.getActivity().getApplicationContext());
		list.setAdapter(bbsAdapter);
		list.setDivider(null);
		      
		list.setOnItemClickListener(new OnPostItemClickListener(context));
		
	
		view_spinner.setSelection(5);
		return rootView;
	}
	
	private class RunThread implements Runnable{
		String str;
		public RunThread(String s){
			str = s;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			initPosts(str);
	        handler.sendEmptyMessage(0);
		}
		
	};
	
	Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            pd.dismiss(); 
        	refreshPosts();
        }  
    };  
	
	private class OnPostItemClickListener implements OnItemClickListener{
		Context context;
		public OnPostItemClickListener(Context c){
			context = c;
		}
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                 long arg3) {
            // if(list.get(position).equals("LinearLayout"))
                 Intent intent = new Intent();
                 intent.setClass(context,BBSDetailActivity.class);
                 intent.putExtra("postId", posts.get(position).id);
                 startActivity(intent);       
                 
         }
	};
	
	private TextWatcher filterTextWatcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			bbsAdapter.getFilter().filter(s);	
		}
	};
	
	private void refreshPosts(){
		this.bbsAdapter.notifyDataSetChanged();
	}
	
	private void initPosts(String tag){
		posts.clear();
		String circle_id_st = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE).getString("current_circle_id", "");
		int circle_id;
		if (circle_id_st ==""){
			return;
		}else{
			circle_id = Integer.parseInt(circle_id_st);
		}
		
		PostDBUtil db = new PostDBUtil();
		BBSElement post;
		
		String[] tag_name = getResources().getStringArray(R.array.view_tag_names);
		
		if (tag.equals(tag_name[4])){
			//collection
			String userName = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE).getString("username", "");
			List<HashMap<String, String>> idList = db.selectcollectPostid(userName);
			for (int i = 1; i < idList.size(); i++){
				String id=idList.get(i).get("postid");
				
				List<HashMap<String, String>> postInfo = db.selectPostById(Integer.parseInt(id));
				String doctorid=postInfo.get(1).get("doctorid");
				if (!doctorid.equals(circle_id_st))
					continue;
				String s1=postInfo.get(1).get("postname");
				String s2=postInfo.get(1).get("postcontent");
				String s3 = postInfo.get(1).get("time");
				String s4 = postInfo.get(1).get("author");
				post = new BBSElement(s1,s2,s3,s4,true,true, id);
				posts.add(post);
			}
			return;
		}
		
		List<HashMap<String, String>> PostList  = db.getAllPostInfo(circle_id);
		for (int i = 1; i < PostList.size(); i++){
			if (tag.equals(tag_name[5]) || tag.equals(PostList.get(i).get("tag"))){
				String s1=PostList.get(i).get("postname");
				String s2=PostList.get(i).get("postcontent");
				String s3 = PostList.get(i).get("time");
				String s4 = PostList.get(i).get("author");
				String postId = PostList.get(i).get("PostId");
				post = new BBSElement(s1,s2,s3,s4,true,true, postId);
				posts.add(post);
			}
		}
		
		
	}
	
}
