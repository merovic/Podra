package com.amirahmed.podra;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class TestActivity extends AppCompatActivity{

    TextView log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        log = (TextView) findViewById(R.id.log);


        String url;
        String name,email,password;

        name = "DDG";
        password = "qwerty";
        email = "email@yahoo.com";


        url = "http://thegreatkiko2090.000webhostapp.com/mobile/user_register.php?name=macbook pro&password=late 2011&email=amir@apple.com";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       log.setText(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );


        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }
}
