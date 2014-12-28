package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.BBSDetailActivity;
import com.edu.thss.smartdental.InputActivity;
import com.edu.thss.smartdental.MainActivity;
import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.RemoteDB.PostDBUtil;
import com.edu.thss.smartdental.model.BBSDetail;




import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class BBSDetailAdapter extends BaseAdapter implements Filterable{
	private ArrayList<BBSDetail> list;
	private Context context;
	private BBSFilter filter;
	private buttonViewHolder holder;
	
	private buttonViewHolder holder1;
	BBSDetailActivity context1;
	
	private class buttonViewHolder{
		Button delete; //
		Button collect;
		Button reply;
	}
	
	public BBSDetailAdapter(ArrayList<BBSDetail> list, Context context, BBSDetailActivity context1){
		this.list = list;
		this.context = context;
		this.context1 = context1;
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
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.bbs_detail_item, null);
		}
		BBSDetail post = list.get(position);
		TextView title =(TextView)convertView.findViewById(R.id.bbs_detail_item_title);
		TextView content = (TextView)convertView.findViewById(R.id.bbs_detail_item_content);
		TextView time = (TextView)convertView.findViewById(R.id.bbs_detail_item_time);
		TextView author = (TextView)convertView.findViewById(R.id.bbs_detail_item_author);
		
		title.setText(post.title);
		content.setText(post.content);
		time.setText(post.time);
		author.setText(post.author);
		
		holder = new buttonViewHolder();
		holder.delete = (Button)convertView.findViewById(R.id.bbs_detail_item_delete);
		holder.delete.setOnClickListener(new deleteButtonListner(position));
		
		holder.collect = (Button)convertView.findViewById(R.id.bbs_detail_item_collect);
		holder.collect.setOnClickListener(new collectButtonListner(holder.collect));
		
		holder.reply = (Button)convertView.findViewById(R.id.bbs_detail_item_reply);
		holder.reply.setOnClickListener(new replyButtonListner());
		
		//judge the delete button and collect button show or not
		if(!context1.isLocalUser()){
			holder.delete.setVisibility(View.INVISIBLE);
		}
		else{
			holder.collect.setVisibility(View.INVISIBLE);
		}
		//judge the state of collect button
		if(context1.postCollected()){
			context1.changeState(holder.collect, false);
		}
		
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if(filter == null){
			filter = new BBSFilter(list);
		}
		return filter;
	}
	
    public class BBSFilter extends Filter{
    	private ArrayList<BBSDetail> original;
    	public BBSFilter(ArrayList<BBSDetail> list){
    		this.original = list;
    	}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults results = new FilterResults();
			if(constraint == null || constraint.length()==0){ 
				results.values = this.original;
				results.count = this.original.size();
				
			}
			else{
				
				ArrayList<BBSDetail> mList = new ArrayList<BBSDetail>();
				for(BBSDetail image: original){
					if(image.title.toUpperCase().contains(constraint.toString().toUpperCase())
					   ||image.content.toUpperCase().contains(constraint.toString().toUpperCase())
					   ||image.time.contains(constraint)){
						
						mList.add(image);
					}
				}
				results.values = mList;
				results.count = mList.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			list = (ArrayList<BBSDetail>)results.values;
			notifyDataSetChanged();
			
		}
    }
    
    class deleteButtonListner implements OnClickListener{
		private int itemPosition;
		public deleteButtonListner(int pos){
			this.itemPosition = pos;
		}

		@Override
		public void onClick(View v) {

			int vid = v.getId();
			if(context1.isLocalUser()){
				//delete the post 
				PostDBUtil dbUtil = new PostDBUtil();
				dbUtil.deletePost(Integer.parseInt(context1.getPostId()));
				
				list.remove(itemPosition);
				notifyDataSetChanged();
				context1.markDelete();
				context1.finish();
                
			}
			
		}
    }
    
    class collectButtonListner implements OnClickListener{
    	Button button;
    	public collectButtonListner(Button but){
    		this.button = but;
    	}
    	
    	@Override
    	public void onClick(View v){
    		if(!context1.collected(button.getText().toString())){				// to collect
    			context1.collectPost();
    			context1.changeState(button, false);
    		}
    		else{												//  cancellation
    			context1.cancelCollectPost();
    			context1.changeState(button, true);
    		}
    	}
    }
    
    class replyButtonListner implements OnClickListener{
    	public replyButtonListner(){
    	}
    	@Override
    	public void onClick(View v){
    		//context1.beginReply();  
    		
    		Intent intent = new Intent();
			//context = context.getApplicationContext();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(context, InputActivity.class);
			intent.putExtra("commentId", "");
			intent.putExtra("postId", context1.getPostId());
			context.startActivity(intent);
    		
    		
    	}
    }

}
