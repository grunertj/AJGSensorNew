package com.example.ajgsensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebugFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugFragment extends Fragment {
    Button buttonSensors, buttonShare;
    TextView textViewSensors;
    SensorManager sensorManager = null;
    String phone = Build.MODEL+" "+Build.BRAND+" "+Build.MANUFACTURER+" "+Build.VERSION.RELEASE;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DebugFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebugFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebugFragment newInstance(String param1, String param2) {
        DebugFragment fragment = new DebugFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_debug, container, false);
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        textViewSensors = (TextView) view.findViewById(R.id.textViewSensors);
        buttonSensors = (Button) view.findViewById(R.id.buttonSensors);
        buttonShare = (Button) view.findViewById(R.id.buttonShare);
        buttonShare.setEnabled(false);

        buttonSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSensors.setText(""+phone+"\n");
                sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ALL);
                ArrayList<Sensor> sensors = new ArrayList<Sensor>();
                sensors.addAll(list);

                Collections.sort(sensors, new Comparator<Sensor>() {
                    @Override
                    public int compare(Sensor lhs, Sensor rhs) {
                        return lhs.getType() - rhs.getType();
                    }
                });

                for (Sensor sensor: sensors) {
                    textViewSensors.append(sensor.getType()+"\n\t"+sensor.getName() + "\n\t"+sensor.getVendor()+"\n\n");
                }
                sensorManager = null;
                buttonShare.setEnabled(true);
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                String string = textViewSensors.getText().toString();
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Phone: "+phone);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, string);
                startActivity(sharingIntent);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.dim_state = MainActivity.dim_screen(false, getActivity());
                }
                return false;
            }
        });

        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            MainActivity.dim_screen(false, getActivity());
        } else if (isResumed()) {

        }
    }
}