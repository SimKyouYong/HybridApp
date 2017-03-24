package co.kr.hybridapp.common;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import co.kr.hybridapp.obj.AddrDTO;

public class SqlLiteUtil extends SQLiteOpenHelper {

	public SqlLiteUtil(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE addr_search_list( id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT, lat REAL, lng REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void query(String query) {
		SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();     
	}
	
	public ArrayList<AddrDTO> getCurrentAddr() {
		SQLiteDatabase db = getReadableDatabase();
    
        ArrayList<AddrDTO> list = new ArrayList<AddrDTO>();
         
        Cursor cursor = db.rawQuery("SELECT * FROM addr_search_list ORDER BY id desc", null);
        while(cursor.moveToNext()) {
        	AddrDTO addr = new AddrDTO();
        	addr.setId(cursor.getInt(0));
        	addr.setAddress(cursor.getString(1));
        	addr.setLat(cursor.getDouble(2));
        	addr.setLng(cursor.getDouble(3));
        	
        	list.add(addr);
        }
         
        return list;
	}
	
	public void insert_addr(String addr, double latitude, double longitude) {
		query("INSERT INTO addr_search_list values(null, '" + addr + "', "+latitude+", "+longitude+")");
	}
	
	public void delete_addr(int id) {
		query("DELETE FROM addr_search_list WHERE id = " + id);
	}
	
}
