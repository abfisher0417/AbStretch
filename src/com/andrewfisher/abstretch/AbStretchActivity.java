package com.andrewfisher.abstretch;

/**
 * AbStretchActivity represents an ab stretch activity.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchActivity {
	private long actID;
	private long actDateTime;
	private long actDuration;
	private String actType;
	private long actDate;

	public long getActID() {
		return actID;
	}
	
	public long getActDateTime() {
		return actDateTime;
	}
	
	public long getActDuration() {
		return actDuration;
	}
	
	public String getActType() {
		return actType;
	}
	
	public long getActDate() {
		return actDate;
	}
	
	public void setActID(long id) {
		actID = id;
	}
	
	public void setActDateTime(long dateTime) {
		actDateTime = dateTime;
	}
	
	public void setActDuration(long duration) {
		actDuration = duration;
	}
	
	public void setActType(String type) {
		actType = type;
	}
	
	public void setActDate(long date) {
		actDate = date;
	}

}
