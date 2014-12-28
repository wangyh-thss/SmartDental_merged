/**
 * Author: FU CHIN SENG
 */
package com.edu.thss.smartdental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




import com.edu.thss.smartdental.RemoteDB.UserDBUtil;
import com.edu.thss.smartdental.adapter.CircleListAdapter;
import com.edu.thss.smartdental.model.CircleElement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class BBSFragment extends Fragment {

	private FragmentManager fragmentManager;
	private ListView list;
	private ArrayList<CircleElement> circles;
	private CircleListAdapter listAdapter;
	private View join_circle_button;
	private ProgressDialog pd;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bbs, container, false);
		
		join_circle_button = rootView.findViewById(R.id.join_circle_button);
		join_circle_button.setOnClickListener(new OnJoinButtonClickListener());
		
		fragmentManager = getFragmentManager();
		list = (ListView) rootView.findViewById(R.id.circle_list);
		
		pd = ProgressDialog.show(this.getActivity(), "", getActivity().getResources().getString(R.string.loading));
		circles = new ArrayList<CircleElement>();
		
		listAdapter = new CircleListAdapter(circles, this.getActivity().getApplicationContext());
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnCircleItemClickListener());
		new Thread(new Runnable(){

			@Override
			public void run() {
				initCircles();		
				handler.sendEmptyMessage(0); 			
			}
		}).start();  
		
		return rootView;
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {  
        
		@Override  
		public void handleMessage(Message msg) {// handler鎺ユ敹鍒版秷鎭悗灏变細鎵ц姝ゆ柟娉�
			showCircles();
			pd.dismiss();
        }  
    };
    
	private void initCircles(){
		
		UserDBUtil db = new UserDBUtil();
		String username = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE).getString("username", "");
		List<HashMap<String, String>> docList = db.selectDoctorsByname(username);
		Iterator<HashMap<String, String>> iterator = docList.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			HashMap<String, String> element = iterator.next();
			CircleElement circleElement = new CircleElement(element.get("doctorname"), element.get("doctorid"));
			circles.add(circleElement);
		}
	}
	
	private void showCircles(){
		this.listAdapter.notifyDataSetChanged();
    }
    
	private class OnJoinButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), JoinCircleActivity.class);
			startActivityForResult(intent, 101);
		}
		
	}
	
	private class OnCircleItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Fragment fragment= new BBSInFragment(Integer.parseInt(circles.get(position).docID));
			Editor editor = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE).edit();
			editor.putString("current_circle", circles.get(position).docName);
			editor.putString("current_circle_id", circles.get(position).docID);
			editor.commit();
			fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
		}
		
	}
	
}
