package co.kr.hybridapp.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import co.kr.hybridapp.common.JSONUtil;

public class AddrSearch {
	String url = "https://apis.daum.net/local/v1/search/keyword.json";
	Context mContext;
	
	public interface OnResultCallback {
		public void onResult(JSONArray result);
	}

	public AddrSearch(Context context) {
		this.mContext = context;
	}

	public void getAddrArray(String getParams, OnResultCallback onResultCallback) {
		new AddrArrayTask(getParams, onResultCallback).execute();
	}


	private class AddrArrayTask extends AsyncTask<Void, Void, JSONArray> {
		String getParams = "";
		InputStream is = null;
		StringBuilder sb = null;
		JSONArray jsonArray;
		OnResultCallback onResultCallback = null;

		public AddrArrayTask(String getParams, OnResultCallback onResultCallback) {
			this.getParams = getParams;
			this.onResultCallback = onResultCallback;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			try {

				Log.e("url", url);

				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpParams params = httpClient.getParams();
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				HttpConnectionParams.setSoTimeout(params, 10000);

				HttpGet httpGet = new HttpGet(url+getParams);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				sb = new StringBuilder();
				
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				Log.e("G",sb.toString());
				
				JSONObject json = new JSONObject(sb.toString());
				JSONObject channel = json.getJSONObject("channel");
				jsonArray = JSONUtil.getSafeJSONArray(channel, "item");

			} catch (Exception e) {

			}

			return jsonArray;
			
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			onResultCallback.onResult(result);
		}

	}





}
