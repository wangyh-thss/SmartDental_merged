package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.edu.thss.smartdental.CallBackInterface;
import com.edu.thss.smartdental.OneEMRActivity;
import com.edu.thss.smartdental.OneImageActivity;
import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.model.ImageElement;
import com.edu.thss.smartdental.model.general.GetUrlByTag;
import com.edu.thss.smartdental.model.general.HttpGetProcess;
import com.edu.thss.smartdental.model.general.HttpPostProcess;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


public class ImgListAdapter extends BaseAdapter implements Filterable{
	private class buttonViewHolder{
		Button read; //阅读
		Button delete; //删除
		Button hide; //隐藏
		Button mark;//标记
	}
	private ArrayList<ImageElement> list;
	private Context context;
	private ImageFilter filter;
	private buttonViewHolder holder;
	private Handler handler;
	private String post_result;
	private String JSON = "";
	private int tagnum;
	
	public ImgListAdapter(ArrayList<ImageElement> list, Context context, int tag){
		this.list = list;
		this.context = context;
		this.post_result = "";
		this.JSON = "";
		this.tagnum = tag;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.image_list_item, null);
		}
		ImageElement image = list.get(position);
		//add!!!!
		TextView name =(TextView)convertView.findViewById(R.id.image_list_item_title);
		TextView description = (TextView)convertView.findViewById(R.id.image_list_item_description);
		TextView time = (TextView)convertView.findViewById(R.id.image_list_item_time);
		
		name.setText(image.name);
		description.setText(image.description);
		time.setText(image.time);
		
		holder = new buttonViewHolder();
		holder.read = (Button)convertView.findViewById(R.id.image_list_item_read);
		holder.delete = (Button)convertView.findViewById(R.id.image_list_item_delete);
		holder.hide = (Button)convertView.findViewById(R.id.image_list_item_hide);
		holder.mark = (Button)convertView.findViewById(R.id.image_list_item_hint);
		
		holder.hide.setText(image.isHidden?"不隐藏":"隐藏");
		holder.mark.setText(image.isMarked?"不标记":"标记");
		holder.read.setText(image.isRead?"已读":"未读");
		
		holder.read.setOnClickListener(new ButtonListner(position));
		holder.delete.setOnClickListener(new ButtonListner(position));
		holder.mark.setOnClickListener(new ButtonListner(position));
		holder.hide.setOnClickListener(new ButtonListner(position));
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if(filter == null){
			filter = new ImageFilter(list);
		}
		return filter;
	}
    public class ImageFilter extends Filter{
    	private ArrayList<ImageElement> original;
    	public ImageFilter(ArrayList<ImageElement> list){
    		this.original = list;
    	}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults results = new FilterResults();
			if(constraint == null || constraint.length()==0){ //没有过滤条件
				results.values = this.original;
				results.count = this.original.size();
				
			}
			else{
				
				ArrayList<ImageElement> mList = new ArrayList<ImageElement>();
				for(ImageElement image: original){
					if(image.name.toUpperCase().contains(constraint.toString().toUpperCase())
					   ||image.description.toUpperCase().contains(constraint.toString().toUpperCase())
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
			list = (ArrayList<ImageElement>)results.values;
			notifyDataSetChanged();
			
		}
    }
    
    protected void ConfirmDeleteDialog(final String caseid) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认删除？");
		builder.setTitle("提示：");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), caseid, "delete", "1").start();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
    
    private class HandleAfterPost implements CallBackInterface{

		@Override
		public void CallBack(String src) {
			// TODO Auto-generated method stub
			if (src.equals("success")){
				new HttpGetProcess(handler, JSON, new GetUrlByTag().GetUrl(tagnum), new UpdateImageInfo()).start();
				Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
    	
    }
    
    
    class ButtonListner implements OnClickListener{
		private int itemPosition;
		public ButtonListner(int pos){
			this.itemPosition = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if(vid == holder.delete.getId()){
			 //删除
				//list.remove(itemPosition);
				new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), list.get(itemPosition).caseID, "delete", "1").start();
				//需要向服务器发post请求修改
				//notifyDataSetChanged();
				//ConfirmDeleteDialog(list.get(itemPosition).caseID);
			}
		    if(vid == holder.hide.getId()){
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isHidden == false){
		    		new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), temp.caseID, "hide", "1").start();
		    		//temp.isHidden = true;
		    	}
		    	else {
		    		new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), temp.caseID, "hide", "0").start();
		    		//temp.isHidden = false;
		    	}
		    	//需要向服务器发post请求修改
		    	//list.set(this.itemPosition, temp);
		    	//notifyDataSetChanged();
		    }
		    if(vid == holder.mark.getId()){
		    	
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isMarked ==false) {
		    		new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), temp.caseID, "record", "1").start();
		    		//temp.isMarked = true;
		    	}
		    	else {
		    		new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), temp.caseID, "record", "0").start();
		    		//temp.isMarked = false;
		    	}
		    	//需要向服务器发post请求修改
		    	//list.set(this.itemPosition, temp);
		    	//notifyDataSetChanged();
		    }
		    if(vid == holder.read.getId()){
		    	ImageElement temp = list.get(this.itemPosition);
		    	if(temp.isRead == false){
		    		new HttpPostProcess(handler, post_result, "http://166.111.80.119/userinfo", new HandleAfterPost(), temp.caseID, "read", "1").start();
		    		//temp.isRead = true;
		    	}
		    	//需要向服务器发post请求修改
		    	//list.set(this.itemPosition, temp);
		    	
		    	//notifyDataSetChanged();
		    	Intent intent = new Intent(context, OneImageActivity.class);
		    	Bundle bundle = new Bundle();
		    	bundle.putCharSequence("imageclass", list.get(this.itemPosition).caseID);
		    	intent.putExtras(bundle);
		    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		    	context.startActivity(intent);
		    }
		}
	}
    
    private class UpdateImageInfo implements CallBackInterface {

		@Override
		public void CallBack(String src) {
			// TODO Auto-generated method stub
			try {
				JSONTokener jsonParser = new JSONTokener(src);
				JSONObject jsonObj = (JSONObject) jsonParser.nextValue();
				JSONArray info = jsonObj.getJSONArray("pic_info");
				int num_of_pic = info.length();
				list = new ArrayList<ImageElement>(num_of_pic);
				for (int i = 0; i < num_of_pic; i++) {
					JSONObject jo = (JSONObject)info.optJSONObject(i);
					boolean is_hidden = (jo.getInt("hide") == 1)? true:false;
					boolean is_marked = (jo.getInt("record") == 1)? true:false;
					boolean is_read = (jo.getInt("read") == 1)? true:false;
					ImageElement tmpElement = new ImageElement(jo.getString("pic_name"), jo.getString("position"), jo.getString("date"), is_hidden, is_marked, is_read, jo.getString("caseid"));
					list.add(tmpElement);
				}
				notifyDataSetChanged();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
