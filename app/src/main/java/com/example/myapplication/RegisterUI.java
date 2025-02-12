package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterUI extends AppCompatActivity {
    private static final int RC_SIGN_IN =101 ;
    EditText username,email,password;
    Button submit;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    TextView signIn_btn;
    Button btnGoogle;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);

        username = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        submit = findViewById(R.id.sub_button);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        signIn_btn = findViewById(R.id.signin_textView);
        btnGoogle = findViewById(R.id.google_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnGoogle.setOnClickListener(v -> signIn());

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),LoginUI.class));
            finish();
        }

        submit.setOnClickListener(v -> {
            String emailUser = email.getText().toString().trim();
            String passUser = password.getText().toString().trim();

            if(TextUtils.isEmpty(emailUser)){
                email.setError("Email is Required..");
                return;
            }

            if(TextUtils.isEmpty(passUser)){
                password.setError("Password is Required..");
                return;
            }

            if(passUser.length() < 6){
                password.setError("Password Must be >= 6 characters..");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //register the user in firebase

            fAuth.createUserWithEmailAndPassword(emailUser,passUser).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterUI.this, "User Created..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),LoginUI.class));
                }
                else{
                    Toast.makeText(RegisterUI.this, "Error!!!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
        
        signIn_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginUI.class)));
    }

    private void signIn() {
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
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth mAuth = null;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterUI.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterUI.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(RegisterUI.this,DashboardUI.class);
        startActivity(intent);
    }

}