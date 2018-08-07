package com.amirahmed.podra;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends AppCompatActivity{

    EditText usernameedittext,passwordedittext;
    Button loginbutton;

    TinyDB tinyDB;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameedittext = (EditText) findViewById(R.id.usernameedittext);
        passwordedittext = (EditText) findViewById(R.id.passwordedittext);
        loginbutton = (Button) findViewById(R.id.loginbutton);

        tinyDB = new TinyDB(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isOnline()) {
                    if (isEmpty(usernameedittext, passwordedittext)) {
                        showMessage("احد الحقول فارغة");

                    } else {
                        String type;
                        String url;
                        String username;
                        username = usernameedittext.getText().toString().trim();
                        final String password = passwordedittext.getText().toString().trim();

                        type = tinyDB.getString("Type");

                        switch (type) {
                            case "Manger":

                                url = "http://podra.compu-base.com/newwebservice.asmx/login_admin?username=" + username + "&pass=" + password;

                                progressDialog.show();

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                progressDialog.dismiss();

                                                if (response.equals("False")) {
                                                    showMessage("هناك خطأ فى اسم المستخدم او كلمة المرور");
                                                } else {
                                                    tinyDB.putString("sellerLogedIn", "True");

                                                    String ID = response.substring( 0, response.indexOf(","));
                                                    String Keyword = response.substring( 1, response.indexOf(","));

                                                    tinyDB.putString("ID",ID);
                                                    tinyDB.putString("Keyword",Keyword);

                                                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();

                                            }
                                        }
                                );


                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



                                break;
                            case "seller": {

                                url = "http://podra.compu-base.com/newwebservice.asmx/login_saler?email=" + username + "&pass=" + password;

                                progressDialog.show();

                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                progressDialog.dismiss();

                                                if (response.equals("False")) {
                                                    showMessage("هناك خطأ فى اسم المستخدم او كلمة المرور");
                                                } else {
                                                    tinyDB.putString("sellerLogedIn", "True");

                                                    tinyDB.putString("sellerLogedIn", "True");

                                                    String ID = response.substring( 0, response.indexOf(","));
                                                    String Keyword = response.substring( 1, response.indexOf(","));

                                                    tinyDB.putString("ID",ID);
                                                    tinyDB.putString("Keyword",Keyword);

                                                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();

                                            }
                                        }
                                );


                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);


                                break;
                            }
                            default: {

                                url = "http://podra.compu-base.com/newwebservice.asmx/login_dis?email=" + username + "&pass=" + password;

                                progressDialog.show();

                                StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                progressDialog.dismiss();

                                                if (response.equals("False")) {
                                                    showMessage("هناك خطأ فى اسم المستخدم او كلمة المرور");
                                                } else {
                                                    tinyDB.putString("disLogedIn", "True");

                                                    tinyDB.putString("sellerLogedIn", "True");

                                                    String ID = response.substring( 0, response.indexOf(","));
                                                    String Keyword = response.substring( 1, response.indexOf(","));

                                                    tinyDB.putString("ID",ID);
                                                    tinyDB.putString("Keyword",Keyword);

                                                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();

                                            }
                                        }
                                );


                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);

                                break;
                            }
                        }


                    }
                } else {
                    showMessage("لا يوجد اتصال يالشبكة");
                }

            }
        });


    }


    // created automatically
    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isEmpty(EditText usernameEdit,EditText passwordEdit) {
        return !(usernameEdit.getText().toString().trim().length() > 0 && passwordEdit.getText().toString().trim().length() > 0);

    }

}
