package com.andrewfisher.abstretch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * AbStretchActivityArrayAdapter is the array adapter used by the AbStretchView activity.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchActivityArrayAdapter extends ArrayAdapter<AbStretchActivityItem> {

    private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public AbStretchActivityArrayAdapter(Context context, List<AbStretchActivityItem> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
    
    public void updateAbStretchActivityArrayAdapter(List<AbStretchActivityItem> items) {
    	clear();
    	addAll(items);
    	notifyDataSetChanged();
    }

}
