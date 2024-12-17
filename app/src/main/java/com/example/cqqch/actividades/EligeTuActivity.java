package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;

public class EligeTuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        getLayoutInflater().inflate(R.layout.activity_elige_tu, findViewById(R.id.content_frame));

        setupEligeTuActivity();
    }

    private void setupEligeTuActivity() {
        Toast.makeText(this, "Bienvenido a Elige Tú", Toast.LENGTH_SHORT).show();
    }
}

