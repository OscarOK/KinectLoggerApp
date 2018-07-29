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

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import mx.uach.hcilab.kinectlogger.R;

public class LevelSelector extends DialogFragment {

    private static final String TAG = "LevelSelector";

    public interface OnInputListener {
        public void sendSelectedNumber(int number);

        public void onChoose();
    }

    private OnInputListener onInputListener;
    private MaterialNumberPicker numberPicker;

    public LevelSelector() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.level_selector_title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.level_selector, null);
        numberPicker = view.findViewById(R.id.level_selector_number_picker);
        builder.setView(view);

        builder.setPositiveButton(R.string.fragment_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.sendSelectedNumber(numberPicker.getValue());
            }
        });

        builder.setNegativeButton(R.string.fragment_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //getActivity().finish();
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

    @Override
    public void onDetach() {
        onInputListener = null;
        super.onDetach();
    }
}
