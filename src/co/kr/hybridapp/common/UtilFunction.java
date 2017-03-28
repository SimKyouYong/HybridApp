package co.kr.hybridapp.common;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class UtilFunction {
	private final static String TAG = "UtilFunction";
	
	public static final int color_1 = Color.argb(190, 255, 0, 0);
	public static final int color_2 = Color.argb(190, 252, 255, 30);
	public static final int color_3 = Color.argb(190, 0, 252, 255);
	public static final int color_4 = Color.argb(190, 120, 255, 0);
	public static final int color_5 = Color.argb(190, 255, 0, 192);
	public static final int color_6 = Color.argb(190, 131, 122, 255);
	public static final int color_7 = Color.argb(190, 255, 108, 0);
	public static final int color_8 = Color.TRANSPARENT;
	
	public static final int color_jul = Color.rgb(255, 102, 0);
	public static final int color_jul_sub = Color.rgb(189, 189, 189);
	
	/**
	 * 외부 브라우저를 실행시킵니다.
	 * @param context
	 * @param url bible25:blank?url=http://www.daum.net
	 * @return
	 */
	public static boolean launchExternalBrowser(Context context, String url) {
		if (TextUtils.isEmpty(url)) return false;
		String checkWord = "url";
		int index = url.indexOf(checkWord);
		String urlToLaunch = url.substring(index + checkWord.length() + 1);
		
		
		getPackage(context,urlToLaunch);
		// String url = 추출;
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToLaunch));
        //context.startActivity(intent);
        return true;
	}
	public static boolean isPackageInstalled(Context ctx, String pkgName) {
		try {
			ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean startApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
		
		return true;
	}
	public static boolean goMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);  
		Intent it = new Intent(Intent.ACTION_VIEW, uri);  
		context.startActivity(it);
		
		return true;
	}
	/**
	 * 해당 패키지명에 해당하는 앱을 실행시킵니다.
	 * @param context
	 * @param url bible25:start_app?packageName=com.foo.fooApp
	 * @return
	 */
	public static boolean startAppProcess(Context context, String url) {
		if (TextUtils.isEmpty(url)) return false;
		String checkWord = "packageName";
		int index = url.indexOf(checkWord);
		String packageName = url.substring(index + checkWord.length() + 1);
		
		if (isPackageInstalled(context, packageName)) {
			return startApp(context, packageName);
		} else {
			return goMarket(context, packageName);
		}		
	}	
	private static void getPackage(Context context,String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		PackageManager packageManager = context.getPackageManager();
		Uri uri = Uri.parse(url);
		browserIntent.setDataAndType(uri, "text/html");
		List<ResolveInfo> list = packageManager.queryIntentActivities(browserIntent, 0);
		for (ResolveInfo resolveInfo : list) {
		    String activityName = resolveInfo.activityInfo.name;
		   
		    Log.e("activityName", activityName);
		    if (activityName.contains("Browser") || activityName.contains("chrome")) {
		        browserIntent =
		                packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
		        ComponentName comp =
		                new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
		        browserIntent.setAction(Intent.ACTION_VIEW);
		        browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
		        browserIntent.setComponent(comp);
		        browserIntent.setData(uri);
		        context.startActivity(browserIntent);
		        break;
		    }
		}


	}
	public static boolean contains(String word, String wordToCheck) {
		if (TextUtils.isEmpty(word)) return false;
		
		if (TextUtils.indexOf(word, wordToCheck) >= 0) {
			return true;
		} else {
			return false;
		}
	}
    public static int toInt(String str, int defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }	
	public static String[] getTotalArray(String[] a, String[] b) {
		int totalLength = a.length + b.length;
		String[] totalStringArray = new String[totalLength];
		
		for(int i = 0; i < totalLength; i++) {
			if (i < a.length) {
				totalStringArray[i] = a[i];
			} else {
				totalStringArray[i] = b[i - a.length];
			}
		}
		return totalStringArray;
	}    	
}
