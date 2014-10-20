package com.andrewfisher.abstretch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * AbStretchActivityDAO data access object save and retrieve ab stretch activities.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchActivityDAO {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	public AbStretchActivityDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public long createAbStretchActivity(long actDateTime, long actDuration, String actType) {
		// Manipulate the actDateTime for storage and retrieval
		Calendar dateTime = Calendar.getInstance();
		dateTime.setFirstDayOfWeek(Calendar.MONDAY);
		dateTime.setTimeInMillis(actDateTime);
		
		String actYear = Integer.toString(dateTime.get(Calendar.YEAR));
		
		SimpleDateFormat yyyymmFormat = new SimpleDateFormat("yyyyMM", Locale.US);
		String actMonth = yyyymmFormat.format(dateTime.getTime());
		
		SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
		String actDate = yyyymmddFormat.format(dateTime.getTime());
		
		while (dateTime.get(Calendar.DAY_OF_WEEK) > dateTime.getFirstDayOfWeek()
				|| dateTime.get(Calendar.DAY_OF_WEEK) < dateTime.getFirstDayOfWeek()) {
			dateTime.add(Calendar.DATE, -1); // Subtract 1 day until first day of week
		}
		String actWeek = yyyymmddFormat.format(dateTime.getTime());
		
		// Store values
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_DATETIME, actDateTime);
		values.put(MySQLiteHelper.COLUMN_DURATION, actDuration);
		values.put(MySQLiteHelper.COLUMN_TYPE, actType);
		values.put(MySQLiteHelper.COLUMN_DATE, actDate);
		values.put(MySQLiteHelper.COLUMN_WEEK, actWeek);
		values.put(MySQLiteHelper.COLUMN_MONTH, actMonth);
		values.put(MySQLiteHelper.COLUMN_YEAR, actYear);
		
		long insertID = database.insert(MySQLiteHelper.TABLE_ACTIVITIES, null, values);

		return insertID;
	}
	
	public void deleteAbStretchActivity(AbStretchActivity act) {
		long ID = act.getActID();
		database.delete(MySQLiteHelper.TABLE_ACTIVITIES, MySQLiteHelper.COLUMN_ID + " = " + ID, null);
	}
	
	/*
	 * getAllAbStretchActivities fetches all ab/stretch activities.
	 * Based on the group by parameter specified at invocation time,
	 * groups the data before returning it. E.g., show me the sum of
	 * all activities by date, week, month, or year.
	 */
	public List<AbStretchActivity> getAllAbStretchActivities(String groupBy) {
		List<AbStretchActivity> activities = new ArrayList<AbStretchActivity>();

		String rawQuery = "SELECT ";
		
		if (MySQLiteHelper.COLUMN_DATE.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_DATE;
		} else if (MySQLiteHelper.COLUMN_WEEK.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_WEEK;
		} else if (MySQLiteHelper.COLUMN_MONTH.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_MONTH;			
		}
		
		rawQuery += " AS " + MySQLiteHelper.SELECT_COLUMN_DATE + ","
		+ MySQLiteHelper.COLUMN_TYPE + ","
		+ "SUM(" + MySQLiteHelper.COLUMN_DURATION + ") AS " + MySQLiteHelper.SELECT_COLUMN_DURATION
		+ " FROM " + MySQLiteHelper.TABLE_ACTIVITIES
		+ " GROUP BY ";
		
		if (MySQLiteHelper.COLUMN_DATE.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_DATE;
		} else if (MySQLiteHelper.COLUMN_WEEK.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_WEEK;
		} else if (MySQLiteHelper.COLUMN_MONTH.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_MONTH;			
		}
		
		rawQuery += "," + MySQLiteHelper.COLUMN_TYPE + " ORDER BY ";
		
		if (MySQLiteHelper.COLUMN_DATE.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_DATE;
		} else if (MySQLiteHelper.COLUMN_WEEK.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_WEEK;
		} else if (MySQLiteHelper.COLUMN_MONTH.equals(groupBy)) {
			rawQuery += MySQLiteHelper.COLUMN_MONTH;			
		}
		
		rawQuery += " DESC," + MySQLiteHelper.COLUMN_TYPE + " ASC";
		
		Cursor cursor = database.rawQuery(rawQuery, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AbStretchActivity abStretchActivity = cursorToAbStretchActivity(cursor);
			activities.add(abStretchActivity);
			cursor.moveToNext();
		}
		cursor.close();
		
		return activities;
	}
	
	private AbStretchActivity cursorToAbStretchActivity(Cursor cursor) {
		AbStretchActivity abStretchActivity = new AbStretchActivity();
		abStretchActivity.setActDate(cursor.getLong(0));
		abStretchActivity.setActType(cursor.getString(1));
		abStretchActivity.setActDuration(cursor.getLong(2));
		return abStretchActivity;
	}
}
