package com.edu.thss.smartdental;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.edu.thss.smartdental.adapter.EMRListAdapter;
import com.edu.thss.smartdental.adapter.ImgListAdapter;
import com.edu.thss.smartdental.model.EMRElement;
import com.edu.thss.smartdental.model.ImageElement;
import com.edu.thss.smartdental.model.general.GetUrlByTag;
import com.edu.thss.smartdental.model.general.HttpGetProcess;

import android.R.bool;
import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ImgTabAllFragment extends Fragment{
	private int tagnum;
	
	private ImgListAdapter listAdapter;
	private EditText editText;
	private ListView list;
	private ArrayList<ImageElement> images;
	
	private String JSON = "";	
	
	private String id = null;
	private int num_of_pic = 0;
	
	private Handler handler;
	
	public ImgTabAllFragment(int tag) {
		// TODO Auto-generated constructor stub
		this.tagnum = tag;
	}
	
	public void getJSON(String src){
		//�����ϻ�ȡ��JSON�н���
		try {
			JSONTokener jsonParser = new JSONTokener(src);
			JSONObject jsonObj = (JSONObject) jsonParser.nextValue();
			id = jsonObj.getString("_id");
			JSONArray info = jsonObj.getJSONArray("pic_info");
			num_of_pic = info.length();
			images = new ArrayList<ImageElement>(num_of_pic);
			for (int i = 0; i < num_of_pic; i++) {
				JSONObject jo = (JSONObject)info.optJSONObject(i);
				boolean is_hidden = (jo.getInt("hide") == 1)? true:false;
				boolean is_marked = (jo.getInt("record") == 1)? true:false;
				boolean is_read = (jo.getInt("read") == 1)? true:false;
				ImageElement tmpElement = new ImageElement(jo.getString("pic_name"), jo.getString("position"), jo.getString("date"), is_hidden, is_marked, is_read, jo.getString("caseid"));
				images.add(tmpElement);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class GetImagesInfo implements CallBackInterface{

		@Override
		public void CallBack(String src) {
			// TODO Auto-generated method stub
			getJSON(src);
			listAdapter = new ImgListAdapter(images,ImgTabAllFragment.this.getActivity().getApplicationContext(), tagnum);
			list.setAdapter(listAdapter);
		}
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_all, container,false);
		editText = (EditText)rootView.findViewById(R.id.image_searchbox);
		editText.addTextChangedListener(filterTextWatcher);
		list = (ListView)rootView.findViewById(R.id.image_list);
		
		return rootView;
	}

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
			listAdapter.getFilter().filter(s);	
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		images = new ArrayList<ImageElement>();
		SharedPreferences preferences = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE);
		String username = preferences.getString("username", "");
		new HttpGetProcess(handler, JSON, new GetUrlByTag(username).GetUrl(tagnum), new GetImagesInfo()).start();
	}
	
}
