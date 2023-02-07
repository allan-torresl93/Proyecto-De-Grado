package com.allan.camvor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.camvor.R;
import com.allan.camvor.activities.client.RegisterActivity;
import com.allan.camvor.activities.driver.RegisterDriverActivity;

public class SelectOptionAuthActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Button mButtonGoToLogin;
    Button mButtonGoToRegister;
    SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Seleccionar Opcion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonGoToLogin = findViewById(R.id.btnGoToLogin);
        mButtonGoToRegister = findViewById(R.id.btnGoToRegister);
        mButtonGoToLogin.setOnClickListener(view -> goToLogin());
        mButtonGoToRegister.setOnClickListener(view -> goToRegister());

        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);

    }

    public void goToLogin(){
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegister(){
        String typeUser = mPref.getString("user","");
        Intent intent;
        if (typeUser.equals("client")){
            intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
        }
        else{
            intent = new Intent(SelectOptionAuthActivity.this, RegisterDriverActivity.class);
        }
        startActivity(intent);

    }
}