package com.andrewfisher.abstretch;

import android.view.LayoutInflater;
import android.view.View;

/**
 * AbStretchActivityItem interface for the list activity, which has a header and item.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public interface AbStretchActivityItem {
	public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
