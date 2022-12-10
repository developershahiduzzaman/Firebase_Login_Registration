package com.ftbd.firebaseloginregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {
    EditText regName,regPhone,regEmail,regPassword,regConfirmPassword;
    Button regButton;
    TextView goToLogin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        regName = findViewById(R.id.regName);
        regPhone = findViewById(R.id.regPhone);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regConfirmPassword = findViewById(R.id.regConfirmPassword);

        regButton = findViewById(R.id.regButton);
        goToLogin = findViewById(R.id.goToLogin);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = regName.getText().toString();
                String phone = regPhone.getText().toString();
                String email = regEmail.getText().toString();
                String pass = regPassword.getText().toString();
                String confirmpass = regConfirmPassword.getText().toString();

                if (name.isEmpty()){
                    regName.setError("Please Enter Your Name");
                    return;
                }
                else if (phone.isEmpty()){
                    regPhone.setError("Enter Phone Number");
                }
                else if(email.isEmpty()){
                    regEmail.setError("Enter Your Email");
                    return;
                }
                else if (pass.isEmpty()|| !pass.equals(confirmpass) || pass.length()<6){
                    regPassword.setError("Password Not Match");
                    return;
                }
                else if (confirmpass.isEmpty() || confirmpass.length() < 6){
                    regConfirmPassword.setError("Password Not Match");
                    return;
                }

                CreateNewAccount(email,pass);

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,Login.class));
            }
        });

    }

    private void CreateNewAccount(String email, String pass) {

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    UpdateUi(user,email);
                }

                else {
                    Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void UpdateUi(FirebaseUser user, String email) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",regName.getText().toString());
        map.put("email",regEmail.getText().toString());
        map.put("phone",regPhone.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.child(user.getUid())
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //after reg complete
                            startActivity(new Intent(Registration.this,Login.class));
                            Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(Registration.this, "Error...Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}