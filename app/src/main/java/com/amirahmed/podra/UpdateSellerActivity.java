package com.amirahmed.podra;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class UpdateSellerActivity extends Activity{

    EditText disnameedittext,emailedittext,cardsnumberedittext,passwordedittext;

    Button updateButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateseller);


        disnameedittext = findViewById(R.id.disnameedittext);
        emailedittext = findViewById(R.id.emailedittext);
        cardsnumberedittext = findViewById(R.id.cardsnumberedittext);
        passwordedittext = findViewById(R.id.passwordedittext);


        updateButton = findViewById(R.id.updatebutton);


        Bundle bundle = getIntent().getExtras();

        disnameedittext.setText(bundle.getString("disName"));
        emailedittext.setText(bundle.getString("emailAddress"));
        cardsnumberedittext.setText(bundle.getString("cardsNumber"));
        passwordedittext.setText(bundle.getString("password"));

        final String ID = bundle.getString("ID");




        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isOnline())
                {
                    if(isEmpty(disnameedittext,emailedittext,cardsnumberedittext,passwordedittext))
                    {
                        showMessage("احد الحقول فارغة");
                    }else
                    {
                        String url;
                        String email,password,cardnumber,disname;
                        disname = disnameedittext.getText().toString().trim();
                        email = emailedittext.getText().toString().trim();
                        password = passwordedittext.getText().toString().trim();
                        cardnumber = cardsnumberedittext.getText().toString().trim();

                        url = "http://podra.compu-base.com/newwebservice.asmx/update_saler?name="+disname+"&email="+email+"&password="+password+"&numberscard="+cardnumber+"&id="+ID+"&full_blance="+cardnumber;

                        //progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //progressDialog.dismiss();

                                        if (response.equals("True")) {
                                            showMessage("تم التحديث بنجاح");
                                        } else {

                                            showMessage("هناك خطأ ما حاول مره اخرى");
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                        //progressDialog.dismiss();

                                    }
                                }
                        );


                        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                    }


                }else
                {
                    showMessage("لا يوجد اتصال بالشبكة");
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

    private boolean isEmpty(EditText disnameedittext,EditText emailedittext,EditText cardsnumberedittext,EditText passwordedittext) {
        return !(disnameedittext.getText().toString().trim().length() > 0 && emailedittext.getText().toString().trim().length() > 0 && cardsnumberedittext.getText().toString().trim().length() > 0 && passwordedittext.getText().toString().trim().length() > 0);

    }


}
