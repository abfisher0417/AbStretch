package com.andrewfisher.abstretch;

import java.util.ArrayList;
import java.util.List;

import com.andrewfisher.abstretch.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * AbStretchView is the activity that displays all recorded core and mobility
 * activities. It also gives the user the option to aggregate information by
 * date (default), by week, or by month.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchView extends ListActivity {
	
	private static final String TAG = "AbStretchView";

	private AbStretchActivityDAO dataSource;
	private List<AbStretchActivityItem> activities;
	private AbStretchActivityArrayAdapter mAdapter;
	
	public void onCreate(final Bundle savedInstanceState) {
		Log.i(TAG, "onClick()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.abstretch_view);
		
		dataSource = new AbStretchActivityDAO(this);
		dataSource.open();
		
		activities = getAllAbStretchActivities(MySQLiteHelper.COLUMN_DATE);
		
		mAdapter = new AbStretchActivityArrayAdapter(this, activities);
		setListAdapter(mAdapter);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu()");
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.abstretch_view_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "onOptionsItemSelected()");
		
		switch (item.getItemId()) {
		case R.id.action_overflow_bydate:
			activities = getAllAbStretchActivities(MySQLiteHelper.COLUMN_DATE);
			mAdapter.updateAbStretchActivityArrayAdapter(activities);
			return true;
		case R.id.action_overflow_byweek:
			activities = getAllAbStretchActivities(MySQLiteHelper.COLUMN_WEEK);
			mAdapter.updateAbStretchActivityArrayAdapter(activities);
			return true;
		case R.id.action_overflow_bymonth:
			activities = getAllAbStretchActivities(MySQLiteHelper.COLUMN_MONTH);
			mAdapter.updateAbStretchActivityArrayAdapter(activities);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		
		dataSource.close();
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		
		dataSource.open();
		super.onResume();
	}
	
	/**
	 * getAllAbStretchActivities accesses the SQLite database to aggregate activities
	 * by date, by week, or by month. It adds activities to the activities list.
	 * Header rows are for each date and non-header rows are for individual activities.
	 * 
	 * @param groupBy whether to aggregate the information by date, by week, or by month.
	 * @return List<AbStretchActivityItem> activities
	 */
	public List<AbStretchActivityItem> getAllAbStretchActivities(String groupBy) {
		List<AbStretchActivityItem> parsedList = new ArrayList<AbStretchActivityItem>();
		List<AbStretchActivity> resultSet = dataSource.getAllAbStretchActivities(groupBy);
		
		long curDateLong = 0;
		for (AbStretchActivity activity : resultSet) {
			if (curDateLong != activity.getActDate()) {
				curDateLong = activity.getActDate();
				parsedList.add(new AbStretchActivityListHeader(longDateToStringDate(curDateLong)));
			}
			
			parsedList.add(new AbStretchActivityListItem(activity.getActType(), formatDuration(activity.getActDuration())));
		}
		
		return parsedList;
	}
	
	/**
	 * longDateToStringDate accepts a long formatted as yyyymmdd and converts it to MM/DD/YYYY.
	 * 
	 * @param longDate long formatted as yyyymmdd
	 * @return String long converted to MM/DD/YYYY
	 */
	private String longDateToStringDate(long longDate) {
		String inputString = Long.toString(longDate);
		String dateString = "";
		if (inputString.length() == 8) {
			dateString = inputString.substring(4,6) + "/" + inputString.substring(6) + "/" + inputString.substring(0,4);
		} else if (inputString.length() == 6) {
			dateString = inputString.substring(4) + "/" + inputString.substring(0,4);
		}
		return dateString;
	}
	
	/**
	 * formatDuration accepts a number in milliseconds and converts it to HH:MM:SS format.
	 * 
	 * @param millis number of milliseconds to convert
	 * @return String milliseconds converted to HH:MM:SS format
	 */
	public static String formatDuration(long millis) {
	    long seconds = (millis / 1000) % 60;
	    long minutes = (millis / (1000 * 60)) % 60;
	    long hours = millis / (1000 * 60 * 60);

	    StringBuilder b = new StringBuilder();
	    b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : 
	    String.valueOf(hours));
	    b.append(":");
	    b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :     
	    String.valueOf(minutes));
	    b.append(":");
	    b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : 
	    String.valueOf(seconds));
	    return b.toString();
	}
}

