// Please note this must be the package if you want to use XML-based preferences
package com.wallpapers_manager.cyril;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePickerPreference extends DialogPreference {

	TimePicker timePicker;

	public TimePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		setPersistent(true);
	}

	@Override
	protected View onCreateDialogView() {
		timePicker = new TimePicker(getContext());
		timePicker.setIs24HourView(true);
		// TODO put default value in @string
		final String storedValue = getPersistedString("01:00");
		final String[] split = storedValue.split(":");
		timePicker.setCurrentHour(Integer.parseInt(split[0]));
		timePicker.setCurrentMinute(Integer.parseInt(split[1]));
		return timePicker;
	}

	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			// Duration can't be null
			if (timePicker.getCurrentHour() == 0 && timePicker.getCurrentMinute() == 0) {
				// TODO put text in @string
				Toast.makeText(getContext(), "Duration can't be null", Toast.LENGTH_SHORT).show();
			} else {
				final String result = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
				persistString(result);
			}
		}
	}
}
