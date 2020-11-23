package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText et;
    private Switch sw1;
    private Switch sw2;
    private Spinner spin;
    private String[] ages = new String[20];
    private int j = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for(int i = 0; i < ages.length; i++){
            ages[i] = Integer.toString(j);
            ++j;
        }

        et = findViewById(R.id.nameTF);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        spin = findViewById(R.id.spinner);

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // Hide Virtual Keyboard After User Clicks "Enter"
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    nextActivity(et);

                    return true;
                }
                return false;
            }
        });

        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ages);
        spin.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id){
        Toast.makeText(arg0.getContext(), ages[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0){

    }

    public void switchError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Make sure only one switch is flipped.");
        builder.setTitle("Error!");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    public void nextActivity(View v){
            if((sw1.isChecked() && sw2.isChecked()) || (!sw1.isChecked() && !sw2.isChecked())) {
                switchError();
            }
            else {

                Intent intent = new Intent(this, Activity2.class);
                String message = et.getText().toString();
                String gender;
                String age = spin.getSelectedItem().toString();
                if(sw1.isChecked())
                    gender = "Male";
                else
                    gender = "Female";

                intent.putExtra("name", message);
                intent.putExtra("gender", gender);
                intent.putExtra("age", age);
                startActivity(intent);
            }
    }

}