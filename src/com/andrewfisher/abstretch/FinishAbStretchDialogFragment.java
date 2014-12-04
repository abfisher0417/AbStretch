package com.andrewfisher.abstretch;

import com.andrewfisher.abstretch.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * FinishAbStretchDialogFragment presents the user with a dialog
 * to save or discard the recorded core or mobility activity.
 * 
 * @author Andrew Fisher
 * @version 1.0
 */
public class FinishAbStretchDialogFragment extends DialogFragment {

	public static FinishAbStretchDialogFragment newInstance(int title) {
		FinishAbStretchDialogFragment frag = new FinishAbStretchDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");

		return new AlertDialog.Builder(getActivity())
		.setMessage(title)
		.setPositiveButton(R.string.alert_dialog_save,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AbStretchHome) getActivity()).saveActivity();
			}
		})
		.setNegativeButton(R.string.alert_dialog_cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				((AbStretchHome) getActivity())
				.cancelActivity();
			}
		}).create();
	}

}
