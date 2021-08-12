package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zero.foodie.model.UserDetail;

public class LoginAndSignupActivity extends AppCompatActivity {
    int RC_SIGN_IN = 35;
    Button login, signup;
    ImageButton registerGoogle;
    EditText LemailEditTxt, LpasswordEditTxt, RnameEditTxt, RemailEditTxt, RpasswordEditTxt, RphoneEditTxt;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginandsignup);
        mAuth = FirebaseAuth.getInstance();
        registerGoogle = findViewById(R.id.registerGoogle);
        RadioButton loginRadio = findViewById(R.id.radio_login_btn);
        login = findViewById(R.id.loginSimple);
        signup = findViewById(R.id.registerSimple);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        LinearLayout registerLinearLayout = findViewById(R.id.registerLinearLayout);
        LinearLayout loginLinearLayout = findViewById(R.id.loginLinearLayout);

        loginRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerLinearLayout.setVisibility(View.GONE);
                loginLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        RadioButton signupRadio = findViewById(R.id.radio_signup_btn);
        signupRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerLinearLayout.setVisibility(View.VISIBLE);
                loginLinearLayout.setVisibility(View.GONE);
            }
        });
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

        registerGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleRegister();
            }
        });

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
                FancyToast.makeText(LoginAndSignupActivity.this,e.toString(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginAndSignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fireUser = mAuth.getCurrentUser();
                    UserDetail googleUser = new UserDetail(fireUser.getDisplayName(), "", fireUser.getEmail(), "","");
                    googleUser.setUserAddress("Earth");
                    googleUser.setUserProfilePicture(fireUser.getPhotoUrl().toString());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(fireUser.getUid()).setValue(googleUser);
                    FancyToast.makeText(LoginAndSignupActivity.this,"Sign in Success", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                    startActivity(new Intent(LoginAndSignupActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                  } else {
                    FancyToast.makeText(LoginAndSignupActivity.this,"Sign in unsuccessful", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }
            }
        });
    }

    private void signup() {
        String name, address, email, password, phoneNumber;
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        RnameEditTxt = findViewById(R.id.registerFullName);
//        addressEditTxt = findViewById(R.id.registerAddress);
        RemailEditTxt = findViewById(R.id.registerEmail);
        RpasswordEditTxt = findViewById(R.id.registerPassword);
        RphoneEditTxt = findViewById(R.id.registerPhoneNumber);

        name = RnameEditTxt.getText().toString().trim();
//        address = RaddressEditTxt.getText().toString().trim();
        email = RemailEditTxt.getText().toString().trim();
        password = RpasswordEditTxt.getText().toString().trim();
        phoneNumber = RphoneEditTxt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            RemailEditTxt.setError("The Email is invalid");
            RemailEditTxt.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            RnameEditTxt.setError("Name is required");
            RnameEditTxt.requestFocus();
            return;
        }

//        if (address.isEmpty()) {
//            addressEditTxt.setError("Address is required");
//            addressEditTxt.requestFocus();
//            return;
//        }

        if (email.isEmpty()) {
            RemailEditTxt.setError("Email is required");
            RemailEditTxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            RpasswordEditTxt.setError("Password is required");
            RpasswordEditTxt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            RpasswordEditTxt.setError("Password is short");
            RpasswordEditTxt.requestFocus();
            return;
        }

        UserDetail user = new UserDetail(name, "Somewhere", email, password, phoneNumber);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FancyToast.makeText(LoginAndSignupActivity.this,"Successfully Registered", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                startActivity(new Intent(LoginAndSignupActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                FancyToast.makeText(LoginAndSignupActivity.this,task.getException().getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }
                    });
                } else {
                    FancyToast.makeText(LoginAndSignupActivity.this,"The task was unsuccessful", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void login() {
        FancyToast.makeText(LoginAndSignupActivity.this,"Please Wait", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        String email, password;
        LemailEditTxt = findViewById(R.id.loginEmail);
        LpasswordEditTxt = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        email = LemailEditTxt.getText().toString().trim();
        password = LpasswordEditTxt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            LemailEditTxt.setError("The Email is invalid");
            LemailEditTxt.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            LemailEditTxt.setError("Email is required");
            LemailEditTxt.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            LpasswordEditTxt.setError("Password is required");
            LpasswordEditTxt.requestFocus();
            return;
        }

        if (password.length() < 6) {
            LpasswordEditTxt.setError("Password is short");
            LpasswordEditTxt.requestFocus();
            return;
        }

        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginAndSignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        // there was an error
                        FancyToast.makeText(LoginAndSignupActivity.this,"Email or Password is Incorrect", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } else {
                        Intent intent = new Intent(LoginAndSignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } catch (Exception e) {
            FancyToast.makeText(LoginAndSignupActivity.this,e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }

    }


}
