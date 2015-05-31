package com.iAcron.view;

import com.iAcron.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MMAlert {

	/**
	 * 
	 * @param context
	 * @param title  标题，可以为空
	 * @param items  可以为空
	 * @param exit   退出按钮，可以为空
	 * @param cancelListener  取消监听，可以为空
	 * @param btListener     按键监听，不可用为空
	 * @return
	 */
	public static Dialog showAlert(final Context context,View view) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		final int cFullFillWidth = 10000;
		view.setMinimumWidth(cFullFillWidth);

		// set a large value put it in bottom
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(view);
		dlg.show();

		return dlg;
	}
}
