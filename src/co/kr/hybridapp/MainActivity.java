package co.kr.hybridapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.common.ActivityEx;
import co.kr.hybridapp.common.Check_Preferences;
import co.kr.hybridapp.common.CommonUtil;
import co.kr.hybridapp.common.CustomDialog;
import co.kr.hybridapp.common.DEFINE;
import co.kr.hybridapp.common.DataSync;
import co.kr.hybridapp.common.UtilFunction;


@SuppressLint("JavascriptInterface")
public class MainActivity extends ActivityEx implements LocationListener {

	private boolean exit_type = false;
	Boolean First_Flag = false;
	public static Context mContext;
	WebView mWebView,pWebView;
	ProgressBar mProgressHorizontal;
	ProgressDialog dialog;
	CommonUtil dataSet = CommonUtil.getInstance();
	Boolean FirstLoadUrl = true;
	ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
	String FirstUrl = "";
	String openURL="";
	String getMsg="";
	boolean getMsgPop = false;
	int progress_count=0;
	float set_progress = 0;
	String homeURL;
	LocationManager locationManager;
	LocationListener locationListener;
	public static String address="";

	boolean popup = false;
	private boolean pan = false;
	private boolean clearHistory = false;

	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;

	public RelativeLayout mainBody;
	public LinearLayout bottomMenu , bottomMenu2;
	public View vi;
	TelephonyManager tMgr;
	private Typeface ttf;
	String key;
	private CustomDialog mCustomDialog,mCustomDialog2;
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearApplicationCache(null);
	}
	@SuppressLint({"SetJavaScriptEnabled","NewApi"})
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("SKY" , "onCreate");
		ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");



		setContentView(R.layout.activity_main);
		tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (Check_Preferences.getAppPreferences(this, "SETEXIT_TYPE").equals("true")) {
			//인텐트
			Intent it = new Intent(this, SlideViewActivity.class);
			it.putExtra("SUB_URL", 		Check_Preferences.getAppPreferences(this, "SUB_URL"));
			it.putExtra("TITLE", 		Check_Preferences.getAppPreferences(this, "TITLE"));
			it.putExtra("NEW", 			Check_Preferences.getAppPreferences(this, "NEW"));
			it.putExtra("BUTTON", 		Check_Preferences.getAppPreferences(this, "BUTTON"));
			it.putExtra("BUTTON_URL", 	Check_Preferences.getAppPreferences(this, "BUTTON_URL"));
			startActivity(it);
			overridePendingTransition(0, 0); 
			finish();
			return;
		}
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);// 사용자 전화번호로 ID값 가져옴
		try {
			dataSet.PHONE = telManager.getLine1Number().toString().trim().replace("+82", "0").replace("82", "0"); //폰번호를 가져옴
			Log.e("SKY" , "폰번호 :: " + dataSet.PHONE);
			dataSet.PHONE_ID = telManager.getDeviceId();
		} catch (Exception e) {
			// TODO: handle exception
		}



		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		mContext = this;

		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		boolean isShortcut = prefs.getBoolean("isShortcut",false);
		boolean isAddShortcut = prefs.getBoolean("isAddShortcut",DEFINE.SHORT_CUT);
		boolean pushAgreeCheck = prefs.getBoolean("pushAgreeCheck", false);
		String tabstyle = prefs.getString("setBottomMenuStyle",DEFINE.BOTTOM_MENU_TABSTYLE);  
		String tabstyle2 = prefs.getString("setBottomMenuStyle2",DEFINE.BOTTOM_MENU_TABSTYLE2);  


		WebSetting();
		inint();


		Intent i = getIntent();
		openURL = i.getStringExtra("openurl");      
		homeURL = DEFINE.DEFAULT_URL;
		if(isAddShortcut&&!isShortcut){
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("isShortcut", true);
			editor.commit();
			addShortcut();
		}
		if(!tabstyle.equals("default") && !tabstyle.equals("")){
			Log.e("SKY", "tabstyle ::: " + tabstyle);
			setBottomMenuStyleColor(tabstyle);
		}
		if(!tabstyle2.equals("default") && !tabstyle.equals("")){
			Log.e("SKY", "tabstyle2 ::: " + tabstyle2);
			setBottomMenuStyleColor2(tabstyle2);
		}
		if(!pushAgreeCheck){
			//askPushAgree();
		}
		
		if (Check_Preferences.getAppPreferences(this, "bottomMenu").equals("GONE")) {
			bottomMenu.setVisibility(View.GONE);
		}else{
			bottomMenu.setVisibility(View.VISIBLE);
		}
		if (Check_Preferences.getAppPreferences(this, "bottomMenu2").equals("GONE")) {
			bottomMenu2.setVisibility(View.GONE);
		}else{
			bottomMenu2.setVisibility(View.VISIBLE);
		}
		Log.e("SKY" , "homeURL :: " + homeURL);
		//카톡 값 들어올때! 바로 url 태우기
		Intent intent = getIntent();
		Uri uri = intent.getData();
		if (uri != null) {
			key = uri.getQueryParameter("key");
			mWebView.loadUrl("" + key);
			return;
		}
		//GPS_Start();
		FirstUrl = homeURL + "?a=" + dataSet.latitude + "&b=" + dataSet.longitude + "&c=" + dataSet.address + "&d=" + dataSet.PHONE_ID+"&e="+key;
		mWebView.loadUrl(homeURL + "?a=" + dataSet.latitude + "&b=" + dataSet.longitude + "&c=" + dataSet.address + "&d=" + dataSet.PHONE_ID+"&e="+key);

	}

	private void inint(){
		mainBody = (RelativeLayout)findViewById(R.id.mainBody);
		bottomMenu = (LinearLayout)findViewById(R.id.bottomMenu);
		bottomMenu2 = (LinearLayout)findViewById(R.id.bottomMenu2);
		vi = (View)findViewById(R.id.loadingview);

		mProgressHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
		mProgressHorizontal.setProgress(R.drawable.progress_bar);

		btn1 = (ImageButton)findViewById(R.id.prevBtn);
		btn2 = (ImageButton)findViewById(R.id.nextBtn);
		btn3 = (ImageButton)findViewById(R.id.homeBtn);
		btn4 = (ImageButton)findViewById(R.id.reloadBtn);
		btn5 = (ImageButton)findViewById(R.id.shareBtn);

		findViewById(R.id.prevBtn).setOnClickListener(btnListener); 
		findViewById(R.id.nextBtn).setOnClickListener(btnListener); 
		findViewById(R.id.homeBtn).setOnClickListener(btnListener); 
		findViewById(R.id.reloadBtn).setOnClickListener(btnListener); 
		findViewById(R.id.shareBtn).setOnClickListener(btnListener); 

		findViewById(R.id.txt1).setOnClickListener(btnListener); 
		findViewById(R.id.txt2).setOnClickListener(btnListener); 
		findViewById(R.id.txt3).setOnClickListener(btnListener); 
		findViewById(R.id.txt4).setOnClickListener(btnListener); 
		findViewById(R.id.txt5).setOnClickListener(btnListener); 

		((TextView)findViewById(R.id.txt1)).setTypeface(ttf);
		((TextView)findViewById(R.id.txt2)).setTypeface(ttf);
		((TextView)findViewById(R.id.txt3)).setTypeface(ttf);
		((TextView)findViewById(R.id.txt4)).setTypeface(ttf);
		((TextView)findViewById(R.id.txt5)).setTypeface(ttf);

		btn1.getBackground().setAlpha(90);
		btn1.setClickable(false);  
		btn2.getBackground().setAlpha(90);
		btn2.setClickable(false);

	}
	private void WebSetting(){
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setVerticalScrollbarOverlay(true);
		mWebView.setHorizontalScrollbarOverlay(true);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);      
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);
		//mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportMultipleWindows(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setPluginState(PluginState. ON);
		mWebView.getSettings().setDefaultTextEncodingName(DEFINE.URI_ENCODE);
		mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString()+" Hybrid 2.0");
		mWebView.setWebChromeClient(new SMOWebChromeClient(this));
		mWebView.setWebViewClient(new ITGOWebChromeClient());
		if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
			mWebView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
		}
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.prevBtn:
				mWebView.goBack();
				break;
			case R.id.nextBtn:
				mWebView.goForward();
				break;
			case R.id.homeBtn:
				String homeURL = DEFINE.DEFAULT_URL;
				if(!openURL.equals("")) homeURL=openURL;
				clearHistory=true;
				mWebView.loadUrl(FirstUrl);
				break;
			case R.id.reloadBtn:
				clearApplicationCache(null);
				mWebView.clearCache(true);
				mWebView.reload();
				break;
			case R.id.shareBtn:
				shareUrl();
				break;
			case R.id.txt1:
				mWebView.stopLoading();
				mWebView.clearHistory();
				mWebView.loadUrl(DEFINE.TXT1);
				break;
			case R.id.txt2:
				mWebView.stopLoading();
				mWebView.clearHistory();
				mWebView.loadUrl(DEFINE.TXT2);
				break;
			case R.id.txt3:
				mWebView.stopLoading();
				mWebView.clearHistory();
				mWebView.loadUrl(DEFINE.TXT3);
				break;
			case R.id.txt4:
				mWebView.stopLoading();
				mWebView.clearHistory();
				mWebView.loadUrl(DEFINE.TXT4);
				break;
			case R.id.txt5:
				mWebView.stopLoading();
				mWebView.clearHistory();
				mWebView.loadUrl(DEFINE.TXT5);
				break;
			}
		}
	};
	class ITGOWebChromeClient extends WebViewClient {
		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			builder.setMessage("안전하지 않는 페이지 입니다.\n그래도 진행하시겠습니까?");
			builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.proceed();
				}
			});
			builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.cancel();
				}
			});
			final AlertDialog dialog = builder.create();
			dialog.show();
		}
		@Override //Tel,MailTo �±��϶� �׼Ǻ� ����Ʈ
		public boolean shouldOverrideUrlLoading(WebView view, String overrideUrl) {
			Log.e("SKY", "1overrideUrl :: " + overrideUrl);
			
			//인터넷 확인후 시작
			if (!checkNetwordState()) {
				Toast.makeText(getApplicationContext(), "인터넷 끊김! url노출 안됨.", 0).show();
				return true;
			}
			if (overrideUrl.startsWith("js2ios://")) {
				try{
					overrideUrl = URLDecoder.decode(overrideUrl, "UTF-8"); 
					SplitFun(overrideUrl);
					Log.e("SKY", "함수 시작");
				}catch(Exception e){
					Log.e("SKY", "e :: " + e.toString());

				} 

				return true;
			}
			if(overrideUrl.contains(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(overrideUrl);
				i.setDataAndType(uri, "video/mp4");
				startActivity(i);
				return super.shouldOverrideUrlLoading(view, overrideUrl);
			}else if(overrideUrl.startsWith("about:")){
				return true;    		   
			}else if(overrideUrl.startsWith("intent")||overrideUrl.startsWith("Intent")){
				Intent intent = null;
				try{
					intent = Intent.parseUri(overrideUrl,  Intent.URI_INTENT_SCHEME);
				}
				catch(URISyntaxException ex){
					Log.e("Browser", "Bad URI " +overrideUrl + ":" + ex.getMessage());
				}
				try{
					view.getContext().startActivity(intent.parseUri(overrideUrl,0));
				}catch(URISyntaxException e){
					e.printStackTrace();
				}catch(ActivityNotFoundException e){
					e.printStackTrace();
				}
				return true;
			} else if (overrideUrl.startsWith("ispmobile://")) {
				boolean isatallFlag = isPackageInstalled("kvp.jjy.MispAndroid320");
				if (isatallFlag) {
					boolean override = false;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
					try {
						startActivity(intent);
						override = true;
					} catch (ActivityNotFoundException ex) {
					}
					return override; 
				}else{
					show_msg("ISP ��ġ�� �ʿ��մϴ�.");
					return true;
				}
			} else if (overrideUrl.startsWith("paypin://")) {
				boolean isatallFlag = isPackageInstalled("com.skp.android.paypin");
				if (isatallFlag) {
					boolean override = false;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
					try {
						startActivity(intent);
						override = true;
					} catch (ActivityNotFoundException ex) {
					}
					return override; 
				}else{
					show_msg("PAYPIN ��ġ�� �ʿ��մϴ�.");
					return true;
				}		
			}else if(overrideUrl.startsWith("hybridapi://getDeviceId")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					getDeviceId(kw[1]);
				}
				return true;  	
			}else if(overrideUrl.startsWith("hybridapi://pushAgree")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					boolean tf = (kw[1].equals("true")) ? true:false;
					SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("pushEnable", tf);
					editor.commit();

					if(tf){
						Toast.makeText(mContext, getString(R.string.toast_push_agree), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(mContext, getString(R.string.toast_push_disagree), Toast.LENGTH_SHORT).show();
					}    				
				}
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://shareUrl")){
				shareUrl();
				return true;     			      
			}else if(overrideUrl.startsWith("hybridapi://makeShortCut")){
				addShortcut();
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://hideBottomMenu")){
				Check_Preferences.setAppPreferences(MainActivity.this, "bottomMenu" , "GONE");
				bottomMenu.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_hideBottomMenu")){
				Check_Preferences.setAppPreferences(MainActivity.this, "bottomMenu2" , "GONE");
				bottomMenu2.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				Check_Preferences.setAppPreferences(MainActivity.this, "bottomMenu" , "VISIBLE");
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_showBottomMenu")){
				Check_Preferences.setAppPreferences(MainActivity.this, "bottomMenu2" , "VISIBLE");
				bottomMenu2.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://setBottomMenuStyle")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle(kw[1]);
				}
				return true;       			
			} else if(overrideUrl.startsWith("hybridapi://new_setBottomMenuStyle")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle2(kw[1]);
				}
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://setActionStyle")){
				final String kw[] = overrideUrl.split("\\?");
				Log.e("SKY", "2overrideUrl :: " + overrideUrl);
				Log.e("SKY", "kw :: " + kw[1]);
				if(!kw[1].equals("")){
					Check_Preferences.setAppPreferences(mContext, "setActionStyle", kw[1] );
				}
				return true;       			
			}else if(overrideUrl.startsWith("http://") || overrideUrl.startsWith("https://")){
				Log.e("SKY", "can url :: " + overrideUrl);
				if(dataSet.paget(overrideUrl , MainActivity.this)){
					view.loadUrl(overrideUrl);
				}
				return true;
			}else {
				boolean override = false;
				if (overrideUrl.startsWith("sms:")) {
					Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("tel:")) {
					Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("mailto:")) {
					Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("geo:")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}else if(overrideUrl.startsWith("about:")){
					return true;
				}
				/*
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					startActivity(intent);
					override = true;
				}
				catch(ActivityNotFoundException ex) {}
				*/
				return override;
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon){
			super.onPageStarted(view, url, favicon);
			Log.e("SKY", "onPageStarted  *===== " + url);
			//프로그레스바 띄우기
			if (Check_Preferences.getAppPreferencesboolean(MainActivity.this, "PROGRESSBAR")) {
				if (dialog == null) {
					//Loading 뷰 가리기
					if (Check_Preferences.getAppPreferencesboolean(MainActivity.this, "PROGRESSBAR_3")) {
						vi.setVisibility(View.VISIBLE);
					}
					dialog = new ProgressDialog(mContext ,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					dialog.setMessage(getString(R.string.loading));
					dialog.setCancelable(false);
					dialog.show();
					if (Check_Preferences.getAppPreferencesboolean(MainActivity.this, "PROGRESSBAR_3")) {
						//					if (true) {
						new Handler().postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								Log.e("SKY", "3초지나면 프로그레스바 종료!!!");
								if (dialog != null) {
									dialog.dismiss();
									dialog = null;
									vi.setVisibility(View.INVISIBLE);
								}
							}
						}, 3000);// 0.5초 정도 딜레이를 준 후 시작
					}
				}
			}
			
		}

		@Override
		public void onPageFinished(WebView view, String url){
			super.onPageFinished(view, url);
			Log.e("SKY", "onPageFinished  *===== " + url);
			exit_type = true;

			//프로그레스바 끔.
			if (Check_Preferences.getAppPreferencesboolean(MainActivity.this, "PROGRESSBAR")) {
				//Loading 뷰 가리기
				if (Check_Preferences.getAppPreferencesboolean(MainActivity.this, "PROGRESSBAR_3")) {
					vi.setVisibility(View.GONE);
				}
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

			}
			
			mProgressHorizontal.setVisibility(View.GONE);

			if(clearHistory){
				mWebView.clearHistory();
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);
				btn2.getBackground().setAlpha(90);
				btn2.setClickable(false);
				clearHistory=false;
			}
			if(mWebView.canGoBack()){
				btn1.getBackground().setAlpha(255);
				btn1.setClickable(true);
			}else{
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);            		
			}
			if(mWebView.canGoForward()){
				btn2.getBackground().setAlpha(255);
				btn2.setClickable(true);
			}else{
				btn2.getBackground().setAlpha(90);
				btn2.setClickable(false);            		
			}


			//CookieSyncManager.getInstance().sync();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(
					view,
					errorCode,
					description,
					"<font id='altools-findtxt' style='color: rgb(0, 0, 0); font-size: 120%; font-weight: bold; background-color: rgb(255, 255, 0);'>failingUrl</font>");
			Log.e("webview_error", "1");
			switch (errorCode) {
			case ERROR_AUTHENTICATION: // 서버에서 사용자 인증 실패
			case ERROR_BAD_URL: // 잘못된 URL
			case ERROR_CONNECT: // 서버로 연결 실패
				Log.e("ERR!", "Code" + errorCode);
			case ERROR_FAILED_SSL_HANDSHAKE: // SSL handshake 수행 실패
			case ERROR_FILE: // 일반 파일 오류
			case ERROR_FILE_NOT_FOUND: // 파일을 찾을 수 없습니다
			case ERROR_HOST_LOOKUP: // 서버 또는 프록시 호스트 이름 조회 실패
			case ERROR_IO: // 서버에서 읽거나 서버로 쓰기 실패
			case ERROR_PROXY_AUTHENTICATION: // 프록시에서 사용자 인증 실패
			case ERROR_REDIRECT_LOOP: // 너무 많은 리디렉션
			case ERROR_TIMEOUT: // 연결 시간 초과
			case ERROR_TOO_MANY_REQUESTS: // 페이지 로드중 너무 많은 요청 발생
			case ERROR_UNKNOWN: // 일반 오류
			case ERROR_UNSUPPORTED_AUTH_SCHEME: // 지원되지 않는 인증 체계
			case ERROR_UNSUPPORTED_SCHEME:
				Toast.makeText(getApplicationContext(), "인터넷 끊김! url노출 안됨.", 0).show();
				Log.e("SKY", "CANGOBACK :: " + mWebView.canGoBack());
				mWebView.canGoBackOrForward(-2);
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						MainActivity.this);
//				builder.setPositiveButton("확인",
//						new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,
//							int which) {
//						//finish();
//					}
//				});
//				builder.setMessage("네트워크 상태가 원활하지 않습니다. 잠시 후 다시 시도해 주세요.");
//				builder.show();
				break; // URI가 지원되지 않는 방식
			}
		}  
	}
	class SMOWebChromeClient extends WebChromeClient{
		private View mCustomView;
		private Activity mActivity;

		public SMOWebChromeClient(Activity activity) {
			this.mActivity = activity;
		}
		@Override  
		public boolean onCreateWindow(WebView view, boolean dial, boolean userGesture, android.os.Message resultMsg) {        		
			super.onCreateWindow(view, dial, userGesture, resultMsg);

			if(popup) mainBody.removeView(pWebView);

			dialog = new ProgressDialog(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			dialog.setMessage(getString(R.string.loading));
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			});
			pWebView = new WebView(mContext);
			pWebView.getSettings().setJavaScriptEnabled(true);
			pWebView.getSettings().setBuiltInZoomControls(true);
			pWebView.getSettings().setSupportZoom(true);
			pWebView.getSettings().setUseWideViewPort(true);
			pWebView.getSettings().setLoadWithOverviewMode(true);
			pWebView.setWebChromeClient(this);
			pWebView.setWebViewClient(new WebAppViewClient(){
				@Override
				public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon){
					super.onPageStarted(view, url, favicon);
					dialog.show();
				}
				@Override
				public void onPageFinished(WebView view, String url){
					super.onPageFinished(view, url);
					dialog.dismiss();
				}    
			});   	        
			pWebView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			mainBody.addView(pWebView);

			WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
			transport.setWebView(pWebView);
			resultMsg.sendToTarget();
			popup=true;

			return true;
		}
		@Override
		public void onCloseWindow(WebView window) {
			super.onCloseWindow(window);

			closePop();
		}       	
		@Override
		public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
		{
			new AlertDialog.Builder(MainActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
			.setMessage(message)
			.setPositiveButton(getString(android.R.string.ok),
					new AlertDialog.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					result.confirm();
				}
			})
			.setCancelable(false)
			.create()
			.show();

			return true;
		};
		@Override  
		public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {           	
			new AlertDialog.Builder(view.getContext() , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)  
			.setTitle(getString(R.string.app_name))  
			.setMessage(message)  
			.setPositiveButton(getString(android.R.string.ok),  
					new AlertDialog.OnClickListener(){  
				public void onClick(DialogInterface dialog, int which) {  
					result.confirm();  
				}  
			})  
			.setNegativeButton(getString(android.R.string.cancel),   
					new AlertDialog.OnClickListener(){  
				public void onClick(DialogInterface dialog, int which) {  
					result.cancel();  
				}  
			})  
			.setCancelable(false)  
			.create()  
			.show();  
			return true;  
		}                           
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) { 
			//super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
		}             
		@Override
		public void onExceededDatabaseQuota(String url, String
				databaseIdentifier, long currentQuota, long estimatedSize,
				long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

			super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
					estimatedSize, totalUsedQuota, quotaUpdater);
		}        	
		@Override
		public void onProgressChanged(WebView view, int newProgress){
			mProgressHorizontal.setProgress(newProgress);
		}
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			MainActivity.this.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}     
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			MainActivity.this.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}             
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			MainActivity.this.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), MainActivity.FILECHOOSER_RESULTCODE);
		}  

	}
	private void askPushAgree(){
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();

		mCustomDialog2 = new CustomDialog(mContext, 
				getString(R.string.app_name),
				String.format( getString(R.string.push_agree),getString(R.string.app_name)),
				Gravity.CENTER,
				getString(R.string.agree),
				getString(android.R.string.cancel),
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editor.putBoolean("pushAgreeCheck", true);
				editor.putBoolean("pushEnable", true);
				editor.commit();
				mCustomDialog2.dismiss();
			}
		}, 
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editor.putBoolean("pushAgreeCheck", true);
				editor.putBoolean("pushEnable", false);
				editor.commit();
				mCustomDialog2.dismiss();
			}
		});
		mCustomDialog2.show();		
	}
	private void addShortcut() {
		String packagename=mContext.getPackageName();
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcutIntent.setClassName(mContext, packagename+".SplashActivity");
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(mContext, R.drawable.ic_launcher));
		intent.putExtra("duplicate", false);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

		sendBroadcast(intent);
	}	
	private void show_msg(String msg){
		mCustomDialog = new CustomDialog(mContext, 
				getString(R.string.app_name),
				msg,
				Gravity.CENTER,
				getString(android.R.string.yes),
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCustomDialog.dismiss();
			}
		});
		mCustomDialog.show();				
	}	
	@SuppressLint("NewApi")
	private void getDeviceId(final String callback){
		Log.e("SKY" , "-- getDeviceId --");
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final String device_id = prefs.getString("device_id","");
		final boolean pushEnable = prefs.getBoolean("pushEnable",false);

		if( Build.VERSION.SDK_INT < 19 ){
			mWebView.loadUrl("javascript:"+callback+"('"+device_id+"',"+pushEnable+");");
		}else{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					ValueCallback<String> resultCallback = null;
					mWebView.evaluateJavascript(callback+"('"+device_id+"',"+pushEnable+");",resultCallback);
				}
			});    			
		}		
	}
	private void setBottomMenuStyle(String style){
		if(style.equals("default") || style.equals("")){
			setBottomMenuStyleDefault();
		}else{
			setBottomMenuStyleColor(style);
		}
		Check_Preferences.setAppPreferences(MainActivity.this, "setBottomMenuStyle", style );
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();	
		editor.putString("tabstyle", style);
		editor.commit();		
	}
	private void setBottomMenuStyle2(String style){
		if(style.equals("default") || style.equals("")){
			setBottomMenuStyleDefault2();
		}else{
			setBottomMenuStyleColor2(style);
		}
		Check_Preferences.setAppPreferences(MainActivity.this, "setBottomMenuStyle2", style );
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();	
		editor.putString("tabstyle2", style);
		editor.commit();		
	}
	private void setBottomMenuStyleDefault(){
		bottomMenu.setBackgroundResource(R.drawable.tab_bg);
		btn1.setBackgroundResource(R.drawable.tab_prev_click);
		btn2.setBackgroundResource(R.drawable.tab_next_click);
		btn3.setBackgroundResource(R.drawable.tab_home_click);
		btn4.setBackgroundResource(R.drawable.tab_reload_click);
		btn5.setBackgroundResource(R.drawable.tab_share_click);		
	}	
	private void setBottomMenuStyleDefault2(){
		bottomMenu2.setBackgroundResource(R.drawable.tab_bg);
	}	
	private void setBottomMenuStyleColor(String tabstyle){
		bottomMenu.setBackgroundColor(Color.parseColor(tabstyle));
		btn1.setBackgroundResource(R.drawable.tab_prev_w_click);
		btn2.setBackgroundResource(R.drawable.tab_next_w_click);
		btn3.setBackgroundResource(R.drawable.tab_home_w_click);
		btn4.setBackgroundResource(R.drawable.tab_reload_w_click);
		btn5.setBackgroundResource(R.drawable.tab_share_w_click);		
	}
	private void setBottomMenuStyleColor2(String tabstyle){
		bottomMenu2.setBackgroundColor(Color.parseColor(tabstyle));
	}
	private void shareUrl(){
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name));
		i.putExtra(Intent.EXTRA_TEXT,mWebView.getUrl());
		startActivity(Intent.createChooser(i, mWebView.getTitle()+" "+getString(R.string.share_page)));		
	}
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Log.e("SKY", "pan :: " + pan);
		Log.e("SKY", "openURL :: " + openURL);
		if(popup&&keyCode == KeyEvent.KEYCODE_BACK&&pWebView.canGoBack()){
			pWebView.goBack();
			return true;
		}else if(popup&&keyCode == KeyEvent.KEYCODE_BACK){    		

			if( Build.VERSION.SDK_INT < 19 ){
				pWebView.loadUrl("javascript:self.close();");
			}else{
				runOnUiThread(new Runnable()
				{
					public void run()
					{
						ValueCallback<String> resultCallback = null;

						pWebView.evaluateJavascript("self.close()",resultCallback);
					}
				});    			
			}

			return true;    		
		}else if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
			pan = false;
			mWebView.goBack();
			return true;
		}else if(!pan&&(keyCode == KeyEvent.KEYCODE_BACK)){
			//종료 타입 1, 2
			if (Check_Preferences.getAppPreferences(MainActivity.this , "SETPOPEXIT_TYPE1" ).equals("true")) {
//			if (false) {
				//디폴트 종료하기
				final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				builder.setMessage("종료 하시겠습니까?");
				builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.e("SKY","AAA:: " + Check_Preferences.getAppPreferences(MainActivity.this , "SETEXIT_TYPE"));
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
				/*
				Intent i = new Intent(SlideViewActivity.this , ShowIMGActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				i.setComponent(new ComponentName(SlideViewActivity.this, ShowIMGActivity.class));
				i.putExtra("flag", true);
				i.putExtra("imgurl","http://img1.tmon.kr/deals/2017/03/08/520859290/front_3325a.jpg");
				i.putExtra("openurl","http://www.naver.com/");
				i.putExtra("bottomview","true");
				startActivity(i);
				*/
				showDialog(1);
				getExitAdAlertDialg(MainActivity.this);
//				//이미지 팝업 종료
//				mexitCustomDialog = new ExitCustomDialog(SlideViewActivity.this, 
//						"https://byunsooblog.files.wordpress.com/2014/06/find-in-path.png",
//						leftClickListener, 
//						rightClickListener);
//				mexitCustomDialog.show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void clearApplicationCache(java.io.File dir){
		if(dir==null)
			dir = getCacheDir();
		else;
		if(dir==null)
			return;
		else;
		java.io.File[] children = dir.listFiles();
		try{
			for(int i=0;i<children.length;i++)
				if(children[i].isDirectory())
					clearApplicationCache(children[i]);
				else
					children[i].delete();
		}
		catch(Exception e){}
	}
	@SuppressLint("NewApi")
	public class WebAppViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String overrideUrl){
			if(overrideUrl.contains(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(overrideUrl);
				i.setDataAndType(uri, "video/mp4");
				startActivity(i);
				return super.shouldOverrideUrlLoading(view, overrideUrl);
			}else if(overrideUrl.startsWith("about:")){
				return true;    		   
			}else if(overrideUrl.startsWith("http://")||overrideUrl.startsWith("https://")){
				view.loadUrl(overrideUrl);
				return true;
			}
			else if(overrideUrl.startsWith("intent")||overrideUrl.startsWith("Intent"))
			{
				Intent intent = null;
				try{
					intent = Intent.parseUri(overrideUrl,  Intent.URI_INTENT_SCHEME);
				}
				catch(URISyntaxException ex){
					Log.e("Browser", "Bad URI " +overrideUrl + ":" + ex.getMessage());
				}
				try{
					view.getContext().startActivity(intent.parseUri(overrideUrl,0));
				}catch(URISyntaxException e){
					e.printStackTrace();
				}catch(ActivityNotFoundException e){
					e.printStackTrace();
				}
				return true;
			} else if (overrideUrl.startsWith("ispmobile://")) {
				boolean isatallFlag = isPackageInstalled("kvp.jjy.MispAndroid320");
				if (isatallFlag) {
					boolean override = false;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
					try {
						startActivity(intent);
						override = true;
					} catch (ActivityNotFoundException ex) {
					}
					return override; 
				}else{
					show_msg("ISP ��ġ�� �ʿ��մϴ�.");
					return true;
				}
			} else if (overrideUrl.startsWith("paypin://")) {
				boolean isatallFlag = isPackageInstalled("com.skp.android.paypin");
				if (isatallFlag) {
					boolean override = false;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
					try {
						startActivity(intent);
						override = true;
					} catch (ActivityNotFoundException ex) {
					}
					return override; 
				}else{
					show_msg("PAYPIN ��ġ�� �ʿ��մϴ�.");
					return true;
				}		
			}else if(overrideUrl.startsWith("hybridapi://getDeviceId")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					getDeviceId(kw[1]);
				}
				return true;  	
			}else if(overrideUrl.startsWith("hybridapi://pushAgree")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					boolean tf = (kw[1].equals("true")) ? true:false;
					SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("pushEnable", tf);
					editor.commit();

					if(tf){
						Toast.makeText(mContext, getString(R.string.toast_push_agree), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(mContext, getString(R.string.toast_push_disagree), Toast.LENGTH_SHORT).show();
					}    				
				}
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://shareUrl")){
				shareUrl();
				return true;     			      
			}else if(overrideUrl.startsWith("hybridapi://makeShortCut")){
				addShortcut();
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://hideBottomMenu")){
				bottomMenu.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://hideBottomMenu2")){
				bottomMenu2.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu2")){
				bottomMenu2.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://setBottomMenuStyle")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle(kw[1]);
				}
				return true;       			
			} else if(overrideUrl.startsWith("hybridapi://setBottomMenuStyle2")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle2(kw[1]);
				}
				return true;       			
			} 		
			else {
				boolean override = false;
				if (overrideUrl.startsWith("sms:")) {
					Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("tel:")) {
					Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("mailto:")) {
					Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}
				if (overrideUrl.startsWith("geo:")) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					startActivity(i);
					return true;
				}else if(overrideUrl.startsWith("about:")){
					return true;
				}
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(overrideUrl));
					startActivity(intent);
					override = true;
				}
				catch(ActivityNotFoundException ex) {}
				return override;
			}    		
		}
	}
	public void closePop(){
		mainBody.removeView(pWebView);
		popup=false;		
	}
	public static boolean isPackageInstalled(String pkgName) {
		try {
			mContext.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	@Override
	protected void onResume(){
		super.onResume();
		Log.e("SKY", "exit_type :: " + exit_type);
		if (exit_type) {
			if (Check_Preferences.getAppPreferences(MainActivity.this , "SETEXIT_TYPE").equals("true") ) {
				finish();
			}
		}

		//GPS_Start();
		CookieSyncManager.getInstance().startSync();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		CookieSyncManager.getInstance().stopSync();
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
			mWebView.loadUrl("javascript:appLoginCallback('"+type+"', '"+data+"')");
		}else if(requestCode == 120){
			if(resultCode==120){
				mWebView.loadUrl(DEFINE.SETTING_120);
			}
			//로그인시
			if(resultCode==121){
				//spu.put("islogin", 0);
				mWebView.loadUrl(DEFINE.SETTING_121);
				CookieManager cookieManager = CookieManager.getInstance();
				cookieManager.removeSessionCookie();
			}
			//로그아웃시
			if(resultCode==130){
				mWebView.loadUrl(DEFINE.SETTING_130);
			}
			//공지
			if(resultCode==131){
				mWebView.loadUrl(DEFINE.SETTING_131);
			}
			if(resultCode==132){
				mWebView.loadUrl(DEFINE.SETTING_132);
			}
			if(resultCode==133){
				mWebView.loadUrl(DEFINE.SETTING_133);
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
				mWebView.loadUrl("javascript:putlocationsetting('"+data+"');");
				//				String url = mWebView.getUrl();
				//				url = url.substring(0, url.indexOf("#"));
				//				url = url.substring(0, url.indexOf("?"));
				//				mWebView.loadUrl(url+"?address="+address+"&lat="+lat+"&lng="+lng+"&deviceid="+tMgr.getDeviceId());
			} catch (JSONException e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	private void SplitFun(String url){
		url = url.replace("js2ios://", "");
		String Fun = url.substring(0, url.indexOf("?"));
		Log.e("SKY", "Fun :: "+Fun);
		String param[] = url.split("&");
		String val[]  = new String[param.length];
		Log.e("SKY", "parameter ea :: "+param.length);
		String par = "" , return_fun = "";
		for (int i = 0; i < param.length; i++) {
			//Log.e("SKY", "parameter ea :: " + "i :: " + i + " ::" +param[i]);
			val[i] = param[i].substring(param[i].indexOf("=")+1, param[i].length());
			Log.e("SKY", "parameter ea :: " + "i :: " + i + " ::" +val[i]);
			if (i == 0) {
				par = val[i];
			}else if( i == (param.length-1)){
				return_fun = val[i];
			}else{
				par += "," +val[i];
			}
		}
		try {
			//String parameter
			Class[] paramString = new Class[4];
			paramString[0] = String.class;
			paramString[1] = Activity.class;
			paramString[2] = WebView.class;
			paramString[3] = String.class;
			@SuppressWarnings("rawtypes")
			Class cls = Class.forName("co.kr.hybridapp.common.FunNative");
			Object obj = cls.newInstance();
			//call the printIt method
			Method method = cls.getDeclaredMethod(Fun, paramString);
			method.invoke(obj, new String(par) , MainActivity.this , mWebView , new String(return_fun));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	private void GPS_Start(){
		//2초동안 응답 없으면 빈값 으로 보내기
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Log.e("SKY", "2초지나면 바로호출!!");
				if (FirstLoadUrl) {
					FirstLoadUrl = false;
					First_Flag = true;
					FirstUrl = homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + address + "&d=" + dataSet.PHONE_ID;
					Log.e("SKY", "gogo ::!" + homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + "" + "&d=" + dataSet.PHONE_ID);
					mWebView.loadUrl(homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + getAddress(mContext , latitude , longitude) + "&d=" + dataSet.PHONE_ID +"&e="+key);
				}
			}
		}, 2000);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.e("SKY" , "latitude :: onLocationChanged");
				String gps = android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
				if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
					latitude = 0;
					longitude = 0;
				} else {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					address = getAddress(mContext , latitude , longitude);
					Log.e("SKY" , "GPS :: latitude :: " + latitude + "//longitude :: " + longitude);
					//최초 한번만
					if (FirstLoadUrl) {
						FirstLoadUrl = false;
						First_Flag = true;
						FirstUrl = homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + address + "&d=" + dataSet.PHONE_ID;
						mWebView.loadUrl(homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + getAddress(mContext , latitude , longitude) + "&d=" + dataSet.PHONE_ID+"&e="+key);
					}

				}
				locationManager.removeUpdates(locationListener);
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.e("SKY" , "onStatusChanged");
			}
			public void onProviderEnabled(String provider) {
				Log.e("SKY" , "onProviderEnabled");
			}
			public void onProviderDisabled(String provider) {
				Log.e("SKY" , "onProviderDisabled");
				latitude = 0;
				longitude = 0;
				if (!First_Flag) {
					First_Flag = true;
					FirstLoadUrl = false;
					FirstUrl = homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + "" + "&d=" + dataSet.PHONE_ID;
					mWebView.loadUrl(homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + "" + "&d=" + dataSet.PHONE_ID+"&e="+key);
				}
			}
		};
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
	}
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		return getExitAdAlertDialg(this);
	}	
	public static AlertDialog getExitAdAlertDialg(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("종료").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				activity.finish();
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		WebView webView = new WebView(activity);
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true); // wide viewport를 사용하도록 설정
		webSettings.setBuiltInZoomControls(false); // 확대 축소 false
		webSettings.setSupportZoom(false); // disable pinch zoom
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setScrollContainer(false);
		webView.loadUrl(DEFINE.EXITPOP);
		
		if (!GetInternet(activity)) {
			webView.setVisibility(View.GONE);
		}else{
			webView.setVisibility(View.VISIBLE);
		}
		webView.setWebViewClient(new WebViewClient() {
			ProgressDialog progressDialog;
			private Context mContext;
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				this.mContext = view.getContext();
				Log.e("SKY" , "url ::" + url);
				if (url.startsWith("bible25:blank")) {
					Log.e("SKY" , "launchExternalBrowser ::");

					return UtilFunction.launchExternalBrowser(mContext, url);
				} else if (url.startsWith("bible25:launch_app")) {
					Log.e("SKY" , "startAppProcess ::");

					int startPos = url.indexOf('?');
					String packageName = url.substring(startPos + 1);
					return UtilFunction.startAppProcess(mContext, "bible25:start_app?packageName=" + packageName);
				} else {
					Log.e("SKY" , "else ::");

					view.loadUrl(url);
					return true;
				}
			}
			@SuppressWarnings("deprecation")
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(activity);
				}
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setMessage("로딩중입니다. 잠시 기다려주세요.");
				progressDialog.setCancelable(false);
				//progressDialog.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (progressDialog.isShowing()) {
					//progressDialog.dismiss();
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.setView(webView);
		return alert;
	}
	private static Boolean GetInternet(Activity ac){
		ConnectivityManager connManager =(ConnectivityManager)ac.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo state_3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    NetworkInfo state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo state_blue = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
	    Boolean val = true;
	    if (state_3g != null) {
			val = state_3g.isConnected() || state_wifi.isConnected()|| state_blue.isConnected();
		}else{
			val = state_wifi.isConnected()|| state_blue.isConnected();
		}
	    return val;
	}
	
}
