package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button RegesterButton,loginButton;
    private EditText emailTxt;
    private EditText passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(this, "WOW", Toast.LENGTH_SHORT).show();
        RegesterButton = findViewById(R.id.lognRegisterButton);
        emailTxt = findViewById(R.id.loginEmail);
        passwordTxt = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, emailTxt.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                new SharedPreferenceHandler(LoginActivity.this).saveUser(emailTxt.getText().toString().trim());
                startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });


        RegesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}