package co.kr.hybridapp.obj;

import java.io.Serializable;

public class SKLocation implements Serializable {
	
	private double lat, lng = 0;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
}
