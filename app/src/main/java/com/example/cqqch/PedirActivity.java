package com.example.cqqch;

import android.os.Bundle;
import android.widget.Toast;

public class PedirActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        getLayoutInflater().inflate(R.layout.activity_pedir, findViewById(R.id.content_frame));

        setupPedirActivity();
    }

    private void setupPedirActivity() {
        Toast.makeText(this, "Bienvenido a Pedir", Toast.LENGTH_SHORT).show();
    }
}
