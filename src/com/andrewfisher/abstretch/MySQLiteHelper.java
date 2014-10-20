package com.andrewfisher.abstretch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MySQLiteHelper defines the SQLite database schema for storing
 * core and mobility activities.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_ACTIVITIES = "Activities";
	public static final String COLUMN_ID = "ActID";
	public static final String COLUMN_DATETIME = "ActDateTime";
	public static final String COLUMN_DURATION = "ActDuration";
	public static final String COLUMN_TYPE = "ActType";
	public static final String COLUMN_DATE = "ActDate";
	public static final String COLUMN_WEEK = "ActWeek";
	public static final String COLUMN_MONTH = "ActMonth";
	public static final String COLUMN_YEAR = "ActYear";
	public static final String SELECT_COLUMN_DATE = "SelectedDate";
	public static final String SELECT_COLUMN_DURATION = "SelectedDuration";
	private static final String DATABASE_NAME = "AbStretch.db";
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_CREATE = "CREATE TABLE "
		      + TABLE_ACTIVITIES + "(" + COLUMN_ID
		      + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATETIME
		      + " INTEGER NOT NULL, " + COLUMN_DURATION
		      + " INTEGER NOT NULL, " + COLUMN_TYPE
		      + " TEXT NOT NULL, " + COLUMN_DATE
		      + " INTEGER NOT NULL, " + COLUMN_WEEK
		      + " INTEGER NOT NULL, " + COLUMN_MONTH
		      + " INTEGER NOT NULL, " + COLUMN_YEAR
		      + " INTEGER NOT NULL);";

	/*
	 * MySQLiteHelper constructor.
	 * 
	 * @param context to use to open or create the database
	 */
	public MySQLiteHelper(Context context) {
		  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/*
	 * (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion
				+ " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
		onCreate(db);
	}

}
