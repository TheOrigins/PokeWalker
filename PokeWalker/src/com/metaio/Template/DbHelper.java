package com.metaio.Template;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DbHelper extends SQLiteOpenHelper {

	private DbHelper instance;
	static final String DATABASE_NAME = "Poke.db";
    static final String DATABASE_TABLE = "Pokedex";
    static int DATABASE_VERSION = 1;
    private Context c;
    private static final String PokeDbCreate = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + 
    "( _id INTEGER PRIMARY KEY, " +
	"name VARCHAR , " +
    "type1 VARCHAR, "+
	"type2 INTEGER, "+
    "description VARCHAR, "+
    "height INTEGER, "+
    "weight INTEGER, "+
    "caught INTEGER, "+
    "seen INTEGER, "+
    "date VARCHAR);";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		c = context;
	}
		
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PokeDbCreate);
		try {
		InputStream am = c.getAssets().open("poke.txt");
	    BufferedReader b = new BufferedReader(new InputStreamReader(am));
		for(int i = 0; i < 151; i++){
			String p = b.readLine();
//			Log.d("poke",p);
			db.execSQL(p);
		}

	} catch (Exception e) {
		Log.d("poke", "sqlerror");
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DbHelper.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	        onCreate(db);
	}
	
}