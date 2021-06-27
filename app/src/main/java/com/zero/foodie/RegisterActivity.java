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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.foodie.model.UserDetail;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    int RC_SIGN_IN = 35;
    private EditText nameEditTxt, addressEditTxt, emailEditTxt, passwordEditTxt,phoneEditTxt;
    private Button register, googleRegister;
    private ProgressBar registerProgressbar;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.registerSimple);
        register.setOnClickListener(this);

        googleRegister = findViewById(R.id.registerGoogle);
        googleRegister.setOnClickListener(this);

        // Configure Google Sign In
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerSimple:
                try {
                    register();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.registerGoogle:
                try {
                    googleRegister();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void googleRegister() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fireUser = mAuth.getCurrentUser();
                    UserDetail googleUser = new UserDetail(fireUser.getDisplayName(), "", fireUser.getEmail(), "","");
                    googleUser.setUserAddress("Earth");
                    googleUser.setUserProfilePicture(fireUser.getPhotoUrl().toString());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(fireUser.getUid()).setValue(googleUser);
                    startActivity(new Intent(RegisterActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(RegisterActivity.this, "Sign success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register() {
        String name, address, email, password,phoneNumber;
        registerProgressbar = findViewById(R.id.registerProgressbar);
        registerProgressbar.setVisibility(View.VISIBLE);
        nameEditTxt = findViewById(R.id.registerFullName);
        addressEditTxt = findViewById(R.id.registerAddress);
        emailEditTxt = findViewById(R.id.registerEmail);
        passwordEditTxt = findViewById(R.id.registerPassword);
        phoneEditTxt = findViewById(R.id.registerPhoneNumber);

        name = nameEditTxt.getText().toString().trim();
        address = addressEditTxt.getText().toString().trim();
        email = emailEditTxt.getText().toString().trim();
        password = passwordEditTxt.getText().toString().trim();
        phoneNumber = phoneEditTxt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTxt.setError("The Email is invalid");
            emailEditTxt.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            nameEditTxt.setError("Name is required");
            nameEditTxt.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            addressEditTxt.setError("Address is required");
            addressEditTxt.requestFocus();
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

        UserDetail user = new UserDetail(name, address, email, password,phoneNumber);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                registerProgressbar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to Store credential", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to Authenticate", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerProgressbar.setVisibility(View.INVISIBLE);
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//    }
}