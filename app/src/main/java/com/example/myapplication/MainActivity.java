package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public Button signIn_btn;
    public Button signUp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn_btn = findViewById(R.id.button_signin);
        signUp_btn = findViewById(R.id.button_signup);

        signIn_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginUI.class)));
        signUp_btn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),RegisterUI.class)));
    }
}
