package com.allan.camvor.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allan.camvor.R;
import com.allan.camvor.activities.client.RegisterActivity;
import com.allan.camvor.includes.MyToolbar;
import com.allan.camvor.models.Client;
import com.allan.camvor.models.Driver;
import com.allan.camvor.providers.AuthProvider;
import com.allan.camvor.providers.ClientProvider;
import com.allan.camvor.providers.DriverProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterDriverActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    DriverProvider mDriverProvider;

    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputVehicleBrand;
    TextInputEditText mTextInputVehiclePlate;
    TextInputEditText mTextInputpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);
        MyToolbar.show(this, "Registro de Conductor",true);

        mAuthProvider = new AuthProvider();
        mDriverProvider = new DriverProvider();

        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputEmail = findViewById(R.id.textImputEmail);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputVehicleBrand = findViewById(R.id.textImputVehicleBrand);
        mTextInputVehiclePlate = findViewById(R.id.textImputVehiclePlate);
        mTextInputpassword = findViewById(R.id.textInputPassword);


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
            }
        });
    }

    void clickRegister(){
        final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String vehicleBrand = mTextInputVehicleBrand.getText().toString();
        final String vehiclePlate = mTextInputVehiclePlate.getText().toString();
        final String password = mTextInputpassword.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !vehicleBrand.isEmpty() && !vehiclePlate.isEmpty()){
            if (password.length() >=6){
                register(name, email, password, vehicleBrand, vehiclePlate);
            }
            else{
                Toast.makeText(this, "La Contraseña Debe Tener al Menos 6 Caracteres ", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Ingrese Todos Los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password, String vehicleBrand, String vehiclePlate){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getUid();
                    Driver driver = new Driver(id, name, email, vehicleBrand, vehiclePlate);
                    create(driver);
                }
                else{
                    Toast.makeText(RegisterDriverActivity.this, "No se Pudo Registrar el Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Driver driver){
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(RegisterDriverActivity.this, "El Registro se Realizo Exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterDriverActivity.this,MapDriverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(RegisterDriverActivity.this, "No se Pudo Crear el Cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}