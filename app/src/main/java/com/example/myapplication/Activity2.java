package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        String str = intent.getStringExtra("name");
        String str2 = intent.getStringExtra("age");
        String str3 = intent.getStringExtra("gender");

        TextView msg = findViewById(R.id.textView);
        TextView age = findViewById(R.id.textView2);
        TextView sex = findViewById(R.id.textView3);

        msg.setText("Hello, " + str + "!");
        age.setText("Age: " + str2);
        sex.setText("Gender: " + str3);
    }
}