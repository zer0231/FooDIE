package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditTxt, passwordEditTxt;
    private Button login, register;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginSimple);
        login.setOnClickListener(this);

        register = findViewById(R.id.loginRegister);
        register.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginSimple:
                login();
                break;
            case R.id.loginRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }

    }

    private void login() {
        String email, password;
        emailEditTxt = findViewById(R.id.loginEmail);
        passwordEditTxt = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.loginProgress);
        progressBar.setVisibility(View.VISIBLE);

        email = emailEditTxt.getText().toString().trim();
        password = passwordEditTxt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTxt.setError("The Email is invalid");
            emailEditTxt.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditTxt.setError("Email is required");
            emailEditTxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditTxt.setError("Password is required");
            passwordEditTxt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditTxt.setError("Password is short");
            passwordEditTxt.requestFocus();
            return;
        }

        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        // there was an error
                        Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}