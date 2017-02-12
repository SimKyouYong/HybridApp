package co.kr.hybridapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.adapter.SectionsPagerAdapter;

public class SlideViewActivity extends FragmentActivity{
	private FrameLayout flContainer;
	private View drawerView;
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	int slie_menu_f = 0;

	public static WebView wc;
	//	window.location.href = "js2ios://SubActivity?url=&title=11번가&action=left&new=1&button=로그인&button_url=http://snap40.cafe24.com";

	public static String SUB_URL;
	public static String TITLE;
	public static String NEW;
	public static String BUTTON;
	public static String BUTTON_URL;
	private Typeface ttf;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideview);
		ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");


		SUB_URL = getIntent().getStringExtra("SUB_URL");
		TITLE = getIntent().getStringExtra("TITLE");
		NEW = getIntent().getStringExtra("NEW");
		BUTTON = getIntent().getStringExtra("BUTTON");
		BUTTON_URL = getIntent().getStringExtra("BUTTON_URL");

		/*Test sample*/
		SUB_URL = "http://www.11st.co.kr/html/bestSellerMain.html";
		TITLE = "11번가";
		NEW = "1";
		BUTTON = "로그인";
		BUTTON_URL = "http://snap40.cafe24.com";
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
		mViewPager.setOffscreenPageLimit(1);
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
		init();
	}
	private void init(){
		Button bt = (Button)findViewById(R.id.btn_list);
		bt.setText("" + BUTTON);
		bt.setTypeface(ttf);

		TextView tt = (TextView)findViewById(R.id.titlea);
		tt.setText("" + TITLE);
		tt.setTypeface(ttf);

		
		TextView txt1 = (TextView)findViewById(R.id.txt1);
		txt1.setTypeface(ttf);
		TextView txt2 = (TextView)findViewById(R.id.txt2);
		txt2.setTypeface(ttf);
		TextView txt3 = (TextView)findViewById(R.id.txt3);
		txt3.setTypeface(ttf);
		TextView txt4 = (TextView)findViewById(R.id.txt4);
		txt4.setTypeface(ttf);
		TextView txt5 = (TextView)findViewById(R.id.txt5);
		txt5.setTypeface(ttf);
		
		
		findViewById(R.id.slide).setOnClickListener(btnListener);
		findViewById(R.id.btn_list).setOnClickListener(btnListener);
		findViewById(R.id.btn1).setOnClickListener(btnListener);
		findViewById(R.id.btn2).setOnClickListener(btnListener);
		findViewById(R.id.btn3).setOnClickListener(btnListener);
		findViewById(R.id.btn4).setOnClickListener(btnListener);
		findViewById(R.id.btn5).setOnClickListener(btnListener);
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.slide:	
				Log.e("SKY" , "slide");
				//토글형식으로 온 오프 적용 하기
				if (slie_menu_f == 0) {
					slie_menu_f = 1;
					dlDrawer.openDrawer(drawerView);
				}else{
					slie_menu_f = 0;
					dlDrawer.closeDrawer(drawerView);
				}
				break;
			case R.id.btn_list:	
				Log.e("SKY" , "btn_list");
				wc.loadUrl(BUTTON_URL);
				break;
			case R.id.btn1:	
				Log.e("SKY" , "btn1");
				Toast.makeText(getApplicationContext(), "btn1 Click!!", 0).show();
				break;
			case R.id.btn2:	
				Log.e("SKY" , "btn2");
				Toast.makeText(getApplicationContext(), "btn2 Click!!", 0).show();
				break;
			case R.id.btn3:	
				Log.e("SKY" , "btn3");
				Toast.makeText(getApplicationContext(), "btn3 Click!!", 0).show();
				break;
			case R.id.btn4:	
				Log.e("SKY" , "btn4");
				Toast.makeText(getApplicationContext(), "btn4 Click!!", 0).show();
				break;
			case R.id.btn5:	
				Log.e("SKY" , "btn5");
				Toast.makeText(getApplicationContext(), "btn5 Click!!", 0).show();
				break;

			}
		}
	};

}
