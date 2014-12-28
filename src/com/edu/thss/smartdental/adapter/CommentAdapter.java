/*
 * 娴ｆ粏锟藉拑绱伴悳瀣暏閻愶拷
 * 閺冦儲婀￠敍锟�2014楠烇拷12閺堬拷11閺冿拷
 */
package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.RemoteDB.CommentDBUtil;
import com.edu.thss.smartdental.model.CommentElement;
import com.edu.thss.smartdental.InputActivity;
import com.edu.thss.smartdental.LoginActivity;
import com.edu.thss.smartdental.PostReplyActivity;
import com.edu.thss.smartdental.R;

import android.content.Context;
import android.content.Intent;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private ArrayList<CommentElement> list;
	private Context context;
	private buttonViewHolder holder;
	private String username;
	private CommentDBUtil commentDB;
	private boolean isDeletable;
	private class buttonViewHolder {
		Button delete;
		Button reply;
	}
	public CommentAdapter(ArrayList<CommentElement> list, Context context, String username) {
		// TODO Auto-generated constructor stub
		commentDB = new CommentDBUtil();
		this.list = list;
		this.context = context;
		this.username = username;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int postion) {
		// TODO Auto-generated method stub
		return postion;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_detail_item, null);
		}
		CommentElement comment = list.get(position);
		
		TextView content = (TextView)convertView.findViewById(R.id.comment_item_content);
		TextView time = (TextView)convertView.findViewById(R.id.comment_item_time);
		TextView author = (TextView)convertView.findViewById(R.id.comment_item_author);
		
		content.setText(comment.content);
		time.setText(comment.time.substring(0, 16));
		author.setText(comment.author);
		
		holder = new buttonViewHolder();
		holder.delete = (Button)convertView.findViewById(R.id.comment_item_delete);
		holder.reply = (Button)convertView.findViewById(R.id.comment_item_reply);
		/*if (author.equals(this.username)) {
			holder.delete.setVisibility(View.VISIBLE);
		}
		else {
			holder.delete.setVisibility(View.INVISIBLE);
		}*/
		isDeletable = list.get(position).isDeletable;
		if(isDeletable){
			holder.delete.setVisibility(View.VISIBLE);
		}else{
			holder.delete.setVisibility(View.INVISIBLE);
		}
		holder.delete.setOnClickListener(new DeleteButtonListener(position));
		holder.reply.setOnClickListener(new ReplyButtonListener(position));
		return convertView;
	}

	class DeleteButtonListener implements OnClickListener {
		private int itemPosition;

		public DeleteButtonListener(int position) {
			this.itemPosition = position;
		}
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int viewId = view.getId();
			if (viewId == holder.delete.getId()) {
				int commentId = list.get(itemPosition).id;
				commentDB.deleteComment(String.valueOf(commentId));
				list.remove(itemPosition);
				notifyDataSetChanged();
			}
		}
		
	}
	class ReplyButtonListener implements OnClickListener {
		private int itemPosition;

		public ReplyButtonListener(int position) {
			this.itemPosition = position;
		}
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int viewId = view.getId();
			if (viewId == holder.reply.getId()) {
				int commentId = list.get(itemPosition).id;
				int postId = list.get(itemPosition).postId;
				
				Intent intent = new Intent();
				//context = context.getApplicationContext();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context, InputActivity.class);
				intent.putExtra("commentId", String.valueOf(commentId));
				intent.putExtra("postId", String.valueOf(postId));
				//intent.putExtra("username", preferences.getString("username", ""));
				context.startActivity(intent);
				//startActivity(intent);
				//notifyDataSetChanged();
			}
		}
		
	}
	
}
