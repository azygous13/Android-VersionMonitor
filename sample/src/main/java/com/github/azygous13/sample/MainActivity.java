package com.github.azygous13.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.azygous13.versionmonitor.VersionMonitor;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VersionMonitor.init(this).monitor();
    }
}
