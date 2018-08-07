package mx.uach.hcilab.kinectlogger.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import mx.uach.hcilab.kinectlogger.R;

public class LevelSelector extends DialogFragment {

    private static final String TAG = "LevelSelector";

    public interface OnInputListener {
        public void sendSelectedLevel(int level);

        public void onChoose();

        void goBack();
    }

    private OnInputListener onInputListener;
    private MaterialNumberPicker numberPicker;

    public LevelSelector() {
    }

    public static LevelSelector newInstance(int maxLevel, int currentNumber) {

        Bundle args = new Bundle();
        args.putInt("max_level", maxLevel);
        args.putInt("current_number", currentNumber);

        LevelSelector fragment = new LevelSelector();
        fragment.setArguments(args);
        fragment.setCancelable(false);
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
        builder.setTitle(R.string.level_selector_title);


        View view = getActivity().getLayoutInflater().inflate(R.layout.level_selector, null);
        numberPicker = view.findViewById(R.id.level_selector_number_picker);

        if (getArguments() != null) {
            numberPicker.setMaxValue(getArguments().getInt("max_level"));
            numberPicker.setValue(getArguments().getInt("current_number"));
        }

        builder.setView(view);

        builder.setPositiveButton(R.string.fragment_next_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.sendSelectedLevel(numberPicker.getValue());
            }
        });

        builder.setNegativeButton(R.string.fragment_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //getActivity().finish();
                onInputListener.onChoose();
            }
        });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        keyEvent.getAction() == KeyEvent.ACTION_UP &&
                        !keyEvent.isCanceled()) {
                    onInputListener.onChoose();
                    return true;
                }

                return false;
            }
        });

        return builder.create();
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
