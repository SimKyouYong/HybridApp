package co.kr.hybridapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.Browser;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
import android.widget.Toast;
import co.kr.hybridapp.common.ActivityEx;
import co.kr.hybridapp.common.CommonUtil;
import co.kr.hybridapp.common.CustomDialog;
import co.kr.hybridapp.common.DEFINE;


@SuppressLint("JavascriptInterface")
public class MainActivity extends ActivityEx implements LocationListener {

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
	public static double latitude = 0;
	public static double longitude=0;

	boolean popup = false;
	private boolean pan = false;
	private boolean clearHistory = false;

	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;

	public RelativeLayout mainBody;
	public LinearLayout bottomMenu;
	public View vi;

	private CustomDialog mCustomDialog,mCustomDialog2;

	@SuppressLint({"SetJavaScriptEnabled","NewApi"})
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("SKY" , "onCreate");

		setContentView(R.layout.activity_main);
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
		String tabstyle = prefs.getString("tabstyle",DEFINE.BOTTOM_MENU_TABSTYLE);  


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
		if(!tabstyle.equals("default")){
			setBottomMenuStyleColor(tabstyle);
		}
		if(!pushAgreeCheck){
			askPushAgree();
		}
		Log.e("SKY" , "homeURL :: " + homeURL);
//		mWebView.loadUrl(homeURL);
	}
	
	private void inint(){
		mainBody = (RelativeLayout)findViewById(R.id.mainBody);
		bottomMenu = (LinearLayout)findViewById(R.id.bottomMenu);
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

		btn1.getBackground().setAlpha(90);
		btn1.setClickable(false);  
		btn2.getBackground().setAlpha(90);
		btn2.setClickable(false);
		GPS_Start();
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
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportMultipleWindows(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setPluginState(PluginState. ON);
		mWebView.getSettings().setDefaultTextEncodingName(DEFINE.URI_ENCODE);
		mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString()+" Hybrid 2.0");
		mWebView.setWebChromeClient(new SMOWebChromeClient(this));
		mWebView.setWebViewClient(new ITGOWebChromeClient());
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
			//인터넷 확인후 시작
			if (!checkNetwordState()) {
				Toast.makeText(getApplicationContext(), "인터넷 끊김! url노출 안됨.", 0).show();
				return true;
			}
			if (overrideUrl.startsWith("js2ios://")) {
				mWebView.stopLoading();
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
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://setBottomMenuStyle")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle(kw[1]);
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

		@Override
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon){
			super.onPageStarted(view, url, favicon);
			

			//mProgressHorizontal.setVisibility(View.VISIBLE);

			


			//프로그레스바 띄우기
			if (DEFINE.PROGRESSBAR) {
				dialog = new ProgressDialog(mContext ,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				dialog.setMessage(getString(R.string.loading));
				dialog.setCancelable(false);
				dialog.show();
			}
			//Loading 뷰 가리기
			if (DEFINE.LOADINGVIEW) {
				vi.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url){
			super.onPageFinished(view, url);
			Log.e("SKY", "onPageFinished = = = = = = = "+url);
			//프로그레스바 끔.
			if (DEFINE.PROGRESSBAR) {
				dialog.dismiss();
			}
			//Loading 뷰 가리기
			if (DEFINE.LOADINGVIEW) {
				vi.setVisibility(View.GONE);
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
			super.onReceivedError(view, errorCode, description, failingUrl);
			//Toast.makeText(getApplicationContext(), "Error: "+description, Toast.LENGTH_SHORT).show();
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
	private void finish_popup(){
		Toast.makeText(mContext, getString(R.string.back_finish), Toast.LENGTH_LONG).show();
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
		if(style.equals("default")){
			setBottomMenuStyleDefault();
		}else{
			setBottomMenuStyleColor(style);
		}
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();	
		editor.putString("tabstyle", style);
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
	private void setBottomMenuStyleColor(String tabstyle){
		bottomMenu.setBackgroundColor(Color.parseColor(tabstyle));
		btn1.setBackgroundResource(R.drawable.tab_prev_w_click);
		btn2.setBackgroundResource(R.drawable.tab_next_w_click);
		btn3.setBackgroundResource(R.drawable.tab_home_w_click);
		btn4.setBackgroundResource(R.drawable.tab_reload_w_click);
		btn5.setBackgroundResource(R.drawable.tab_share_w_click);		
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
		}else if(!pan&&openURL.equals("")&&(keyCode == KeyEvent.KEYCODE_BACK)){
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			builder.setMessage("종료 하시겠습니까?");
			builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
//			finish_popup();

//			pan = true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		clearApplicationCache(null);
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
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://setBottomMenuStyle")){
				final String kw[] = overrideUrl.split("\\?");
				if(!kw[1].equals("")){
					setBottomMenuStyle(kw[1]);
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
		GPS_Start();
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
	private void GPS_Start(){
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
					Log.e("SKY" , "GPS :: latitude :: " + latitude + "//longitude :: " + longitude);
					//최초 한번만
					if (FirstLoadUrl) {
						FirstLoadUrl = false;
						FirstUrl = homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + getAddress(mContext , latitude , longitude) + "&d=" + dataSet.PHONE_ID;
						mWebView.loadUrl(homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + getAddress(mContext , latitude , longitude) + "&d=" + dataSet.PHONE_ID);
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
				FirstUrl = homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + "" + "&d=" + dataSet.PHONE_ID;
				mWebView.loadUrl(homeURL + "?a=" + latitude + "&b=" + longitude + "&c=" + "" + "&d=" + dataSet.PHONE_ID);

			}
		};
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
	}
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
}
