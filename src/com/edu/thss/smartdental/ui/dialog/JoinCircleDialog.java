/**
 * Author: FU CHIN SENG
 */
package com.edu.thss.smartdental.ui.dialog;

import com.edu.thss.smartdental.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class JoinCircleDialog extends DialogFragment {
	
	private View dialogView;
	private String docName;
	private String docID;
	
	public interface JoinCircleDialogListener {
		public void onDialogPositiveClick(JoinCircleDialog dialog);
		public void onDialogNegativeClick(JoinCircleDialog dialog);
    }
	
	JoinCircleDialogListener mListener;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialogView = inflater.inflate(R.layout.dialog_join_circle, null);
		
		builder.setView(dialogView);
		builder.setTitle(R.string.enter_circle_password)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
        	  	public void onClick(DialogInterface dialog, int id) {
        	  		mListener.onDialogPositiveClick(JoinCircleDialog.this);
        	  					}
          					})
          .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        	  	public void onClick(DialogInterface dialog, int id) {
        	  		mListener.onDialogNegativeClick(JoinCircleDialog.this);
        	  					}
               				});
		return builder.create();
    }
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (JoinCircleDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
		}
    }
	
	public void setDocName(String name) {
		docName = name.trim();
	}
	
	public String getDocName() {
		return docName;
	}
	
	public View getDialogView() {
		return dialogView;
	}
	
	public void setDocID(String id) {
		docID = id.trim();
	}
	
	public String getDocID() {
		return docID;
	}
}