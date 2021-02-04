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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button logout;
    private EditText et;
    private Switch sw1;
    private Switch sw2;
    private Spinner spin;
    private String[] ages = new String[20];
    private Intent intent;
    private int j = 18;
    private static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        findViewById(android.R.id.content).setFocusableInTouchMode(true);

        ages[0] = "Select Your Age";
        for(int i = 1; i < ages.length; i++){
            ages[i] = Integer.toString(j);
            ++j;
        }

        logout = findViewById(R.id.button4);
        et = findViewById(R.id.nameTF);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        spin = findViewById(R.id.spinner);

        intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        try {
            downloadJSON("http://10.0.2.2/androidLogin_get.php?username=" + username + "&password=" + password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2/androidSave.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("FAILED"))
                                    Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Info Saved!", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error + "", Toast.LENGTH_SHORT).show();
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", id);
                        params.put("firstName", et.getText().toString());
                        params.put("age", spin.getSelectedItem().toString());
                        if(sw1.isChecked())
                            params.put("sex", "Male");
                        else
                            params.put("sex", "Female");
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
/*
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

 */
            }
    }

    private void downloadJSON(final String urlWebService) throws JSONException{

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getApplicationContext(), "Gathering data...", Toast.LENGTH_SHORT).show();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("FAILURE") || s.equals("FAILED")){
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        id = obj.getString("id");
                        et.setText(obj.getString("firstName"));

                        if(obj.getString("sex").equals("M")){
                            sw1.setChecked(true);
                            sw2.setChecked(false);
                        }
                        else if(obj.getString("sex").equals("F")){
                            sw1.setChecked(false);
                            sw2.setChecked(true);
                        }
                        else{
                            sw1.setChecked(false);
                            sw2.setChecked(false);
                        }

                        for(int i = 0; i < ages.length; i++){
                            if(obj.getString("age").equals(ages[i])){
                                spin.setSelection(i);
                                break;
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
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