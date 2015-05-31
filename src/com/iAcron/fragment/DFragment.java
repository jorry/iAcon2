package com.iAcron.fragment;

import com.iAcron.JianHuActivity;
import com.iAcron.R;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 弹出框
 * 
 * @author jorry_liu
 * 
 */
public class DFragment extends DialogFragment implements OnEditorActionListener {

	public interface EditNameDialogListener {
		void onFinishEditDialog(String inputText);
	}

	private EditText mEditText;
	private String title;
	private String message;
	private String poString;
	private String noString;

	public DFragment(String title, String message,
			String poS, String noS) {
		// Empty constructor required for DialogFragment
		this.title = title;
		this.message = message;
		this.poString = poS;
		this.noString = noS;
	}
	public View v;
	public DFragment(String title, String message,
			String poS, String noS,View v) {
		// Empty constructor required for DialogFragment
		this.title = title;
		this.message = message;
		this.poString = poS;
		this.noString = noS;
		this.v = v;
	}
	
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle(title);
		dialog.setMessage(message);
		if(v!=null){
			dialog.setView(v);
		}
		if (!TextUtils.isEmpty(poString)) {
			dialog.setPositiveButton(poString,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if(onClick!=null){
								onClick.poOnclick();
							}
						}
					});
		}
		if (!TextUtils.isEmpty(noString)) {
			dialog.setNegativeButton(noString,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if(onClick!=null){
								onClick.neOnclick();
							}
						}
					});
		}
		return dialog.show();
	}
	
	ButtonOnClick onClick;
	public void setButtonOnClick(ButtonOnClick onClick){
		this.onClick = onClick;
	}

	public interface ButtonOnClick {
		public void poOnclick();

		public void neOnclick();

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// Return input text to activity
			EditNameDialogListener activity = (EditNameDialogListener) getActivity();
			activity.onFinishEditDialog(mEditText.getText().toString());
			this.dismiss();
			return true;
		}
		return false;
	}
}