/**
 * Author: FU CHIN SENG
 */
package com.edu.thss.smartdental.adapter;

import java.util.ArrayList;

import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.model.CircleElement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class CircleListAdapter extends BaseAdapter {

	private ArrayList<CircleElement> list;
	private Context context;
	
	public CircleListAdapter(ArrayList<CircleElement> list, Context context) {
		this.list = list;
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.circle_list_item, null);
		}
		CircleElement element = list.get(position);
		String docName = element.docName;
		TextView name =(TextView)convertView.findViewById(R.id.circle_list_item_title);
		name.setText(docName);
		
		return convertView;
	}

}
