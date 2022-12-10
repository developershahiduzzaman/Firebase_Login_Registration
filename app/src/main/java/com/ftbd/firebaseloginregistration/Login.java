package com.ftbd.firebaseloginregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    Button loginButton;
    TextView goToReg;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        goToReg = findViewById(R.id.goToReg);
        auth = FirebaseAuth.getInstance();

        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    loginEmail.setError("Enter Registered Email Address");
                    return;
                }
                else if (TextUtils.isEmpty(password)){
                    loginPassword.setError("Enter Your Password");
                    return;
                }
                else {
                    Signin(email,password);
                }
            }
        });
    }

    private void Signin(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Login.this,MainActivity.class));
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else {
                            Toast.makeText(Login.this, "Login Unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}