package co.kr.hybridapp.common;


import android.text.TextUtils;

/**
 * 카카오 링크의 정보를 담는 도메인
 * @author ncodi
 *
 */
public class KakaoLinkData {

	private String text;
	private String imageSrc;
	private String imageWidth;
	private String imageHeight;
	
	// 버튼 타입
	private String buttonText;
	private String buttonType;
	private String buttonUrl;
	
	// 링크 타입
	private String linkText;
	private String linkType;
	private String linkUrl;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}
	public String getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getButtonText() {
		return buttonText;
	}
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	public String getButtonType() {
		return buttonType;
	}
	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}
	public String getButtonUrl() {
		return buttonUrl;
	}
	public void setButtonUrl(String buttonUrl) {
		this.buttonUrl = buttonUrl;
	}
	public String getLinkText() {
		return linkText;
	}
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	/**
	 * 웹버튼 여부
	 * @return
	 */
	public boolean hasWebButton() {
		if ( TextUtils.equals("web", this.getButtonType()) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * 링크 존재 여부
	 * @return
	 */
	public boolean hasWebLink() {
		if ( TextUtils.equals("web", this.getLinkType()) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * 이미지 존재
	 * @return
	 */
	public boolean hasImage() {
		if (!TextUtils.isEmpty(this.getImageSrc())) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 메인화면에서 추천하기에서 카카오톡을 선택했을시에
	 * 카카오링크로 공유하기 위한 내용을 생성하여 리턴합니다.
	 * 
	 * @return
	 */
	public static KakaoLinkData getInstanceForRecommend() {
		KakaoLinkData data = new KakaoLinkData();
		data.setText("[바이블25]:대한기독교서회미디어");
		data.setImageSrc("http://emview.godohosting.com/ic_launcher.png");
		data.setButtonType("web");
		data.setButtonText("앱 설치하러 가기");
		data.setButtonUrl("http://ch2ho.bible25.com/bible25.php");
		
		return data;
	}
	
	/**
	 * 성경 Activity에서 특정 절을 클릭한후 공유하기 위해 카카오링크 데이터를 생성하여 리턴합니다.
	 * @param text 공유할 특정 절의 내용
	 * 
	 * @return 카카오링크를 보낼 내용을 담은 도메인
	 */
	public static KakaoLinkData getInstanceForShareBibleVerse(String text) {
		KakaoLinkData data = new KakaoLinkData();
		
		data.setText("[하이브리드 Demo]" + text);
		data.setButtonType("web");
		data.setButtonText("앱 설치하러 가기");
		data.setImageSrc("http://emview.godohosting.com/ic_launcher.png");
		data.setButtonUrl("http://ch2ho.bible25.com/bible25.php");
		
		return data;
	}
	
}
