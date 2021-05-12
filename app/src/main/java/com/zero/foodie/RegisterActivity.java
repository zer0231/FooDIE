package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
   EditText nameTxt,addressTxt,emailTxt,passwordTxt,cpasswordTxt;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTxt = findViewById(R.id.registerName);
        addressTxt = findViewById(R.id.registerAddress);
        emailTxt = findViewById(R.id.registerEmail);
        passwordTxt = findViewById(R.id.registerPassword);
        cpasswordTxt = findViewById(R.id.registerCPassword);
        registerButton = findViewById(R.id.registerButton);

        String name,address,email,password,cpassword;

        name = nameTxt.getText().toString().trim();
        address = addressTxt.getText().toString();
        email = emailTxt.getText().toString();
        password = passwordTxt.getText().toString();
        cpassword = cpasswordTxt.getText().toString();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, address+" "+email+" "+cpassword, Toast.LENGTH_SHORT).show();
//                if(checkPassword(password,cpassword))
//                {
//                    Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    new SharedPreferenceHandler(RegisterActivity.this).saveUser(name);
//                    Toast.makeText(RegisterActivity.this, "Register Success "+name, Toast.LENGTH_SHORT).show();
//                  //  sharedPreferenceHandler.saveUser(name);
//
//                }
                startActivity(new Intent(RegisterActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));//After finishing its task
            }
        });


    }

    private boolean checkPassword(String password, String cpassword) {
        if(password == cpassword)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}