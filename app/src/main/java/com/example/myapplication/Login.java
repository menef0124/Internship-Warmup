package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class Login extends AppCompatActivity {

    private EditText user, pass;
    private Button login;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user       = findViewById(R.id.username);
        pass       = findViewById(R.id.password);
        login      = findViewById(R.id.loginBtn);
        loginError = findViewById(R.id.loginErrorMessage);

        pass.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //Hide Virtual Keyboard on Enter keystroke
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    sendLoginRequest("http://wfm.auto1stop.com/api/login.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());

                    return true;
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendLoginRequest("http://wfm.auto1stop.com/api/login.php?username=" + user.getText().toString() + "&password=" + pass.getText().toString());;
            }
        });
    }

    public void sendLoginRequest(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response.matches("SUCCESS")) {

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

                } else {

                    user.setText("");
                    pass.setText("");

                    user.clearFocus();
                    pass.clearFocus();

                    loginError.setText(getResources().getString(R.string.loginErrorMessage));
                    loginError.setVisibility(View.VISIBLE);

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