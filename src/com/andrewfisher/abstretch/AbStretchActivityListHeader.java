package com.andrewfisher.abstretch;

import com.andrewfisher.abstretch.AbStretchActivityArrayAdapter.RowType;
import com.andrewfisher.abstretch.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * AbStretchActivityListHeader represents a date. It has header text.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class AbStretchActivityListHeader implements AbStretchActivityItem {
	
	private String headerText;

	public AbStretchActivityListHeader(String headerText) {
		this.headerText = headerText;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		
		if (convertView == null) {
            view = (View) inflater.inflate(R.layout.list_header, null);
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(headerText);

        return view;
	}

}
