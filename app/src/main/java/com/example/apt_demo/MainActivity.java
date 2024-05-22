package com.example.apt_demo;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.example.annotation.Print;
import com.example.apt_demo.PrintUtil;


public class MainActivity extends ComponentActivity {
    private static final String TAG = "MainActivity---";
    @Print
    private String name;
    @Print
    private int age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PrintUtil.print$$name();
        PrintUtil.print$$age();
    }
}
