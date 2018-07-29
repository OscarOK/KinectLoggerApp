package mx.uach.hcilab.kinectlogger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LevelSelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LevelSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelSelectorFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    NumberPicker picker;
    Button botonSelNiv;

    public LevelSelectorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LevelSelectorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelSelectorFragment newInstance() {
        return new LevelSelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_selector, container, false);
        picker = (NumberPicker) view.findViewById(R.id.LevelPicker);
        botonSelNiv = (Button) view.findViewById(R.id.ConfirmLevelButton);
        botonSelNiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickSelection();
            }
        });
        picker.setMinValue(1);
        picker.setMaxValue(9);
        picker.setWrapSelectorWheel(false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onClickSelection();

    }
}
