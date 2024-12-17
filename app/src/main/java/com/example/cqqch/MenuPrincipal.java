package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class MenuPrincipal extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    //Este es un menú muy básico que luego usaremos y desarrollaremos en nuestra app, lo único que configuramos es el botón para retroceder
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        floatingActionButton = findViewById(R.id.fabmenuprincipal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}