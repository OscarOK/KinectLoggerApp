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

public class ConfirmFragment extends DialogFragment {
    private static final String TAG = "ConfirmFragment";

    public interface OnInputListener {
        public void confirmPressed();
        public void goBack();
        public void onChoose();
    }

    private OnInputListener onInputListener;

    public ConfirmFragment() {
    }

    public static ConfirmFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString("message", message);

        ConfirmFragment fragment = new ConfirmFragment();
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
        builder.setTitle(R.string.confirmation_title);

        if (getArguments() != null) {
            builder.setMessage(getArguments().getString("message"));
        }

        builder.setPositiveButton(R.string.fragment_start_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.confirmPressed();
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

    @Override
    public void onDetach() {
        onInputListener = null;
        super.onDetach();
    }
}
