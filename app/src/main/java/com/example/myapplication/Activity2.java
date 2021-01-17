package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    private Intent intent;
    private Intent backIntent;
    private Button backBtn;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        backIntent = new Intent(this, MainActivity.class);
        intent = getIntent();
        //intent.setClass(this, MainActivity.class);
        String str = intent.getStringExtra("name");
        String str2 = intent.getStringExtra("age");
        String str3 = intent.getStringExtra("gender");

        backBtn = findViewById(R.id.button2);
        homeBtn = findViewById(R.id.button3);

        TextView msg = findViewById(R.id.textView);
        TextView age = findViewById(R.id.textView2);
        TextView sex = findViewById(R.id.textView3);

        msg.setText("Hello, " + str + "!");
        age.setText("Age: " + str2);
        sex.setText("Gender: " + str3);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //backIntent = new Intent(this, MainActivity.class);
                backIntent.putExtra("username", intent.getStringExtra("username"));
                backIntent.putExtra("password", intent.getStringExtra("password"));
                backIntent.putExtra("name", intent.getStringExtra("name"));
                backIntent.putExtra("gender", intent.getStringExtra("gender"));
                backIntent.putExtra("age", intent.getStringExtra("age"));

                startActivity(backIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        backIntent = new Intent(this, MainActivity.class);
        backIntent.putExtra("username", this.intent.getStringExtra("username"));
        backIntent.putExtra("password", this.intent.getStringExtra("password"));
        backIntent.putExtra("name", this.intent.getStringExtra("name"));
        backIntent.putExtra("gender", this.intent.getStringExtra("gender"));
        backIntent.putExtra("age", this.intent.getStringExtra("age"));

        startActivity(backIntent);
        finish();
    }

}