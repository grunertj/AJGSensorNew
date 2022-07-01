package com.example.ajgsensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    String global_fragments[];
    MainFragment mainFragment=null;
    SettingsFragment settingsFragment=null;
    DebugFragment debugFragment=null;
    TabLayout tabLayout;
    ViewPager viewPager;
    static final int TYPE_GPS = 0;

    static final int OVERRIDE = 1;
    static final int APPEND = 2;
    static final int NEW = 3;

    static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;

    static boolean COMPRESS = true;
    static boolean FILTERED = false;

    static boolean dim_state = true;

    static int file_mode = NEW;

    static boolean[] log;

    static int curBrightnessValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        log = new boolean[100];

        log[TYPE_GPS] = false;
        log[Sensor.TYPE_ACCELEROMETER] = false;
        log[Sensor.TYPE_LINEAR_ACCELERATION] = false;
        log[Sensor.TYPE_STEP_DETECTOR] = false;
        log[Sensor.TYPE_STEP_COUNTER] = false;
        log[Sensor.TYPE_GYROSCOPE] = false;
        log[Sensor.TYPE_GYROSCOPE_UNCALIBRATED] = false;
        log[Sensor.TYPE_ROTATION_VECTOR] = false;


    }
}