package com.amirahmed.podra;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {

    TinyDB tinydb;

    LinearLayout lin1,lin2,lin3,lin4,lin5;
    ImageView img1,img2,img3,img4,img5;
    TextView txt1,txt2,txt3,txt4,txt5;

    TextView logout;

    EditText nameedittext,emailedittext,passwordedittext,phoneedittext,cardnumberedittext;
    Button addbutton;

    double latitude;
    double longitude;

    GPSTracker gps;

    private ProgressDialog progressDialog;

    LinearLayout addlayout,map1layout,map2layout;

    List<ListItem> distributors;
    private RecyclerView rvdistributors;

    List<ListItem> sellers;
    private RecyclerView rvsellers;

    String GET_JSON_DATA_HTTP_URL;

    ListAdapter adapter,adapter2;

    RequestQueue requestQueue;

    private GoogleMap mMap,mMap2;

    String Keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinydb = new TinyDB(getApplicationContext());
        Keyword = tinydb.getString("Keyword");

        lin1 = (LinearLayout) findViewById(R.id.addbuttonlayout);
        lin2 = (LinearLayout) findViewById(R.id.dislistbuttonlayout);
        lin3 = (LinearLayout) findViewById(R.id.sellerlistbuttonlayout);
        lin4 = (LinearLayout) findViewById(R.id.sellermapbuttonlayout);
        lin5 = (LinearLayout) findViewById(R.id.dismapbuttonlayout);

        img1 = (ImageView) findViewById(R.id.addimage);
        img2 = (ImageView) findViewById(R.id.disicon);
        img3 = (ImageView) findViewById(R.id.sellericon);
        img4 = (ImageView) findViewById(R.id.sellermapicon);
        img5 = (ImageView) findViewById(R.id.dismapicon);

        txt1 = (TextView) findViewById(R.id.addtext);
        txt2 = (TextView) findViewById(R.id.distext);
        txt3 = (TextView) findViewById(R.id.sellertext);
        txt4 = (TextView) findViewById(R.id.sellermaptext);
        txt5 = (TextView) findViewById(R.id.dismaptext);

        addlayout = (LinearLayout) findViewById(R.id.addlayout);
        map1layout = (LinearLayout) findViewById(R.id.mapfield);
        map2layout = (LinearLayout) findViewById(R.id.mapfield2);


        nameedittext = (EditText) findViewById(R.id.nameedittext);
        emailedittext = (EditText) findViewById(R.id.emailedittext);
        passwordedittext = (EditText) findViewById(R.id.passwordedittext);
        phoneedittext = (EditText) findViewById(R.id.phoneedittext);
        cardnumberedittext = (EditText) findViewById(R.id.cardsnumberedittext);

        logout = (TextView) findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinydb.putString("adminLogedIn", "False");

                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                startActivity(intent);
            }
        });

        addbutton = (Button) findViewById(R.id.addbutton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOnline())
                {
                    if(isEmpty(nameedittext,emailedittext,passwordedittext,phoneedittext,cardnumberedittext))
                    {
                        showMessage("احد الحقول فارغة");
                    }else
                    {
                        String url;
                        String name,email,password,phone,cardnumber;
                        name = nameedittext.getText().toString().trim();
                        email = emailedittext.getText().toString().trim();
                        password = passwordedittext.getText().toString().trim();
                        phone = phoneedittext.getText().toString().trim();
                        cardnumber = cardnumberedittext.getText().toString().trim();

                        url = "http://podra.compu-base.com/newwebservice.asmx/insert_saler_for_admin?name="+name+"&email="+email+"&password="+password+"&phone="+phone+"&numberscard="+cardnumber+"&lau_map="+latitude+"&long_map="+longitude+"&full_blance="+cardnumber+"&keyword="+Keyword;

                        progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        progressDialog.dismiss();

                                        if (response.equals("True")) {
                                            showMessage("تم التسجيل بنجاح");
                                        } else {

                                            showMessage("هناك خطأ ما حاول مره اخرى");
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

                    }


                }else
                {
                    showMessage("لا يوجد اتصال بالشبكة");
                }
            }
        });

        //------------------------------------------------------------------------------------------------------------

        distributors = new ArrayList<>();

        rvdistributors = (RecyclerView) findViewById(R.id.list1);

        rvdistributors.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvdistributors.setLayoutManager(llm);

        JSON_DATA_WEB_CALL();

        //------------------------------------------------------------------------------------------------------------

        sellers = new ArrayList<>();

        rvsellers = (RecyclerView) findViewById(R.id.list2);

        rvsellers.setHasFixedSize(true);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        rvsellers.setLayoutManager(llm2);

        JSON_DATA_WEB_CALL_2();

        //------------------------------------------------------------------------------------------------------------


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapD);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                String urlmap = "http://podra.compu-base.com/newwebservice.asmx/get_all_saler_for_admin";


                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlmap,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray js = new JSONArray(response);

                                    for(int i =0;i<js.length();i++)
                                    {

                                        JSONObject childJSONObject = js.getJSONObject(i);
                                        String latitudeString = childJSONObject.getString("lau_map");
                                        String longitudeString = childJSONObject.getString("long_map");

                                        double latitude;
                                        double longitude;

                                        if(!Objects.equals(latitudeString, "") && !Objects.equals(longitudeString, ""))
                                        {
                                            latitude = Double.parseDouble(latitudeString);
                                            longitude = Double.parseDouble(longitudeString);
                                        }else
                                            {
                                                latitude = 31.3059780188196;
                                                longitude = 31.3059780188196;
                                            }




                                        // Add a marker in Sydney and move the camera
                         //-------------               mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(childJSONObject.getString("name")));
                                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        //--------------                mMap.getUiSettings().setScrollGesturesEnabled(true);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
        });


        //------------------------------------------------------------------------------------------------------------


        MapFragment mapFragment2 = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
        mapFragment2.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap2 = googleMap;

                String urlmap = "http://podra.compu-base.com/newwebservice.asmx/get_all_dis_for_admin";


                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlmap,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray js = new JSONArray(response);

                                    for(int i =0;i<js.length();i++)
                                    {

                                        JSONObject childJSONObject = js.getJSONObject(i);
                                        String latitudeString = childJSONObject.getString("lau_map");
                                        String longitudeString = childJSONObject.getString("long_map");

                                        double latitude;
                                        double longitude;

                                        if(!Objects.equals(latitudeString, "") && !Objects.equals(longitudeString, ""))
                                        {
                                            latitude = Double.parseDouble(latitudeString);
                                            longitude = Double.parseDouble(longitudeString);
                                        }else
                                        {
                                            latitude = 31.3059780188196;
                                            longitude = 31.3059780188196;
                                        }




                                        // Add a marker in Sydney and move the camera
                          //--------------              mMap2.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(childJSONObject.getString("nameco")));
                                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                          //--------------              mMap2.getUiSettings().setScrollGesturesEnabled(true);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
        });





        //------------------------------------------------------------------------------------------------------------

        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_red_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));

                txt1.setTextColor(Color.parseColor("#9C0B03"));
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.VISIBLE);
                rvdistributors.setVisibility(View.GONE);
                rvsellers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);
                map2layout.setVisibility(View.GONE);


            }
        });

        lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_red_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.parseColor("#9C0B03"));
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.VISIBLE);
                rvsellers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);
                map2layout.setVisibility(View.GONE);

                tinydb.putString("That","dis");


            }
        });

        lin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_red_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.parseColor("#9C0B03"));
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvsellers.setVisibility(View.VISIBLE);
                map1layout.setVisibility(View.GONE);
                map2layout.setVisibility(View.GONE);

                tinydb.putString("That","seller");

            }
        });

        lin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_red_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.parseColor("#9C0B03"));
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvsellers.setVisibility(View.GONE);
                map1layout.setVisibility(View.VISIBLE);
                map2layout.setVisibility(View.GONE);

            }
        });

        lin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_red_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.parseColor("#9C0B03"));

                addlayout.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvsellers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);
                map2layout.setVisibility(View.VISIBLE);

            }
        });


    }



    public void JSON_DATA_WEB_CALL(){

        GET_JSON_DATA_HTTP_URL = "http://podra.compu-base.com/newwebservice.asmx/get_all_dis_for_admin?";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                        //showMessage("Successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("No Connection");


                    }
                }
        );

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL(String Jobj){

        try {
            JSONArray js = new JSONArray(Jobj);

            for(int i = 0; i<js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                ListItem GetDataAdapter2 = new ListItem();

                GetDataAdapter2.setCompanyname(childJSONObject.getString("nameco"));
                GetDataAdapter2.setAddress(childJSONObject.getString("addressco"));
                GetDataAdapter2.setCardsNumber(childJSONObject.getString("numbers_card"));
                GetDataAdapter2.setDisName(childJSONObject.getString("namecilent"));
                GetDataAdapter2.setCity(childJSONObject.getString("city"));
                GetDataAdapter2.setPhoneNumber(childJSONObject.getString("phone"));
                GetDataAdapter2.setWebsite(childJSONObject.getString("website"));
                GetDataAdapter2.setEmail(childJSONObject.getString("email"));
                GetDataAdapter2.setPassword(childJSONObject.getString("password"));
                GetDataAdapter2.setTotal(childJSONObject.getString("full_blance"));
                GetDataAdapter2.setID(childJSONObject.getString("Id"));

                distributors.add(GetDataAdapter2);
            }

            adapter = new ListAdapter(distributors,this);
            rvdistributors.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //------------------------------------------------------------------------------------------------------



    public void JSON_DATA_WEB_CALL_2(){

        GET_JSON_DATA_HTTP_URL = "http://podra.compu-base.com/newwebservice.asmx/get_all_saler_for_admin?";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL_2(response);

                        //showMessage("Successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("No Connection");


                    }
                }
        );

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_2(String Jobj) {

        try {
            JSONArray js = new JSONArray(Jobj);

            for(int i = 0; i<js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                ListItem GetDataAdapter2 = new ListItem();


                GetDataAdapter2.setDisName(childJSONObject.getString("name"));
                GetDataAdapter2.setEmail(childJSONObject.getString("email"));
                GetDataAdapter2.setPassword(childJSONObject.getString("password"));
                GetDataAdapter2.setCardsNumber(childJSONObject.getString("numbers_card"));
                GetDataAdapter2.setTotal(childJSONObject.getString("full_blance"));
                GetDataAdapter2.setID(childJSONObject.getString("Id"));

                sellers.add(GetDataAdapter2);
            }

            adapter2 = new ListAdapter(sellers,this);
            rvsellers.setAdapter(adapter2);

            adapter2.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    private boolean isEmpty(EditText nameEdit,EditText emailedittext,EditText passwordEdit,EditText phoneedittext,EditText cardnumberedittext) {
        return !(nameEdit.getText().toString().trim().length() > 0 && emailedittext.getText().toString().trim().length() > 0 && passwordEdit.getText().toString().trim().length() > 0  && phoneedittext.getText().toString().trim().length() > 0  && cardnumberedittext.getText().toString().trim().length() > 0);

    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(isOnline())
        {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
                    //Toast.makeText(getApplicationContext(), "You need have granted GPS permission", Toast.LENGTH_SHORT).show();


                    gps = new GPSTracker(getApplicationContext(), MainActivity.this);

                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();


                    } else {
                        gps.showSettingsAlert();
                    }
                }

            }else{
                showGPSDisabledAlertToUser();
            }

        }else
        {
            showMessage("No Connection");
        }

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
