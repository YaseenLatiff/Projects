package com.said.icare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.said.icare.databinding.ActivityMainBinding;

public class Welcome extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }
}