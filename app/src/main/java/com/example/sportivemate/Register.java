package com.example.sportivemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView loginLabel;
    private EditText mEmail, mPassword;
    private Button registerBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TextView fullName = findViewById(R.id.register_full_name);
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        loginLabel = findViewById(R.id.register_login);
        registerBtn = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.register_progressBar);
        loginLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required!");
                    return;
                }
                if(password.length() < 6) {
                    mPassword.setError("Password is too short <6");
                    return;
                }
                registerBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                register(email, password);
            }
        });
    }

    void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", user.getEmail() + " Created!");
                            Toast.makeText(Register.this, user.getEmail() + " Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Failed to create user.", Toast.LENGTH_SHORT).show();
                            registerBtn.setEnabled(true);

                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

}