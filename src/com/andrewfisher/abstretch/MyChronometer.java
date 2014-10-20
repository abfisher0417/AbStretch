package com.andrewfisher.abstretch;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**
 * MyChronometer extends the base Android Chronometer by providing
 * ability to start at a non-zero number and to pause.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class MyChronometer extends Chronometer {

	private int msElapsed;
	private boolean isRunning = false;

	/**
	 * MyChronomter constructor. Sets the base time to the current time.
	 * 
	 * @param context to use to define the Chronomter
	 */
	public MyChronometer(Context context) {
		super(context);
	}
	
	/**
	 * MyChronomter constructor. Sets the base time to the current time
	 * and initializes with standard view layout information.
	 * 
	 * @param context to use to define the Chronomter
	 * @param attrs
	 */
	public MyChronometer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * MyChronomter constructor. Sets the base time to the current time
	 * and initializes with standard view layout information and style.
	 * 
	 * @param context to use to define the Chronomter
	 * @param attrs
	 * @param defStyle
	 */
	public MyChronometer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * getMsElapsed returns the number of milliseconds the chronometer
	 * incremented for from the base time.
	 * 
	 * @return int number of elapsed milliseconds
	 */
	public int getMsElapsed() {
		return msElapsed;
	}

	/**
	 * setMsElapsed is used to set the base time to a specific number of
	 * milliseconds.
	 * 
	 * @param ms number of milliseconds to set the base time to
	 */
	public void setMsElapsed(int ms) {
		setBase(getBase() - ms);
		msElapsed = ms;
	}

	/**
	 * start begins to increment the chronometer.
	 */
	@Override
	public void start() {
		super.start();
		setBase(SystemClock.elapsedRealtime() - msElapsed);
		isRunning = true;
	}
	
	/**
	 * startTiming begins to increment the chronometer and returns
	 * the base time.
	 * 
	 * @return long base time in milliseconds
	 */
	public long startTiming() {
		super.start();
		long base = SystemClock.elapsedRealtime() - msElapsed;
		setBase(base);
		isRunning = true;
		return base;
	}
	
	/**
	 * startAtBase begins to increment the chronometer from a specific
	 * base time.
	 * 
	 * @param base number of milliseconds to set the chronometer to
	 */
	public void startAtBase(long base) {
		super.start();
		setBase(base);
		isRunning = true;
	}

	/**
	 * stop pauses the chronometer.
	 */
	@Override
	public void stop() {
		super.stop();
		if (isRunning) {
			msElapsed = (int) (SystemClock.elapsedRealtime() - this.getBase());
		}
		isRunning = false;
	}
	
	/**
	 * isRunning returns true if the chronometer is incrementing;
	 * false otherwise.
	 * 
	 * @return boolean	whether the chronometer is running or not
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * setIsRunning changes the value of the class level isRunning
	 * variable.
	 * 
	 * @param mIsRunning whether the chronometer is running or not
	 */
	public void setIsRunning(boolean mIsRunning) {
		isRunning = mIsRunning;
	}
	
	/**
	 * clear sets the base time to 0.
	 */
	public void clear() {
		msElapsed = 0;
		setBase(SystemClock.elapsedRealtime());
	}
	
	/**
	 * getElapsedTime returns the number of milliseconds the chronometer
	 * incremented for from the base time.
	 * 
	 * @return int number of elapsed milliseconds
	 */
	public int getElapsedTime() {
		int mTime = 0;
		if (isRunning) {
			mTime = (int) (SystemClock.elapsedRealtime() - this.getBase());
		} else {
			mTime = getMsElapsed();
		}
		return mTime;
	}
	
	/**
	 * toString method shows class level and relevant system information.
	 * 
	 * @return String
	 */
	public String toString() {
		return "msElapsed - " + msElapsed
				+ " isRunning - " + isRunning
				+ " elapsedRealTime - " + SystemClock.elapsedRealtime()
				+ " base - " + this.getBase();
	}
}
