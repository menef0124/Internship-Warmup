package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText et;
    private Switch sw1;
    private Switch sw2;
    private Spinner spin;
    private String[] ages = new String[20];
    private Intent intent;
    private int j = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(android.R.id.content).setFocusableInTouchMode(true);

        for(int i = 0; i < ages.length; i++){
            ages[i] = Integer.toString(j);
            ++j;
        }

        et = findViewById(R.id.nameTF);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        spin = findViewById(R.id.spinner);

        intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        try {
            downloadJSON("http://10.0.2.2/hello_get.php?username=" + username + "&password=" + password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(this);
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
            findViewById(android.R.id.content).clearFocus();
        }
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

                intent.putExtra("username", this.intent.getStringExtra("username"));
                intent.putExtra("password", this.intent.getStringExtra("password"));
                intent.putExtra("name", message);
                intent.putExtra("gender", gender);
                intent.putExtra("age", age);
                startActivity(intent);
            }
    }

    private void downloadJSON(final String urlWebService) throws JSONException{

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    et.setText(obj.getString("firstName"));

                    if(obj.getString("sex").equals("M")){
                        sw1.setChecked(true);
                        sw2.setChecked(false);
                    }
                    else{
                        sw1.setChecked(false);
                        sw2.setChecked(true);
                    }

                    for(int i = 0; i < ages.length; i++){
                        if(obj.getString("age").equals(ages[i])){
                            spin.setSelection(i);
                            break;
                        }
                    }

                    /*
                    String[] stocks = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        stocks[i] = obj.getString("name") + " " + obj.getString("price");
                    }
                     */
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

}