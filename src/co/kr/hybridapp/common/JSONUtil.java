package co.kr.hybridapp.common;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil 
{
	public static JSONObject getSafeJSONObject(JSONObject json, String key)
	{
		try
		{
			return json.getJSONObject(key);
		}
		catch (Exception e) { }
		
		return null;
	}

	public static JSONArray getSafeJSONArray(JSONObject json, String key)
	{
		try
		{
			return json.getJSONArray(key);
		}
		catch (Exception e) { }
		
		return null;
	}

	public static String getSafeString(JSONObject json, String key)
	{
		try
		{
			String value = json.getString(key);
			if (value.equals("null") == true) 
			{
				value = "";
			}
			return value;
		}
		catch (Exception e) { }
		
		return null;
	}
	
	public static int getSafeInt(JSONObject json, String key)
	{
		try
		{
			return json.getInt(key);
		}
		catch (Exception e) { }
		
		return 0;
	}
	
	public static long getSafeLong(JSONObject json, String key)
	{
		try
		{
			return json.getLong(key);
		}
		catch (Exception e) { }
		
		return 0;
	}
	
	public static boolean getSafeBoolean(JSONObject json, String key)
	{
		try
		{
			return json.getBoolean(key);
		}
		catch (Exception e) { }
		
		return false;
	}
	
	public static void putSafe(JSONObject json, String key, Object value)
	{
		try
		{
			json.put(key, value);
		}
		catch (Exception e) { }
	}
	
	public static void putSafe(JSONObject json, String key, int value)
	{
		try
		{
			json.put(key, value);
		}
		catch (Exception e) { }
	}

	public static void putSafe(JSONObject json, String key, boolean value)
	{
		try
		{
			json.put(key, value);
		}
		catch (Exception e) { }
	}
	
	
}
