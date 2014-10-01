package com.metaio.Template;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {
	private SQLiteDatabase db;
	private DbHelper DbHelper;
	private static DataSource instance;
	
	public DataSource(Context c){
		DbHelper = new DbHelper(c);
	}
	
	public void open() throws SQLException {
		db = DbHelper.getWritableDatabase();
	}

  public void close() {
	  DbHelper.close();
  }
  
public static DataSource getInstance(Context c){
	if(instance == null){
		instance = new DataSource(c);
	}
	return instance;
}
  
public ArrayList<String[]> getAllPokemon(){
	Cursor c = db.rawQuery("select * from " + DbHelper.DATABASE_TABLE, null);
	ArrayList<String[]> s = new ArrayList<String[]>(c.getCount());
	if(c.moveToFirst()){
		for(int i = 0; i < c.getCount();i++){
			String id = c.getString(0);
			String name = c.getString(1);
			String[] tmp = {id,name};
			s.add(tmp);
			c.moveToNext();
		}
	}
	return s;
}

public void catchPokemon(int id, double lat, double longt){
	db.execSQL("update " + DbHelper.DATABASE_TABLE + "  set lat=" + lat + ", long=" + longt + " where _id=" + id);
}

public double[] getCoordinates(int nat_id){
	Cursor c = db.rawQuery("select lat,long from " + DbHelper.DATABASE_TABLE + " where _id = " + nat_id, null);
	if(c.moveToFirst()){
	double lat = c.getFloat(0);
	double longt = c.getFloat(1);
	double[] coord = {lat,longt};
	return coord;
	}
	else{
		return null;
	}
}

public ArrayList<float[]> getAllCoordinates(){
	Log.d("out", "select _id,lat,long from " + DbHelper.DATABASE_TABLE);
	Cursor c = db.rawQuery("select _id,lat,long from " + DbHelper.DATABASE_TABLE, null);
	ArrayList<float[]> s = new ArrayList<float[]>(c.getCount());
	if(c.moveToFirst()){
		for(int i = 0; i < c.getCount();i++){
			Log.d("lat", Float.toString(c.getFloat(0)));
			Log.d("long", Float.toString(c.getFloat(1)));
			float id = c.getFloat(0);
			float lat = c.getFloat(1);
			float longt = c.getFloat(2);
			float[] tmp = {id,lat,longt};
//			Log.d("pokemon", id+name);
			s.add(tmp);
			c.moveToNext();
		}
	}
	return s;
}

public String[] getPokemon(int nat_id){
	Log.d("nat id", Integer.toString(nat_id));
	Cursor c = db.rawQuery("select * from " + DbHelper.DATABASE_TABLE + " where _id=" + nat_id , null);
	Log.d("out", "select * from " + DbHelper.DATABASE_TABLE + " where _id=" + nat_id);
	Log.d("s","hi");
	if(c.moveToFirst()){
		String id = c.getString(0);
		String name = c.getString(1);
		String type1 = c.getString(2);
		String type2 = c.getString(3);
		String description = c.getString(4);
		String height = c.getString(5);
		String weight = c.getString(6);
		String caught = c.getString(7);
		String seen = c.getString(8);
		String dateCaptured = c.getString(9);
		String[] tmp = {id,name,type1,type2,description,height,weight,caught,seen,dateCaptured};
		return tmp;
	}
	else{return null;}
}

public String test(){
return "hi";
}
}
