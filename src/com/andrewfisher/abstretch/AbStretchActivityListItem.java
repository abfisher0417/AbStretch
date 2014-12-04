package com.andrewfisher.abstretch;

import com.andrewfisher.abstretch.AbStretchActivityArrayAdapter.RowType;
import com.andrewfisher.abstretch.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * AbStretchActivityListItem represents an activity. It has an activity type
 * (label) and duration (value).
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchActivityListItem implements AbStretchActivityItem {

	private String label;
	private String value;
	
	public AbStretchActivityListItem(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;

		if (convertView == null) {
			view = (View) inflater.inflate(R.layout.list_item, null);
		} else {
			view = convertView;
		}

		TextView mLabel = (TextView) view.findViewById(R.id.list_content1);
		TextView mValue = (TextView) view.findViewById(R.id.list_content2);
		mLabel.setText(label);
		mValue.setText(value);

		return view;
	}

}
