/*
 * Author: Zhang Kai
*/

package com.edu.thss.smartdental;

import com.edu.thss.smartdental.RemoteDB.UserDBUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddOneFragment extends Fragment {
	
	Button add_one_btn;
	EditText username_edit, password_edit, repeat_password_edit;
	UserDBUtil db = new UserDBUtil();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_one, container, false);
		username_edit = (EditText)rootView.findViewById(R.id.username_edit);
		password_edit = (EditText)rootView.findViewById(R.id.password_edit);
		repeat_password_edit = (EditText)rootView.findViewById(R.id.repeat_password_edit);
		add_one_btn = (Button)rootView.findViewById(R.id.add_btn);
		add_one_btn.setOnClickListener(addListener);
		return rootView;
	}

	private OnClickListener addListener = new OnClickListener() {
		public void onClick(View v) {
			String username = username_edit.getText().toString(), password = password_edit.getText().toString(), repeat_password = repeat_password_edit.getText().toString();
			if (username.equals("")) {
				Toast.makeText(getActivity(), getString(R.string.message_null_username), Toast.LENGTH_LONG).show();
				return;
			}
			if (password.equals("")) {
				Toast.makeText(getActivity(), getString(R.string.message_null_password), Toast.LENGTH_LONG).show();
				return;
			}
			if (!repeat_password.equals(password)) {
				Toast.makeText(getActivity(), getString(R.string.message_password_not_same), Toast.LENGTH_LONG).show();
				return;
			}
			if (username.equals(getString(R.string.admin_username))) {
				Toast.makeText(getActivity(), getString(R.string.username_exists), Toast.LENGTH_LONG).show();
				return;
			}
			String t = db.insertUser(username, password, "doctor");
			if (t.equals("true")) {
				Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_LONG).show();
				username_edit.setText("");
				password_edit.setText("");
				repeat_password_edit.setText("");
			}
			else
				if (t.equals("username exists"))
					Toast.makeText(getActivity(), getString(R.string.username_exists), Toast.LENGTH_LONG).show();
				else
					if (t.equals("fail to connect to Database"))
						Toast.makeText(getActivity(), getString(R.string.message_link_fail), Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getActivity(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
		}
	};
}
