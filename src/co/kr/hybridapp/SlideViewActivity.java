package co.kr.hybridapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.adapter.SectionsPagerAdapter;
import co.kr.hybridapp.common.Check_Preferences;
import co.kr.hybridapp.common.CustomDialog;
import co.kr.hybridapp.common.DEFINE;
import co.kr.hybridapp.common.ExitCustomDialog;

public class SlideViewActivity extends FragmentActivity{
	private FrameLayout flContainer;
	private View drawerView;
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	int slie_menu_f = 0;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private CustomDialog mCustomDialog;
	public static Context mContext;
	private boolean clearHistory = false;
	public View vi;
	private ExitCustomDialog mexitCustomDialog;
	public static Boolean exit_flag = false;
	public static WebView wc;
	//	window.location.href = "js2ios://SubActivity?url=&title=11번가&action=left&new=1&button=로그인&button_url=http://snap40.cafe24.com";
	public static ActionBar actionBar;
	public static RelativeLayout action_bar;
	
	public static String gourl = "";
	public static String SUB_URL;
	public static String TITLE;
	public static String NEW;
	public static String BUTTON;
	public static String BUTTON_URL;
	private Typeface ttf;
	ProgressDialog dialog;
	TelephonyManager tMgr;

	ImageView rednew1 , rednew2 , rednew3 , rednew4, rednew5;
	@Override
	protected void onResume(){
		super.onResume();
		if (gourl.length() > 1) {
			wc.loadUrl(gourl);
			gourl = "";
		}
		if (exit_flag) {
			exit_flag  = false;
			finish();
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideview);
		ttf = Typeface.createFromAsset(getAssets(), "RixB.ttf");
		mContext = this;
		tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		rednew1 = (ImageView) findViewById(R.id.rednew1);
		rednew2 = (ImageView) findViewById(R.id.rednew2);
		rednew3 = (ImageView) findViewById(R.id.rednew3);
		rednew4 = (ImageView) findViewById(R.id.rednew4);
		rednew5 = (ImageView) findViewById(R.id.rednew5);



		SUB_URL = getIntent().getStringExtra("SUB_URL");
		TITLE = getIntent().getStringExtra("TITLE");
		NEW = getIntent().getStringExtra("NEW");
		BUTTON = getIntent().getStringExtra("BUTTON");
		BUTTON_URL = getIntent().getStringExtra("BUTTON_URL");

		String new_str[] = NEW.split("/");
		for (int i = 0; i < new_str.length; i++) {
			switch (Integer.parseInt(new_str[i])) {
			case 1:
				rednew1.setVisibility(View.VISIBLE);
				break;
			case 2:
				rednew2.setVisibility(View.VISIBLE);
				break;
			case 3:
				rednew3.setVisibility(View.VISIBLE);
				break;
			case 4:
				rednew4.setVisibility(View.VISIBLE);
				break;
			case 5:
				rednew5.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
		
		/*Test sample*/
		/*
		SUB_URL = "http://www.11st.co.kr/html/bestSellerMain.html";
		TITLE = "11번가";
		NEW = "1";
		BUTTON = "로그인";
		BUTTON_URL = "http://snap40.cafe24.com";
		 */
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
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F16261")));


		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		actionBar.setCustomView(R.layout.action_bar_title_main);
		action_bar = (RelativeLayout)actionBar.getCustomView().findViewById(R.id.action_bar);
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


		//		TextView txt1 = (TextView)findViewById(R.id.txt1);
		//		txt1.setTypeface(ttf);
		//		TextView txt2 = (TextView)findViewById(R.id.txt2);
		//		txt2.setTypeface(ttf);
		//		TextView txt3 = (TextView)findViewById(R.id.txt3);
		//		txt3.setTypeface(ttf);
		//		TextView txt4 = (TextView)findViewById(R.id.txt4);
		//		txt4.setTypeface(ttf);
		//		TextView txt5 = (TextView)findViewById(R.id.txt5);
		//		txt5.setTypeface(ttf);
		//WebSetting();

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
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK&&wc.canGoBack()){
			wc.goBack();
			return true;
		}else{
			//종료 타입 1, 2
			if (Check_Preferences.getAppPreferences(SlideViewActivity.this , "SETPOPEXIT_TYPE1" ).equals("true")) {
				//디폴트 종료하기
				final AlertDialog.Builder builder = new AlertDialog.Builder(SlideViewActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				builder.setMessage("종료 하시겠습니까?");
				builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("SKY","AAA:: " + Check_Preferences.getAppPreferences(SlideViewActivity.this , "SETEXIT_TYPE"));
						finish();
					}
				});
				builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				//이미지만 나오는 푸시 팝업
				Intent i = new Intent(SlideViewActivity.this , ShowIMGActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setComponent(new ComponentName(SlideViewActivity.this, ShowIMGActivity.class));
				i.putExtra("flag", true);
				i.putExtra("imgurl","http://img1.tmon.kr/deals/2017/03/08/520859290/front_3325a.jpg");
				i.putExtra("openurl","http://www.naver.com/");
				i.putExtra("bottomview","true");
				startActivity(i);
				
//				//이미지 팝업 종료
//				mexitCustomDialog = new ExitCustomDialog(SlideViewActivity.this, 
//						"https://byunsooblog.files.wordpress.com/2014/06/find-in-path.png",
//						leftClickListener, 
//						rightClickListener);
//				mexitCustomDialog.show();
			}
			
		}

		return super.onKeyDown(keyCode, event);
	}
	private View.OnClickListener leftClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			finish();
			mCustomDialog.dismiss();
		}
	};

	private View.OnClickListener rightClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mCustomDialog.dismiss();
		}
	};
	public static boolean isPackageInstalled(String pkgName) {
		try {
			mContext.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressLint("NewApi")
	private void getDeviceId(final String callback){
		Log.e("SKY" , "-- getDeviceId --");
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final String device_id = prefs.getString("device_id","");
		final boolean pushEnable = prefs.getBoolean("pushEnable",false);

		if( Build.VERSION.SDK_INT < 19 ){
			wc.loadUrl("javascript:"+callback+"('"+device_id+"',"+pushEnable+");");
		}else{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					ValueCallback<String> resultCallback = null;
					wc.evaluateJavascript(callback+"('"+device_id+"',"+pushEnable+");",resultCallback);
				}
			});    			
		}		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == FILECHOOSER_RESULTCODE){
			if(null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}else if(requestCode == 9000) {
			if(intent == null) return;
			Bundle bundle = intent.getExtras();
			String type = bundle.getString("type");
			String data = bundle.getString("data");
			Log.e("SKY", "type :: " + type);
			Log.e("SKY", "data :: " + data);
			wc.loadUrl("javascript:appLoginCallback('"+type+"', '"+data+"')");
		}else if(requestCode == 120){
			if(resultCode==120){
				wc.loadUrl(DEFINE.SETTING_120);
			}
			//로그인시
			if(resultCode==121){
				//spu.put("islogin", 0);
				wc.loadUrl(DEFINE.SETTING_121);
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeSessionCookie();
			}
			//로그아웃시
			if(resultCode==130){
				wc.loadUrl(DEFINE.SETTING_130);
			}
			//공지
			if(resultCode==131){
				wc.loadUrl(DEFINE.SETTING_131);
			}
			if(resultCode==132){
				wc.loadUrl(DEFINE.SETTING_132);
			}
			if(resultCode==133){
				wc.loadUrl(DEFINE.SETTING_133);
			}
		}else if(requestCode == DEFINE.REQ_LOCATION) {
			if(intent == null) return;
			Bundle bundle = intent.getExtras();
			String data = bundle.getString("data");
			try {
				JSONObject jobj = new JSONObject(data);
				String address = jobj.getString("address");
				double lat = jobj.getDouble("lat");
				double lng = jobj.getDouble("lng");
				String url = wc.getUrl();
				url = url.substring(0, url.indexOf("#"));
				url = url.substring(0, url.indexOf("?"));
				wc.loadUrl(url+"?address="+address+"&lat="+lat+"&lng="+lng+"&deviceid="+tMgr.getDeviceId());
			} catch (JSONException e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}


}
