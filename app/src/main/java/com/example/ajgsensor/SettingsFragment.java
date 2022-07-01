package com.example.ajgsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    CheckBox checkBox, checkBoxCompress, checkBoxGPS, checkBoxDelayed;
    ArrayList<CheckBox> checkBoxes;
    SensorManager sensorManager = null;
    RadioGroup radioGroup, radioGroup2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        // return inflater.inflate(R.layout.fragment_settings, container, false);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioGroup2 = (RadioGroup)view.findViewById(R.id.radioGroup2);
        checkBoxCompress = (CheckBox)view.findViewById(R.id.checkBoxCompress);
        checkBoxDelayed = (CheckBox)view.findViewById(R.id.checkBoxDelayed);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.dim_screen(false, getActivity());
                }
                return false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonOverride:
                        MainActivity.file_mode = MainActivity.OVERRIDE;
                        break;
                    case R.id.radioButtonNew:
                        MainActivity.file_mode = MainActivity.NEW;
                        break;
                    default:
                        MainActivity.file_mode = MainActivity.NEW;
                        break;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonDefault:
                        MainActivity.SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case R.id.radioButtonGame:
                        MainActivity.SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case R.id.radioButtonFastest:
                        MainActivity.SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    default:
                        MainActivity.SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
            }
        });

        checkBoxCompress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.COMPRESS = true;
                } else {
                    MainActivity.COMPRESS = false;
                }
            }
        });

        checkBoxDelayed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.FILTERED = true;
                } else {
                    MainActivity.FILTERED = false;
                }
            }
        });

        checkBoxCompress.setChecked(true);
        checkBoxDelayed.setChecked(false);

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

        checkBoxes = new ArrayList<CheckBox>();

        checkBoxGPS = new CheckBox(view.getContext());
        checkBoxGPS.setText("GPS Location");
        checkBoxGPS.setChecked(true);
        checkBoxGPS.setId(MainActivity.TYPE_GPS);

        checkBoxGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
                        MainActivity.log[MainActivity.TYPE_GPS] = false;
                        checkBoxGPS.setChecked(false);
                        Toast.makeText(getActivity().getApplicationContext(), "Please enable Location.", Toast.LENGTH_LONG).show();
                    } else {
                        MainActivity.log[MainActivity.TYPE_GPS] = true;
                    }
                } else {
                    MainActivity.log[MainActivity.TYPE_GPS] = false;
                }
            }
        });

        checkBoxes.add(checkBoxGPS);
        linearLayout.addView(checkBoxGPS);

        for (Sensor sensor: sensors) {
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                case Sensor.TYPE_LINEAR_ACCELERATION:
                case Sensor.TYPE_GYROSCOPE:
                    checkBox = new CheckBox(view.getContext());
                    checkBox.setText(sensor.getType() + " " + sensor.getName() + " " + sensor.getVendor());
                    checkBox.setChecked(true);
                    checkBox.setId(sensor.getType());
                    checkBoxes.add(checkBox);
                    linearLayout.addView(checkBox);
                    break;
                // case Sensor.TYPE_STEP_DETECTOR:
                // case Sensor.TYPE_STEP_COUNTER:
                case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                case Sensor.TYPE_ROTATION_VECTOR:
                    checkBox = new CheckBox(view.getContext());
                    checkBox.setText(sensor.getType() + " " + sensor.getName() + " " + sensor.getVendor());
                    checkBox.setChecked(false);
                    checkBox.setId(sensor.getType());
                    checkBoxes.add(checkBox);
                    linearLayout.addView(checkBox);
                    break;
                default:
                    break;
            }
        }

        sensorManager = null;

        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkBoxGPS.isChecked()) {
            MainActivity.log[MainActivity.TYPE_GPS] = true;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            MainActivity.dim_screen(false, getActivity());
        } else if (isResumed()) {
            for (CheckBox checkBox: checkBoxes) {
                MainActivity.log[checkBox.getId()] = checkBox.isChecked();
            }
        }
    }
}