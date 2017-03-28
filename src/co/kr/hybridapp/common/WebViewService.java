package co.kr.hybridapp.common;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;

/**
 * 앱 메인화면에서 네이티브로 구성된 내부 화면으로 들어갔을시에
 * 보여지는 광고(웹뷰)를 생성하는 기능을 담당합니다.
 * @author ncodi
 *
 */
public class WebViewService {

	/**
	 * 광고 화면 생성
	 * @param webView
	 * @param url
	 */
	public static void setAdWebView(Context context, WebView webView, String url) {
		boolean isJavascriptEnabled = true;
		boolean isHorizontalScrollBarEnabled = false;
		boolean isVerticalScrollBarEnabled = true;
		
		setWebViewProperties(context, webView, url, isJavascriptEnabled, isHorizontalScrollBarEnabled, isVerticalScrollBarEnabled);
	}
	
	
		/**
	 * 스크롤 없는 광고 뷰
	 * @param context
	 * @param webView
	 * @param url
	 */
	public static void setAdWebViewWithNoScroll(Context context, WebView webView, String url) {
		boolean isJavascriptEnabled = true;
		boolean isHorizontalScrollBarEnabled = false;
		boolean isVerticalScrollBarEnabled = false;
		
		setWebViewProperties(context, webView, url, isJavascriptEnabled, isHorizontalScrollBarEnabled, isVerticalScrollBarEnabled);
	}
	
	public static void setWebViewProperties(final Context context, WebView webView
			, String url, boolean isJavascriptEnabled, boolean isHorizontalScrollBarEnabled, boolean isVerticalScrollBarEnabled) {
		
		WebSettings webSettings = webView.getSettings();
		
		webSettings.setJavaScriptEnabled(isJavascriptEnabled);
//      캐시파일 사용 금지(운영중엔 주석처리 할 것) 
//      webSettings.setCacheMode( WebSettings.LOAD_NO_CACHE );
        webSettings.setSupportZoom(false);
        
        webView.setHorizontalScrollBarEnabled(isHorizontalScrollBarEnabled);
        webView.setVerticalScrollBarEnabled(isVerticalScrollBarEnabled);
        webView.setBackgroundColor(0);
                
        webSettings.setAllowFileAccess(true);
		webSettings.getLoadsImagesAutomatically();
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setPluginState(PluginState.ON);
		webSettings.setSupportMultipleWindows(false);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setAppCacheEnabled(true);

		if (Build.VERSION.SDK_INT >= 19) {
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			// webView.setWebContentsDebuggingEnabled(true);
		} else {
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
        
        // 웹뷰 하나 LIKE 오늘자처럼
        webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("bible25:blank")) {
					return true;
				} else {
					view.loadUrl(url);
					return true;
				}
			}
		});

        try {
			webView.loadUrl(url);
        } catch(Exception ex) {
			ex.printStackTrace();
        }		
	}
}