/*
 * 作者：王宇炜
 * 日期：2014年12月7日
 */
package com.edu.thss.smartdental;

import java.util.Date;

import com.edu.thss.smartdental.model.PostElement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BBSInTabPostFragment extends Fragment {
	private Button post_bbs_btn;
	private EditText edit_bbs_content;
	private EditText edit_bbs_title;
	private Spinner edit_tab_spinner;
	private ArrayAdapter adapter; 
	private static SharedPreferences preferences = null;
	private static Editor editor = null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		preferences = preferences == null ? this.getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE) : preferences;
		editor = editor == null ? preferences.edit() : editor;
		View rootView = inflater.inflate(R.layout.fragment_bbs_in_post, container, false);
		edit_bbs_title = (EditText)rootView.findViewById(R.id.edit_bbs_title);
		edit_bbs_content = (EditText)rootView.findViewById(R.id.edit_bbs_content);
		edit_tab_spinner = (Spinner)rootView.findViewById(R.id.edit_tab_spinner);
		adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.tab_names, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_tab_spinner.setAdapter(adapter);
		edit_tab_spinner.setVisibility(View.VISIBLE);
		post_bbs_btn = (Button)rootView.findViewById(R.id.post_bbs_btn);
		post_bbs_btn.setOnClickListener(postBBSListener);
		return rootView;
	}
	
	private OnClickListener postBBSListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String title = edit_bbs_title.getText().toString();
			String content = edit_bbs_content.getText().toString();
			String tabName = edit_tab_spinner.getSelectedItem().toString();
			if (title.equals("")) {
				Toast.makeText(getActivity(), "请填写帖子标题", Toast.LENGTH_LONG).show();
				return;
			}
			if (content.equals("")) {
				Toast.makeText(getActivity(), "请填写帖子内容", Toast.LENGTH_LONG).show();
				return;
			}
			PostElement postElement = new PostElement(title, content, preferences.getString("username", ""), tabName, preferences.getString("current_circle_id", ""), new Date(), false);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			postElement.insertToDB();
			builder.setMessage("发布成功")
				   .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						ft.replace(R.id.bbs_in_tab_content, new BBSInTabViewFragment());
						ft.commit();*/
						
					}
				});
			builder.show();
		}
	};
}
