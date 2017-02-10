package co.kr.hybridapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import co.kr.hybridapp.adapter.SectionsPagerAdapter;

public class SlideViewActivity extends FragmentActivity{
	private FrameLayout flContainer;
	private View drawerView;
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideview);

		


		flContainer = (FrameLayout)findViewById(R.id.fl_activity_main_container);
		drawerView = (View) findViewById(R.id.drawer);

		dlDrawer = (DrawerLayout)findViewById(R.id.dl_activity_main_drawer);
		dtToggle = new ActionBarDrawerToggle(this, dlDrawer, 			
				R.drawable.slide, R.string.open_drawer, R.string.close_drawer){
			@Override
			public void onDrawerClosed(View drawerView) {
				Log.e("test", "--onDrawerClosed--");
				super.onDrawerClosed(drawerView);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				Log.e("test", "--onDrawerOpened--");
				super.onDrawerOpened(drawerView);
			}
		};
		dlDrawer.setDrawerListener(dtToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true); // 타이틀


		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getApplicationContext(),this, getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setBackgroundColor(Color.WHITE);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				Log.e("SKY" ,"onPageSelected  :: " + position);
				Log.e("SKY" , "--ACTION_UP--");
			}
			@Override
			public void onPageScrolled(int position, float positionOffSet, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				Log.v("CHECK2","onPageScrolled");
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				Log.e("SKY" , "--onPageScrollStateChanged--");
			}
		});


		//이미지 가운데 올리기
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F16261")));


		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBar.setCustomView(R.layout.action_bar_title_main);
//		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_btn));


//		findViewById(R.id.last_btn2).setOnClickListener(btnListener);
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {

			switch (v.getId()) {
//			case R.id.bt_bottom:	
//				Log.e("SKY" , "bt_bottom");
//				break;

			}
		}
	};

}
