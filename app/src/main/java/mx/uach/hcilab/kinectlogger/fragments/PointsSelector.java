package mx.uach.hcilab.kinectlogger.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fxn769.Numpad;
import com.fxn769.TextGetListner;

import mx.uach.hcilab.kinectlogger.R;

public class PointsSelector extends DialogFragment {
    private static final String TAG = "GeneralTimeSelector";

    public interface OnInputListener {
        public void sendSelectedPoints(int points);
        public void goBack();
        public void onChoose();
    }

    private OnInputListener onInputListener;
    private TextView timeInput;
    private TextView unitText;

    public PointsSelector() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.general_time_title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.number_pad, null);
        unitText = view.findViewById(R.id.text_view_unit);
        timeInput = view.findViewById(R.id.time_input);
        Numpad numpad = view.findViewById(R.id.num);

        unitText.setVisibility(View.INVISIBLE);

        numpad.setOnTextChangeListner(new TextGetListner() {
            @Override
            public void onTextChange(String text, int digits_remaining) {
                if (text.length() == 0) {
                    timeInput.setText("0");
                } else {
                    timeInput.setText(text);
                }
                Log.d("input",text+"  "+digits_remaining);
            }
        });

        builder.setView(view);

        builder.setPositiveButton(R.string.fragment_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.sendSelectedPoints(Integer.parseInt(timeInput.getText().toString()));
            }
        });

        builder.setNegativeButton(R.string.fragment_back_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.goBack();
            }
        });

        builder.setNeutralButton(R.string.fragment_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.onChoose();
            }
        });

        Dialog dialogFragment;
        dialogFragment = builder.create();
        dialogFragment.setCanceledOnTouchOutside(false);

        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
