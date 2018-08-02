package mx.uach.hcilab.kinectlogger.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment  {
    private DatePickerDialog.OnDateSetListener mListener;
    private static final short PATIENT_AGE = 10;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), mListener, year-PATIENT_AGE, month, day);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof DatePickerDialog.OnDateSetListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mListener = (DatePickerDialog.OnDateSetListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }



}
