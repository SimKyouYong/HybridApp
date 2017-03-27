package co.kr.hybridapp.fregment;

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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.hybridapp.MainActivity;
import co.kr.hybridapp.R;
import co.kr.hybridapp.SlideViewActivity;
import co.kr.hybridapp.common.Check_Preferences;
import co.kr.hybridapp.common.CustomDialog;
import co.kr.hybridapp.common.DEFINE;
import co.kr.hybridapp.common.FragmentEx;

public class SlideViewFregment extends FragmentEx implements OnTouchListener{
	private static Context mContext;
	Activity av_;    //  액티비티 av_로 지정

	boolean popup = false;
	public static  RelativeLayout mainBody;
	WebView pWebView;
	private CustomDialog mCustomDialog,mCustomDialog2;
	ProgressDialog dialog;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	public View vi;
	String openURL="";

	ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
	public LinearLayout bottomMenu , bottomMenu2;
	private boolean clearHistory = false;
	private Typeface ttf;

	public SlideViewFregment(Context context , Activity av , int tag) {  //  액티비티 지정, 프래그먼트
		mContext = context;  
		av_ = av;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		Log.e("SKY" , "--MAIN START--");
		ttf = Typeface.createFromAsset(av_.getAssets(), "HANYGO230.TTF");

		View view = inflater.inflate(R.layout.activity_slidefregment, null);
		SlideViewActivity.wc = (WebView)view.findViewById(R.id.webview);
		vi = (View)view.findViewById(R.id.loadingview);
		mainBody = (RelativeLayout)view.findViewById(R.id.mainBody);
		bottomMenu = (LinearLayout)view.findViewById(R.id.bottomMenu);
		bottomMenu2 = (LinearLayout)view.findViewById(R.id.bottomMenu2);

		btn1 = (ImageButton)view.findViewById(R.id.prevBtn);
		btn2 = (ImageButton)view.findViewById(R.id.nextBtn);
		btn3 = (ImageButton)view.findViewById(R.id.homeBtn);
		btn4 = (ImageButton)view.findViewById(R.id.reloadBtn);
		btn5 = (ImageButton)view.findViewById(R.id.shareBtn);

		view.findViewById(R.id.prevBtn).setOnClickListener(btnListener); 
		view.findViewById(R.id.nextBtn).setOnClickListener(btnListener); 
		view.findViewById(R.id.homeBtn).setOnClickListener(btnListener); 
		view.findViewById(R.id.reloadBtn).setOnClickListener(btnListener); 
		view.findViewById(R.id.shareBtn).setOnClickListener(btnListener); 

		view.findViewById(R.id.txt1).setOnClickListener(btnListener); 
		view.findViewById(R.id.txt2).setOnClickListener(btnListener); 
		view.findViewById(R.id.txt3).setOnClickListener(btnListener); 
		view.findViewById(R.id.txt4).setOnClickListener(btnListener); 
		view.findViewById(R.id.txt5).setOnClickListener(btnListener); 

		((TextView)view.findViewById(R.id.txt1)).setTypeface(ttf);
		((TextView)view.findViewById(R.id.txt2)).setTypeface(ttf);
		((TextView)view.findViewById(R.id.txt3)).setTypeface(ttf);
		((TextView)view.findViewById(R.id.txt4)).setTypeface(ttf);
		((TextView)view.findViewById(R.id.txt5)).setTypeface(ttf);
		
		if (Check_Preferences.getAppPreferences(mContext, "bottomMenu").equals("GONE")) {
			bottomMenu.setVisibility(View.GONE);
		}else{
			bottomMenu.setVisibility(View.VISIBLE);
			setBottomMenuStyle(Check_Preferences.getAppPreferences(mContext, "setBottomMenuStyle"));
		}
		
		btn1.getBackground().setAlpha(90);
		btn1.setClickable(false);  
		btn2.getBackground().setAlpha(90);
		btn2.setClickable(false);


		SlideViewActivity.wc.getSettings().setJavaScriptEnabled(true);
		SlideViewActivity.wc.setVerticalScrollbarOverlay(true);
		SlideViewActivity.wc.setHorizontalScrollbarOverlay(true);
		SlideViewActivity.wc.setHorizontalScrollBarEnabled(false);
		SlideViewActivity.wc.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);      
		SlideViewActivity.wc.getSettings().setJavaScriptEnabled(true);
		SlideViewActivity.wc.getSettings().setDomStorageEnabled(true);
		SlideViewActivity.wc.getSettings().setAppCacheEnabled(true);
		SlideViewActivity.wc.getSettings().setBuiltInZoomControls(true);
		SlideViewActivity.wc.getSettings().setDisplayZoomControls(false);
		SlideViewActivity.wc.getSettings().setSupportZoom(true);
		SlideViewActivity.wc.getSettings().setUseWideViewPort(true);
		SlideViewActivity.wc.getSettings().setLoadWithOverviewMode(true);
		SlideViewActivity.wc.getSettings().setSupportMultipleWindows(true);
		SlideViewActivity.wc.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		SlideViewActivity.wc.getSettings().setPluginState(PluginState. ON);
		SlideViewActivity.wc.getSettings().setDefaultTextEncodingName(DEFINE.URI_ENCODE);
		SlideViewActivity.wc.getSettings().setUserAgentString(SlideViewActivity.wc.getSettings().getUserAgentString()+" Hybrid 2.0");
		SlideViewActivity.wc.setWebChromeClient(new SMOWebChromeClient(av_));
		SlideViewActivity.wc.setWebViewClient(new ITGOWebChromeClient());


		SlideViewActivity.wc.loadUrl(SlideViewActivity.SUB_URL);


		return view;

	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.prevBtn:
				SlideViewActivity.wc.goBack();
				break;
			case R.id.nextBtn:
				SlideViewActivity.wc.goForward();
				break;
			case R.id.homeBtn:
				String homeURL = SlideViewActivity.SUB_URL;
				if(!openURL.equals("")) homeURL=openURL;
				clearHistory=true;
				SlideViewActivity.wc.loadUrl(homeURL);
				break;
			case R.id.reloadBtn:
				clearApplicationCache(null);
				SlideViewActivity.wc.clearCache(true);
				SlideViewActivity.wc.reload();
				break;
			case R.id.shareBtn:
				shareUrl();
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
		Check_Preferences.setAppPreferences(mContext, "setBottomMenuStyle", style );
		SharedPreferences prefs = mContext.getSharedPreferences("co.kr.hybrid", mContext.MODE_PRIVATE);
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
		i.putExtra(Intent.EXTRA_TEXT,SlideViewActivity.wc.getUrl());
		startActivity(Intent.createChooser(i, SlideViewActivity.wc.getTitle()+" "+getString(R.string.share_page)));		
	}
	private void clearApplicationCache(java.io.File dir){
		if(dir==null)
			dir = mContext.getCacheDir();
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
			final AlertDialog.Builder builder = new AlertDialog.Builder(av_ , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
				Toast.makeText(mContext, "인터넷 끊김! url노출 안됨.", 0).show();
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
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, av_.getPackageName());
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
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, av_.getPackageName());
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
					SharedPreferences prefs = av_.getSharedPreferences("co.kr.hybrid", av_.MODE_PRIVATE);
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
			}else if(overrideUrl.startsWith("hybridapi://settingtitle")){
				//타이틀 바 변경 : ex)로그인 & 저장 & 로그아웃 기능 

			} else if(overrideUrl.startsWith("hybridapi://shareUrl")){
				shareUrl();
				return true;     			      
			}else if(overrideUrl.startsWith("hybridapi://makeShortCut")){
				addShortcut();
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://hideBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu" , "GONE");
				bottomMenu.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_hideBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu2" , "GONE");
				bottomMenu2.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu" , "VISIBLE");
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_showBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu2" , "VISIBLE");
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
			} else if(overrideUrl.startsWith("hybridapi://setActionStyle")){
				final String kw[] = overrideUrl.split("\\?");
				Log.e("SKY", "overrideUrl :: " + overrideUrl);
				Log.e("SKY", "kw :: " + kw[1]);
				if(!kw[1].equals("")){
					SlideViewActivity.action_bar.setBackgroundColor(Color.parseColor(kw[1]));
				}
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


			//프로그레스바 띄우기
			if (Check_Preferences.getAppPreferencesboolean(mContext, "PROGRESSBAR")) {
				if (dialog == null) {
					dialog = new ProgressDialog(getActivity() ,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					dialog.setMessage(getString(R.string.loading));
					dialog.setCancelable(false);
					dialog.show();
					if (Check_Preferences.getAppPreferencesboolean(getActivity(), "PROGRESSBAR_3")) {
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
			if (Check_Preferences.getAppPreferencesboolean(av_, "PROGRESSBAR")) {
				dialog.dismiss();
			}
			//Loading 뷰 가리기
			if (DEFINE.LOADINGVIEW) {
				vi.setVisibility(View.GONE);
			}
			if(clearHistory){
				SlideViewActivity.wc.clearHistory();
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);
				btn2.getBackground().setAlpha(90);
				btn2.setClickable(false);
				clearHistory=false;
			}
			if(SlideViewActivity.wc.canGoBack()){
				btn1.getBackground().setAlpha(255);
				btn1.setClickable(true);
			}else{
				btn1.getBackground().setAlpha(90);
				btn1.setClickable(false);            		
			}
			if(SlideViewActivity.wc.canGoForward()){
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
					getContext());
			builder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					av_.finish();
				}
			});
			builder.setMessage("네트워크 상태가 원활하지 않습니다. 잠시 후 다시 시도해 주세요.");
			builder.show();
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

			dialog = new ProgressDialog(getContext(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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
			new AlertDialog.Builder(av_ , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
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
			av_.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}     
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			av_.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
		}             
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			av_.startActivityForResult(Intent.createChooser(i, getString(R.string.file_choose)), FILECHOOSER_RESULTCODE);
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
		SharedPreferences prefs = av_.getSharedPreferences("co.kr.hybrid", av_.MODE_PRIVATE);
		final String device_id = prefs.getString("device_id","");
		final boolean pushEnable = prefs.getBoolean("pushEnable",false);

		if( Build.VERSION.SDK_INT < 19 ){
			SlideViewActivity.wc.loadUrl("javascript:"+callback+"('"+device_id+"',"+pushEnable+");");
		}else{
			av_.runOnUiThread(new Runnable()
			{
				public void run()
				{
					ValueCallback<String> resultCallback = null;
					SlideViewActivity.wc.evaluateJavascript(callback+"('"+device_id+"',"+pushEnable+");",resultCallback);
				}
			});    			
		}		
	}
	public void closePop(){
		mainBody.removeView(pWebView);
		popup=false;		
	}
	public boolean checkNetwordState() {
		ConnectivityManager connManager =(ConnectivityManager)av_.getSystemService(Context.CONNECTIVITY_SERVICE);
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
			method.invoke(obj, new String(par) , av_ , SlideViewActivity.wc , new String(return_fun));
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
			Log.e("SKY", "overrideUrl :: " + overrideUrl);
			//인터넷 확인후 시작
			if (!checkNetwordState()) {
				Toast.makeText(av_, "인터넷 끊김! url노출 안됨.", 0).show();
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
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, av_.getPackageName());
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
					intent.putExtra(Browser.EXTRA_APPLICATION_ID, av_.getPackageName());
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
					SharedPreferences prefs = av_.getSharedPreferences("co.kr.hybrid", av_.MODE_PRIVATE);
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
			} else if(overrideUrl.startsWith("hybridapi://shareUrl")){
				shareUrl();
				return true;     			      
			}else if(overrideUrl.startsWith("hybridapi://makeShortCut")){
				addShortcut();
				return true;      			
			}else if(overrideUrl.startsWith("hybridapi://hideBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu" , "GONE");
				bottomMenu.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_hideBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu2" , "GONE");
				bottomMenu2.setVisibility(View.GONE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://showBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu" , "VISIBLE");
				bottomMenu.setVisibility(View.VISIBLE);
				return true;       			
			}else if(overrideUrl.startsWith("hybridapi://new_showBottomMenu")){
				Check_Preferences.setAppPreferences(av_, "bottomMenu2" , "VISIBLE");
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

	private void setBottomMenuStyle2(String style){
		if(style.equals("default") || style.equals("")){
			setBottomMenuStyleDefault2();
		}else{
			setBottomMenuStyleColor2(style);
		}
		Check_Preferences.setAppPreferences(mContext, "setBottomMenuStyle2", style );
		SharedPreferences prefs = av_.getSharedPreferences("co.kr.hybrid", av_.MODE_PRIVATE);
		final SharedPreferences.Editor editor = prefs.edit();	
		editor.putString("tabstyle2", style);
		editor.commit();		
	}
	private void setBottomMenuStyleDefault2(){
		bottomMenu2.setBackgroundResource(R.drawable.tab_bg);
	}	
	private void setBottomMenuStyleColor2(String tabstyle){
		bottomMenu2.setBackgroundColor(Color.parseColor(tabstyle));
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

		av_.sendBroadcast(intent);
	}	





	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}