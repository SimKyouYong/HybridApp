package co.kr.hybridapp.common;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DataSync extends AsyncTask<String, String, String> {
	Context con;
	String s;
	SharedPreferencesUtil spu;
	public DataSync(Context con) {
		// TODO Auto-generated constructor stub
		this.con=con;
		spu=new SharedPreferencesUtil(con);

	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL ur;

		try {
			ur = new URL(params[0]+"");
			URLConnection con = ur.openConnection();

			HttpURLConnection exitCode = (HttpURLConnection)con;
			s=exitCode.getResponseCode()+"";
			spu.put("getcode", s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}





}
