package com.zingakart.android.miscellaneous;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zingakart.android.R;

public class EmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }
}
