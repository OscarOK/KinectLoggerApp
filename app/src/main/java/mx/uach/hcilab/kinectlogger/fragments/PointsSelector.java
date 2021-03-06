package mx.uach.hcilab.kinectlogger.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
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

    public static PointsSelector newInstance() {

        Bundle args = new Bundle();

        PointsSelector fragment = new PointsSelector();
        fragment.setArguments(args);
        fragment.setCancelable(false);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.points_title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.number_pad, null);
        unitText = view.findViewById(R.id.text_view_unit);
        timeInput = view.findViewById(R.id.time_input);
        Numpad numpad = view.findViewById(R.id.num);

        unitText.setVisibility(View.GONE);

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

        builder.setPositiveButton(R.string.fragment_finish_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onInputListener.sendSelectedPoints(Integer.parseInt(timeInput.getText().toString()));
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
