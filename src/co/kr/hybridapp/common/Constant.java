package co.kr.hybridapp.common;

import java.io.File;

import android.os.Environment;

/**
 * @author jong-hyun.jeong
 * Constant ?��?��, API 주소, ?��?��미터 ?�� �??���? ?��?�� 값들?�� 모여?��?�� ?��?��?��
 */
public class Constant {
	
	
	public static final String EXTRA_IS_AMOBILE = "extra_is_amobile";
	public static final String EXTRA_SELECTED_ITEM = "extra_selected_item";
	public static final String EXTRA_COOKIE = "extra_cookie";
	public static final String EXTRA_USER_INFO = "extra_user_info";
	public static final String PREFERENCE_MENU_ITEMS = "preference_menu_items";
	public static final String PREFERENCE_INCIDENT = "preference_incident";
	public static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory() + File.separator + "upload";
	public static final String DIRECTORY_PHOTO_PATH = DIRECTORY_PATH + File.separator + "photo";
	public static final String DIRECTORY_VOICE_PATH = DIRECTORY_PATH + File.separator + "voice";
	public static final String DIRECTORY_DAMAGE_PATH = DIRECTORY_PATH + File.separator + "damage";
	
	// API 관련 
	public static final String DAUM_API_KEY = "eaac7c0b4b79a3712977e6e95ff684eb";
	public static final String NAVER_CLIENT_ID = "lYVR15dCB94C3TX8GUut";
	public static final String NAVER_CLIENT_SECRET = "pgKBhPYCYb";
	public static final String NAVER_CLIENT_NAME = "모인다";
}
