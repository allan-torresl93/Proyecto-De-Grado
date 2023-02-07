package com.allan.camvor.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allan.camvor.R;
import com.allan.camvor.activities.client.MapClientActivity;
import com.allan.camvor.activities.driver.MapDriverActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    SharedPreferences mPref;


    AlertDialog mDialog;

    private CircleImageView mCircleImageBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //MyToolbar.show(this, "Login de Usuario",true);

        mTextInputEmail = findViewById(R.id.textImputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mCircleImageBack = findViewById(R.id.circleImageBack);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);

        //mDialog = new SpotsDialog.Builder().setContex();

        mButtonLogin.setOnClickListener(view -> login());
        mCircleImageBack.setOnClickListener(v -> finish());
    }

    private void login(){
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()){
            if (password.length() >= 6){
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String user = mPref.getString("user", "");
                        if (user.equals("client")){
                            Intent intent = new Intent(LoginActivity.this, MapClientActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(LoginActivity.this, MapDriverActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "La Contraseña o El Correo Son Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}