package com.nadershamma.apps.androidfunwithflags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class LoginActivityMCLB extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etContraseña;
    private Button btnAceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mclb);

        etCorreo = findViewById(R.id.txtCorreo);
        etContraseña = findViewById(R.id.txtContraseña);

        btnAceptar = findViewById(R.id.btnAceptar);
    }

    public void onClicAceptar(View view) {
        String nombre = etCorreo.getText().toString();
        String apellido = etContraseña.getText().toString();
        //nombre.toUpperCase(Locale.ROOT);
        if (!nombre.matches("") && !apellido.matches("")) {
            Intent intent = new Intent(this, MainActivityMCLB.class);
            //intent.putExtra("key_nombre",nombre);
            // intent.putExtra("key_apellido",apellido);

            startActivity(intent);

        } else {
            Toast.makeText(this, "Correo y contraseña son obligatorios", Toast.LENGTH_LONG).show();
        }
    }
}