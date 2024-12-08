package com.example.cqqch;

import android.os.Bundle;
import android.widget.Toast;

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

