package com.andrewfisher.abstretch;

import java.util.Calendar;

import com.example.abstretch.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * AbStretchHome is the main activity where the user can record a
 * core or mobility activity.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchHome extends Activity {
	private static final String TAG = "AbStretchHome";
	private static final String ELAPSED_MS = "ElapsedMs";
	private static final String IS_RUNNING = "IsRunning";
	private static final String ACTIVITY_DATE_TIME = "ActivityDateTime";
	private static final String ACTIVITY_TYPE = "ActivityType";
	private static final String CHRONO_BASE = "ChronoBase";
	private static final String CORE = "Core";
	private static final String MOBILITY = "Mobility";
	
	private MyChronometer chrono;
	private ImageButton actionBtn;
	private ImageButton finishBtn;
	private ImageButton coreBtn;
	private ImageButton mobilityBtn;
	private String activityType;
	private boolean doTime;
	private AbStretchActivityDAO dataSource;
	private Calendar activityDateTime;
	private long chronoBase;
	private int chronoElapsedMs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.abstretch_main);
		
		dataSource = new AbStretchActivityDAO(this);
		dataSource.open();

		chrono = (MyChronometer) findViewById(R.id.chronometer);
		actionBtn = (ImageButton) findViewById(R.id.action_btn);
		finishBtn = (ImageButton) findViewById(R.id.finish_btn);
		
		coreBtn = (ImageButton) findViewById(R.id.activty_type_core);
		mobilityBtn = (ImageButton) findViewById(R.id.activty_type_mobility);
		
		if (savedInstanceState == null) {
			doTime = false;
			activityDateTime = Calendar.getInstance();
			activityDateTime.setTimeInMillis(0);
			activityType = CORE;
			chronoBase = 0;
			chronoElapsedMs = 0;
		} else {
			activityDateTime = Calendar.getInstance();
			activityDateTime.setTimeInMillis(savedInstanceState.getLong(ACTIVITY_DATE_TIME));
			doTime = savedInstanceState.getBoolean(IS_RUNNING);
			chronoBase = savedInstanceState.getLong(CHRONO_BASE);
			chronoElapsedMs = savedInstanceState.getInt(ELAPSED_MS);
			if (CORE.equals(savedInstanceState.getString(ACTIVITY_TYPE))) {
				activityType = CORE;
			} else {
				activityType = MOBILITY;
			}
		}
		
		setActivityButtons(activityType);
		
		actionBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "onClick() start/stop button");
				if (doTime) {
					stopTiming();
				} else {
					startTiming();
				}
			}
		});
		
		finishBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "onClick() finish button");
				stopTiming();
				showFinishDialog();
			}
		});
		
		coreBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "onClick() core button");
				activityType = CORE;
				setActivityButtons(activityType);
			}
		});
		
		mobilityBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "onClick() mobility button");
				activityType = MOBILITY;
				setActivityButtons(activityType);
			}
		});
		
		Log.i(TAG, "onCreate(): " + toString());
	}
	
	/**
	 * startTiming begins an activity. The date/time of the activity is recorded.
	 */
	private void startTiming() {
		if (activityDateTime == null ||
				(activityDateTime != null && activityDateTime.getTimeInMillis() < 115200000)) { // 115200000 = 01/02/1970 00:00:00
			activityDateTime = Calendar.getInstance();
		}
		
		actionBtn.setImageResource(R.drawable.stop_button);
		finishBtn.setClickable(false);
		finishBtn.setImageAlpha((int) 128f);
		
		chrono.start();
		chronoBase = chrono.getBase();
		doTime = true;
	
		Log.i(TAG, "startTiming(): " + toString());
	}
	
	/**
	 * stopTiming pauses an activity.
	 */
	private void stopTiming() {
		actionBtn.setImageResource(R.drawable.start_button);
		finishBtn.setClickable(true);
		finishBtn.setImageAlpha((int) 255f);
		
		chrono.stop();
		chronoElapsedMs = chrono.getElapsedTime();
		doTime = false;
		
		Log.i(TAG, "stopTiming(): " + toString());
	}

	/**
	 * onCreateOptionsMenu inflates the menu items for use in the action bar.
	 * One icon: view activities.
	 * 
	 * @return boolean true is returned for the menu to display
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu(): " + toString());
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.abstretch_home_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * onOptionsItemSelected handles presses on the action bar items.
	 * 
	 * @return boolean return false to resume normal processing; true to consume it here
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "onOptionsItemSelected(): " + toString());
		
		switch (item.getItemId()) {
		case R.id.action_view:
			Intent intent = new Intent(this, AbStretchView.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "onConfigurationChanged(): " + toString());
		
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
		chrono.stop();
		
		Log.i(TAG, "onPause(): " + toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		if (doTime) {
			chrono.setMsElapsed((int) (SystemClock.elapsedRealtime() - chronoBase));
			startTiming();
		} else {
			chrono.setBase(SystemClock.elapsedRealtime());
			chrono.setMsElapsed((int) (chronoElapsedMs));
			
			if (chrono.getMsElapsed() == 0) {
				finishBtn.setClickable(false);
				finishBtn.setImageAlpha((int) 128f);
			}
		}
		
		Log.i(TAG, "onResume(): " + toString());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "onSaveInstanceState(): " + toString());
		
		super.onSaveInstanceState(outState);
		outState.putBoolean(IS_RUNNING, doTime);
		outState.putInt(ELAPSED_MS, chrono.getElapsedTime());
		outState.putLong(CHRONO_BASE, chronoBase);
		outState.putString(ACTIVITY_TYPE, activityType);
		outState.putLong(ACTIVITY_DATE_TIME, activityDateTime.getTimeInMillis());
	}
    
	/**
	 * showFinishDialog displays a menu where the user can choose to save
	 * the activity or trash it.
	 */
    void showFinishDialog() {
    	Log.i(TAG, "showFinishDialog(): " + toString());
    	
        DialogFragment newFragment = FinishAbStretchDialogFragment.newInstance(
                R.string.alert_finish_dialog_title);
        newFragment.show(getFragmentManager(), "dialog");
    }

    /*
     * cancelActivity resets timer to zero.
     */
	public void cancelActivity() {
		Log.i(TAG, "cancelActivity(): " + toString());
		
		resetChronoToZero();
	}

	/*
	 * saveActivity stores activity in database. Resets timer to zero.
	 */
	public void saveActivity() {
		Log.i(TAG, "saveActivity(): " + toString());
		
		dataSource.createAbStretchActivity(activityDateTime.getTimeInMillis(), chrono.getMsElapsed(), activityType);
		
		resetChronoToZero();
	}
	
	/*
	 * resetChronoToZero is a utility method to reset timer to zero.
	 * Clear global calendar variables.
	 */
	public void resetChronoToZero() {
		chrono.clear();
		chronoBase = 0;
		chronoElapsedMs = 0;
		activityDateTime.clear();
		doTime = false;
		
		finishBtn.setClickable(false);
		finishBtn.setImageAlpha((int) 128f);
		
		Log.i(TAG, "resetChronoToZero(): " + toString());
	}
	
	public String toString() {
		String s = " | chrono - " + chrono.toString();
		s += " | activityType - " + activityType.toString();
		s += " | doTime - " + doTime;
		s += " | activityDateTime - " + activityDateTime.getTimeInMillis();
		s += " | chronoBase - " + Long.toString(chronoBase);
		s += " | chronoElapsedMs - " + Integer.toString(chronoElapsedMs);
		s += " | SystemClock.elapsedRealtime() - " + Long.toString(SystemClock.elapsedRealtime());
		s += " | ActivityDuration - " + chrono.getMsElapsed();
		return s;
	}
	
	/**
	 * setActivityButton changes the drawable for the core or mobility buttons,
	 * depending on what was selected.
	 * 
	 * @param actType either core or mobility
	 */
	private void setActivityButtons(String actType) {
		if (CORE.equals(actType)) {
			coreBtn.setImageResource(R.drawable.core_button_on);
			mobilityBtn.setImageResource(R.drawable.mobility_button_off);
		} else if (MOBILITY.equals(actType)) {
			coreBtn.setImageResource(R.drawable.core_button_off);
			mobilityBtn.setImageResource(R.drawable.mobility_button_on);
		}
	}

}
