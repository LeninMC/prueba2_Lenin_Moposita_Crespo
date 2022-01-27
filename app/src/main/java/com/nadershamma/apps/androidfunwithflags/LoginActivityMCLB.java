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
    private String user1 = "lenin@gmail.com";
    private String user2 = "bryan@gmail.com";
    private String user1pass = "lenin123";
    private String user2pass = "bryan123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mclb);

        etCorreo = findViewById(R.id.txtCorreo);
        etContraseña = findViewById(R.id.txtContraseña);

        btnAceptar = findViewById(R.id.btnAceptar);
    }

    public void onClicAceptar(View view) {

        String user = etCorreo.getText().toString();
        String password = etContraseña.getText().toString();
        //nombre.toUpperCase(Locale.ROOT);
        if (!user.matches("") && !password.matches("") ) {
          if ((user.matches("lenin@gmail.com") && password.matches("lenin123")) ||
                  (user.matches("bryan@gmail.com") && password.matches("bryan123"))){
              Intent intent = new Intent(this, MainActivityMCLB.class);
              intent.putExtra("key_user",user);

              startActivity(intent);
          }else {
              Toast.makeText(this, "Correo o contraseña erroneos ", Toast.LENGTH_LONG).show();
          }

        } else {
            Toast.makeText(this, "Correo y contraseña son obligatorios", Toast.LENGTH_LONG).show();
        }
    }
}