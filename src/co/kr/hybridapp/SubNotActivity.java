package co.kr.hybridapp;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.support.v4.app.Fragment.InstantiationException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.common.Check_Preferences;
import co.kr.hybridapp.common.CustomDialog;
import co.kr.hybridapp.common.DEFINE;

public class SubNotActivity extends Activity {
	WebView wc;
	public View vi;
	public static  LinearLayout mainBody;
	public static String SUB_URL;
	public static String TITLE;
	public static String NEW;
	public static String BUTTON;
	public static String BUTTON_URL;
	ProgressDialog dialog;
	Button bt;
	boolean popup = false;
	WebView pWebView;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private CustomDialog mCustomDialog,mCustomDialog2;
	private static Context mContext;
	private Typeface ttf;
	ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
	public LinearLayout bottomMenu,bottomMenu2;
	private boolean clearHistory = false;
	String openURL="";
	TelephonyManager tMgr;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subnot);
		mContext = this;
		tMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		
		ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");

		SUB_URL = getIntent().getStringExtra("SUB_URL");
		TITLE = getIntent().getStringExtra("TITLE");
		NEW = getIntent().getStringExtra("NEW");
		BUTTON = getIntent().getStringExtra("BUTTON");
		BUTTON_URL = getIntent().getStringExtra("BUTTON_URL");

		Log.e("SKY" , "SUB_URL :: " + SUB_URL);
		Log.e("SKY" , "TITLE :: " + TITLE);
		Log.e("SKY" , "NEW :: " + NEW);
		Log.e("SKY" , "BUTTON :: " + BUTTON);
		Log.e("SKY" , "BUTTON_URL :: " + BUTTON_URL);

		wc = (WebView)findViewById(R.id.webview);
		vi = (View)findViewById(R.id.loadingview);
		bottomMenu = (LinearLayout)findViewById(R.id.bottomMenu);
		bottomMenu2 = (LinearLayout)findViewById(R.id.bottomMenu2);

		
		btn1 = (ImageButton)findViewById(R.id.prevBtn);
		btn2 = (ImageButton)findViewById(R.id.nextBtn);
		btn3 = (ImageButton)findViewById(R.id.homeBtn);
		btn4 = (ImageButton)findViewById(R.id.reloadBtn);
		btn5 = (ImageButton)findViewById(R.id.shareBtn);

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
		
		if (Check_Preferences.getAppPreferences(this, "bottomMenu").equals("GONE")) {
			bottomMenu.setVisibility(View.GONE);
		}else{
			bottomMenu.setVisibility(View.VISIBLE);
			setBottomMenuStyle(Check_Preferences.getAppPreferences(this, "setBottomMenuStyle"));
		}
		
		findViewById(R.id.prevBtn).setOnClickListener(btnListener); 
		findViewById(R.id.nextBtn).setOnClickListener(btnListener); 
		findViewById(R.id.homeBtn).setOnClickListener(btnListener); 
		findViewById(R.id.reloadBtn).setOnClickListener(btnListener); 
		findViewById(R.id.shareBtn).setOnClickListener(btnListener); 

		btn1.getBackground().setAlpha(90);
		btn1.setClickable(false);  
		btn2.getBackground().setAlpha(90);
		btn2.setClickable(false);

		mainBody = (LinearLayout)findViewById(R.id.mainBody);
		bt = (Button)findViewById(R.id.btn_right);
		bt.setText("" + BUTTON);
		bt.setTypeface(ttf);

		TextView tt = (TextView)findViewById(R.id.title);
		tt.setText("" + TITLE);
		tt.setTypeface(ttf);




		wc.getSettings().setJavaScriptEnabled(true);
		wc.setVerticalScrollbarOverlay(true);
		wc.setHorizontalScrollbarOverlay(true);
		wc.setHorizontalScrollBarEnabled(false);
		wc.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);      
		wc.getSettings().setJavaScriptEnabled(true);
		wc.getSettings().setDomStorageEnabled(true);
		wc.getSettings().setAppCacheEnabled(true);
		wc.getSettings().setBuiltInZoomControls(true);
		wc.getSettings().setDisplayZoomControls(false);
		wc.getSettings().setSupportZoom(true);
		wc.getSettings().setUseWideViewPort(true);
		wc.getSettings().setLoadWithOverviewMode(true);
		wc.getSettings().setSupportMultipleWindows(true);
		wc.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wc.getSettings().setPluginState(PluginState. ON);
		wc.getSettings().setDefaultTextEncodingName(DEFINE.URI_ENCODE);
		wc.getSettings().setUserAgentString(wc.getSettings().getUserAgentString()+" Hybrid 2.0");
		wc.setWebChromeClient(new SMOWebChromeClient(this));
		wc.setWebViewClient(new ITGOWebChromeClient());


		wc.loadUrl(SUB_URL);

		findViewById(R.id.btn_back).setOnClickListener(btnListener);
		findViewById(R.id.btn_right).setOnClickListener(btnListener);


	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_back:	
				Log.e("SKY" , "btn_back");
				finish();
				break;
			case R.id.btn_right:	
				Log.e("SKY" , "btn_right");
				if (BUTTON_URL.equals("") || BUTTON_URL == null) {
					//스크립트 함수 호출
					wc.loadUrl("javascript:"+"getRightButton" + "()");
				}else{
					wc.loadUrl(BUTTON_URL);
				}
				
				break;
			case R.id.prevBtn:
				wc.goBack();
				break;
			case R.id.nextBtn:
				wc.goForward();
				break;
			case R.id.homeBtn:
				String homeURL = SUB_URL;
				if(!openURL.equals("")) homeURL=openURL;
				clearHistory=true;
				wc.loadUrl(homeURL);
				break;
			case R.id.reloadBtn:
				clearApplicationCache(null);
				wc.clearCache(true);
				wc.reload();
				break;
			case R.id.shareBtn:
				shareUrl();
				break;
			case R.id.txt1:
				wc.loadUrl(DEFINE.TXT1);
				break;
			case R.id.txt2:
				wc.loadUrl(DEFINE.TXT2);
				break;
			case R.id.txt3:
				wc.loadUrl(DEFINE.TXT3);
				break;
			case R.id.txt4:
				wc.loadUrl(DEFINE.TXT4);
				break;
			case R.id.txt5:
				wc.loadUrl(DEFINE.TXT5);
				break;
			}
		}
	};
	private void setBottomMenuStyle(String style){
		if(style.equals("default") || style.equals("")){
			setBottomMenuStyleDefault();
		}else{
			setBottomMenuStyleColor(style);
		}
		Check_Preferences.setAppPreferences(this, "setBottomMenuStyle", style );
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
		i.putExtra(Intent.EXTRA_TEXT,wc.getUrl());
		startActivity(Intent.createChooser(i, wc.getTitle()+" "+getString(R.string.share_page)));		
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
	class ITGOWebChromeClient extends WebViewClient {
		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(SubNotActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
						Toast.makeText(SubNotActivity.this, getString(R.string.toast_push_agree), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(SubNotActivity.this, getString(R.string.toast_push_disagree), Toast.LENGTH_SHORT).show();
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
				Check_Preferences.setAppPreferences(SubNotActivity.this, "bottomMenu" , "GONE");
				bottomMenu.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_hideBottomMenu")){
				Check_Preferences.setAppPreferences(SubNotActivity.this, "bottomMenu2" , "GONE");
				bottomMenu2.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				Check_Preferences.setAppPreferences(SubNotActivity.this, "bottomMenu" , "VISIBLE");
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_showBottomMenu")){
				Check_Preferences.setAppPreferences(SubNotActivity.this, "bottomMenu2" , "VISIBLE");
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
			}else if(overrideUrl.startsWith("hybridapi://settingtitle")){
				//타이틀 바 변경 : ex)로그인 & 저장 & 로그아웃 기능 

			} else if(overrideUrl.startsWith("hybridapi://setRightButton")){
				final String kw[] = overrideUrl.split("\\?");
				Log.e("SKY", "kw1 :: " + kw[1]);
				String str2 ="";
				try {
					str2 = URLDecoder.decode( kw[1] , "UTF-8" );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final String kw1[] = str2.split(",");
				bt.setText("" + kw1[1]);
				BUTTON_URL = kw1[2];
				return true;  	
			} else {
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
			return false;  
		}

		@Override
		public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon){
			super.onPageStarted(view, url, favicon);
			if (url.indexOf("js2ios://") != -1) {
				wc.stopLoading();
				try{
					url = URLDecoder.decode(url, "UTF-8"); 
					SplitFun(url);
					Log.e("SKY", "함수 시작");
				}catch(Exception e){
					Log.e("SKY", "e :: " + e.toString());

				} 

				return;
			}


			//인터넷 확인후 시작
			if (!checkNetwordState()) {
				Toast.makeText(SubNotActivity.this, "인터넷 끊김! url노출 안됨.", 0).show();
				wc.stopLoading();
				return ;
			}


			//프로그레스바 띄우기
			if (Check_Preferences.getAppPreferencesboolean(SubNotActivity.this, "PROGRESSBAR")) {
				if (dialog == null) {
					dialog = new ProgressDialog(SubNotActivity.this ,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					dialog.setMessage(getString(R.string.loading));
					dialog.setCancelable(false);
					dialog.show();
					if (Check_Preferences.getAppPreferencesboolean(SubNotActivity.this, "PROGRESSBAR_3")) {
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
			if (Check_Preferences.getAppPreferencesboolean(SubNotActivity.this, "PROGRESSBAR")) {
				dialog.dismiss();
			}
			//Loading 뷰 가리기
			if (DEFINE.LOADINGVIEW) {
				vi.setVisibility(View.GONE);
			}
			if(clearHistory){
				wc.clearHistory();
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);
				btn2.getBackground().setAlpha(90);
				btn2.setClickable(false);
				clearHistory=false;
			}
			if(wc.canGoBack()){
				btn1.getBackground().setAlpha(255);
				btn1.setClickable(true);
			}else{
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);            		
			}
			if(wc.canGoForward()){
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
			super.onReceivedError(view, errorCode, description, 
					"<font id='altools-findtxt' style='color: rgb(0, 0, 0); font-size: 120%; font-weight: bold; background-color: rgb(255, 255, 0);'>failingUrl</font>");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					getBaseContext());
			builder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					finish();
				}
			});
			builder.setMessage("네트워크 상태가 원활하지 않습니다. 잠시 후 다시 시도해 주세요.");
			builder.show();
		}  
	}
	private void setBottomMenuStyle2(String style){
		if(style.equals("default") || style.equals("")){
			setBottomMenuStyleDefault2();
		}else{
			setBottomMenuStyleColor2(style);
		}
		Check_Preferences.setAppPreferences(SubNotActivity.this, "setBottomMenuStyle2", style );
		SharedPreferences prefs = getSharedPreferences("co.kr.hybrid", MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();	
		editor.putString("tabstyle2", style);
		editor.commit();		
	}
	private void setBottomMenuStyleColor2(String tabstyle){
		bottomMenu2.setBackgroundColor(Color.parseColor(tabstyle));
	}
	private void setBottomMenuStyleDefault2(){
		bottomMenu2.setBackgroundResource(R.drawable.tab_bg);
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

			dialog = new ProgressDialog(getApplicationContext(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			dialog.setMessage(getString(R.string.loading));
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			});
			pWebView = new WebView(SubNotActivity.this);
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
					//dialog.show();
				}
				@Override
				public void onPageFinished(WebView view, String url){
					super.onPageFinished(view, url);
					//dialog.dismiss();
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
			new AlertDialog.Builder(SubNotActivity.this , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
			//mProgressHorizontal.setProgress(newProgress);
		}
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}     
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}             
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}  

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
	private void show_msg(String msg){
		mCustomDialog = new CustomDialog(SubNotActivity.this, 
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
	public void closePop(){
		mainBody.removeView(pWebView);
		popup=false;		
	}
	public boolean checkNetwordState() {
		ConnectivityManager connManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo state_3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo state_blue = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

		return state_3g.isConnected() || state_wifi.isConnected()|| state_blue.isConnected();
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
			Object obj = null;
			try {
				obj = cls.newInstance();
			} catch (java.lang.InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//call the printIt method
			Method method = cls.getDeclaredMethod(Fun, paramString);
			method.invoke(obj, new String(par) , SubNotActivity.this , wc , new String(return_fun));
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
					SharedPreferences prefs = getSharedPreferences("co.kr.hybrid",MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("pushEnable", tf);
					editor.commit();

					if(tf){
						Toast.makeText(SubNotActivity.this, getString(R.string.toast_push_agree), Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(SubNotActivity.this, getString(R.string.toast_push_disagree), Toast.LENGTH_SHORT).show();
					}    				
				}
				return true;      			
			} else if(overrideUrl.startsWith("hybridapi://setRightButton")){
				final String kw[] = overrideUrl.split("\\?");
				Log.e("SKY", "kw1 :: " + kw[0]);
				Log.e("SKY", "kw2 :: " + kw[1]);
				String str2 ="";
				try {
					str2 = URLDecoder.decode( kw[1] , "UTF-8" );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final String kw1[] = str2.split(",");
				bt.setText("" + kw1[0]);
				BUTTON_URL = kw1[1];
				return true;  	
			} else {
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
	@Override
	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK&&wc.canGoBack()){
			wc.goBack();
			return true;
		}else{
			finish();
		}

		return super.onKeyDown(keyCode, event);
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
