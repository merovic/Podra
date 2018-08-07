package com.amirahmed.podra;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity{

    Button button1,button2,button3;

    TinyDB tinyDB;

    String adminlogedin;
    String dislogedin;
    String sellerlogedin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        tinyDB = new TinyDB(getApplicationContext());

        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("Type","Manger");

                adminlogedin = tinyDB.getString("adminLogedIn");

                if(adminlogedin.equals("True"))
                {
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    startActivity(intent);
                }else
                    {
                        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                        startActivity(intent);
                    }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("Type","dis");

                dislogedin = tinyDB.getString("disLogedIn");

                if(dislogedin.equals("True"))
                {
                    Intent intent = new Intent(getApplicationContext() , Main2Activity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("Type","seller");

                sellerlogedin = tinyDB.getString("sellerLogedIn");

                if(sellerlogedin.equals("True"))
                {
                    Intent intent = new Intent(getApplicationContext() , Main2Activity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
