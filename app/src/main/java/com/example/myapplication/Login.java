package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    private EditText user, pass;
    private Button login;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(android.R.id.content).setFocusableInTouchMode(true);

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        loginError = findViewById(R.id.loginErrorMessage);

        pass.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    //Hide Virtual Keyboard on Enter keystroke
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //sendLoginRequest("http://wfm.auto1stop.com/api/login.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());
                    sendLoginRequest("http://10.0.2.2/hello_get.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());

                    return true;
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendLoginRequest("http://wfm.auto1stop.com/api/login.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());
                sendLoginRequest("http://10.0.2.2/hello_get.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());
            }
        });
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

    public void sendLoginRequest(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response.matches("FAILURE")) {

                    user.setText("");
                    pass.setText("");

                    user.clearFocus();
                    pass.clearFocus();

                    loginError.setText(getResources().getString(R.string.loginErrorMessage));
                    loginError.setVisibility(View.VISIBLE);

                } else {

                    Intent goToHome = new Intent(Login.this, MainActivity.class);
                    goToHome.putExtra("username", user.getText().toString());
                    goToHome.putExtra("password", pass.getText().toString());

                    user.setText("");
                    pass.setText("");

                    user.clearFocus();
                    pass.clearFocus();

                    loginError.setVisibility(View.INVISIBLE);
                    loginError.setText("");

                    startActivity(goToHome);

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error+"", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}