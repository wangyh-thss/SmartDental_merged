/**
 * Author: FU CHIN SENG
 */
package com.edu.thss.smartdental;

import java.util.ArrayList;

import com.edu.thss.smartdental.RemoteDB.NewsDBUtil;
import com.edu.thss.smartdental.adapter.NewsListAdapter;
import com.edu.thss.smartdental.model.NewsElement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BBSInTabNewsFragment extends Fragment {
	
	private ListView listView;
	private NewsListAdapter listAdapter;
	private ArrayList<NewsElement> newsList;
	private TextView noNewsText;
	
	public BBSInTabNewsFragment(ArrayList<NewsElement> newsList) {
		this.newsList = newsList;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bbs_in_news, container, false);
		
		this.listView = (ListView) rootView.findViewById(R.id.bbs_news_list);
		this.noNewsText = (TextView) rootView.findViewById(R.id.bbs_no_news_text);
		if (newsList.size() > 0) {
			noNewsText.setVisibility(View.INVISIBLE);
		}
		this.listAdapter = new NewsListAdapter(newsList, getActivity());
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnNewsListItemClickListener());
		
		return rootView;
	}

	private class OnNewsListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			NewsDBUtil db = new NewsDBUtil();
			NewsElement element = newsList.get(position);
			db.haveread(element.getNewsID());
			
			Intent intent = new Intent(getActivity(), BBSDetailActivity.class);
			intent.putExtra("postId", element.getPostID());
			startActivity(intent);
			newsList.remove(position);
			if (newsList.size() == 0) {
				noNewsText.setVisibility(View.VISIBLE);
			} else {
				noNewsText.setVisibility(View.INVISIBLE);
			}
			listAdapter.notifyDataSetChanged();
		}
		
	}
}
