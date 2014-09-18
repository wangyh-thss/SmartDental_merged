package com.edu.thss.smartdental.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.edu.thss.smartdental.R;
import com.edu.thss.smartdental.model.general.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBManager {
	//private DBHelper helper;
	private SQLiteDatabase db;
	private Context context;
	 private static final String DB_NAME = "smartdental.db";
     private static final String PACKAGE_NAME="com.edu.thss.smartdental";
     public static final String DB_PATH="/data"+Environment.getDataDirectory().getAbsolutePath()+"/"+PACKAGE_NAME;
     private final int BUFFER_SIZE = 400000;
     
	public DBManager(Context context){
		this.context = context;
	}
 
	public SQLiteDatabase getDatabase() {
        return db;
    }
 
    public void setDatabase(SQLiteDatabase database) {
        this.db = database;
    }
    public void openDatabase() {
    	Log.i("open", DB_PATH + "/" + DB_NAME);
        this.db = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }
 
    private SQLiteDatabase openDatabase(String dbfile) {
 
        try {
            if (!(new File(dbfile).exists())) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.smartdental); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
 
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return db;
 
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

     /**
	 * 关闭数据库
	 * */
	public void closeDB(){
		db.close();
	}
	/**
	 * 以下为数据库查询相关操作
	 * */
	
	/**
	 * 返回查询列表，以病人为例
	 * */
	public List<SDPatient> query(){
		ArrayList<SDPatient> patients  = new ArrayList<SDPatient>();
		Cursor c = db.rawQuery("SELECT * FROM patient", null);
		while(c.moveToNext()){
			SDPatient patient = new SDPatient();
			patient.idNum = c.getString(c.getColumnIndex("idNum"));
			patient.name = c.getString(c.getColumnIndex("name"));
			patient.birth = c.getString(c.getColumnIndex("birth"));
			patient.bloodType = c.getString(c.getColumnIndex("bloodType"));
			patients.add(patient);
		}
		c.close();
		return patients;
	}
	public SDPatient queryByName(String name){
		SDPatient patient = new SDPatient();
		String[] columns = {"idNum","name","birth","bloodType"};
		String[] selection = new String[]{name};
		
 		Cursor c = db.rawQuery("select * from patient where name = '"+name+"'",null);
 		if(c.getCount()>0){
 		c.moveToNext();
		patient.idNum = c.getString(c.getColumnIndex("idNum"));
		patient.name = c.getString(c.getColumnIndex("name"));
		patient.birth = c.getString(c.getColumnIndex("birth"));
		patient.bloodType = c.getString(c.getColumnIndex("bloodType"));
 		}
		c.close();
		return patient;
	}
}
