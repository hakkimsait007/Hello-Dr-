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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class LoginUI extends AppCompatActivity {
    EditText email,password;
    Button Login;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    TextView signUp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        email = findViewById(R.id.email_editText);
        password = findViewById(R.id.pass_editText);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        Login = findViewById(R.id.login_button);
        signUp_btn = findViewById(R.id.signup_textView);

        Login.setOnClickListener(v -> {
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

            //authenticate the user
            fAuth.signInWithEmailAndPassword(emailUser,passUser).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginUI.this, "Logged In Successfully..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginUI.this,DashboardUI.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginUI.this, "Error!!!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });


        signUp_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),RegisterUI.class)));

    }
}
