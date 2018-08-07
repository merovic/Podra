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

import static com.amirahmed.podra.R.id.cardsnumberedittext;


public class UpdateUserActivity extends Activity{

    EditText nameedittext,phoneedittext,cardsnumberseditetext;
    Button updateButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuser);

        nameedittext = findViewById(R.id.nameedittext);
        phoneedittext = findViewById(R.id.phoneedittext);
        cardsnumberseditetext = findViewById(cardsnumberedittext);

        updateButton = findViewById(R.id.updatebutton);

        Bundle bundle = getIntent().getExtras();

        nameedittext.setText(bundle.getString("disName"));
        phoneedittext.setText(bundle.getString("phoneNumber"));
        cardsnumberseditetext.setText(bundle.getString("cardsNumber"));

        final String ID = bundle.getString("ID");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isOnline())
                {
                    if(isEmpty(nameedittext,phoneedittext,cardsnumberseditetext))
                    {
                        showMessage("احد الحقول فارغة");
                    }else
                    {
                        String url;
                        String phone,cardnumber,disname;
                        disname = nameedittext.getText().toString().trim();
                        phone = phoneedittext.getText().toString().trim();
                        cardnumber = cardsnumberseditetext.getText().toString().trim();

                        url = "http://podra.compu-base.com/newwebservice.asmx/update_user?name="+disname+"&phone="+phone+"&number_card="+cardnumber+"&id="+ID;

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

    private boolean isEmpty(EditText disnameedittext,EditText phoneedittext,EditText cardsnumberedittext) {
        return !(disnameedittext.getText().toString().trim().length() > 0 && phoneedittext.getText().toString().trim().length() > 0 && cardsnumberedittext.getText().toString().trim().length() > 0);

    }

}
