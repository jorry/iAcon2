//package com.iAcon.fragmenAdapter;
//
//import com.iAcron.R;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.FrameLayout;
//import android.widget.RadioButton;
//
//public class FragmentAdapterManager extends FragmentActivity implements
//		OnCheckedChangeListener, OnClickListener {
//	private RadioButton mTab1;
//	private RadioButton mTab2;
//	private RadioButton mTab3;
//	private RadioButton mTab4;
//	private RadioButton mTab5;
//	private FrameLayout mContainer;
//	public CompoundButton currentButtonView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.qinqing_fragment);
//		
//		mContainer = (FrameLayout)findViewById(R.id.container);
//		mTab1 = (RadioButton) findViewById(R.id.radio_button0);
//		mTab2 = (RadioButton) findViewById(R.id.radio_button1);
//		mTab3 = (RadioButton) findViewById(R.id.radio_button2);
//		
//		mTab1.setOnCheckedChangeListener(this);
//		mTab2.setOnCheckedChangeListener(this);
//		mTab3.setOnCheckedChangeListener(this);
//		
//		mTab1.setOnClickListener(this);
//		mTab2.setOnClickListener(this);
//		mTab3.setOnClickListener(this);
//		
//		mTab1.setChecked(true);
//	}
//
//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		if (isChecked) {
//			System.out.println(""+buttonView.getId());
//			Fragment fragment = (Fragment) mFragmentPagerAdapter
//					.instantiateItem(mContainer, buttonView.getId());
//			mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
//			mFragmentPagerAdapter.finishUpdate(mContainer);
//		}
//	}
//
//	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
//			getSupportFragmentManager()) {
//		@Override
//		public Fragment getItem(int position) {
//			switch (position) {
//			case R.id.radio_button0:
//				return Fragment1.instantiation(1);
//			
//			case R.id.radio_button1:
//				return Fragment1.instantiation(2);
//			case R.id.radio_button2:
//				return Fragment1.instantiation(3);
//			default:
//				return Fragment1.instantiation(1);
//			}
//		}
//
//		@Override
//		public int getCount() {
//			return 3;
//		}
//	};
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//	}
//}
