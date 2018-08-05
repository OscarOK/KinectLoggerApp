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
import android.widget.Toast;

import com.fxn769.Numpad;
import com.fxn769.TextGetListner;

import java.text.ParseException;

import mx.uach.hcilab.kinectlogger.R;

public class GeneralTimeSelector extends DialogFragment {

    private static final String TAG = "GeneralTimeSelector";

    public interface OnInputListener {
        public void sendSelectedTime(int generalTime);

        public void goBack();

        public void onChoose();
    }

    private OnInputListener onInputListener;
    private TextView timeInput;

    public GeneralTimeSelector() {
    }

    public static GeneralTimeSelector newInstance(String display) {

        Bundle args = new Bundle();

        args.putString("display", display);

        GeneralTimeSelector fragment = new GeneralTimeSelector();
        fragment.setArguments(args);
        return fragment;
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
        timeInput = view.findViewById(R.id.time_input);
        Numpad numpad = view.findViewById(R.id.num);

        numpad.setOnTextChangeListner(new TextGetListner() {
            @Override
            public void onTextChange(String text, int digits_remaining) {
                if (text.length() == 0) {
                    timeInput.setText("0");
                } else {
                    timeInput.setText(text);
                }
            }
        });

        builder.setView(view);

        builder.setPositiveButton(R.string.fragment_next_button, null);

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
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int generalTime = Integer.parseInt(timeInput.getText().toString());

                if (generalTime != 0) {
                    onInputListener.sendSelectedTime(generalTime);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), R.string.general_time_invalid_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public void onDetach() {
        onInputListener = null;
        super.onDetach();
    }
}
