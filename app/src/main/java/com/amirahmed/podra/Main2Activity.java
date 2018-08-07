package com.amirahmed.podra;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Main2Activity extends Activity implements PermissionCallback, ErrorCallback {

    TextView cardsnumbertext;

    TinyDB tinyDB;

    String type,ID,Keyword;

    String url,url2,url4;

    LinearLayout lin1,lin2,lin3,lin4,lin5;
    ImageView img1,img2,img3,img4,img5;
    TextView txt1,txt2,txt3,txt4,txt5;

    TextView logout;

    EditText nameedittext,addressedittext,disnameedittext,phoneedittext,emailedittext,websiteedittext,cardsnumberedittext,cityedittext,passwordedittext;
    Button addbutton;

    EditText nameedittext2,phoneedittext2,cardsnumberedittext2;
    Button addbutton2;

    double latitude;
    double longitude;

    GPSTracker gps;

    private ProgressDialog progressDialog;

    LinearLayout addlayout,addlayout2,map1layout;

    List<ListItem> distributors;
    private RecyclerView rvdistributors;

    List<ListItem> users;
    private RecyclerView rvusers;

    String GET_JSON_DATA_HTTP_URL_th,GET_JSON_DATA_HTTP_URL_tt;

    ListAdapter adapter,adapter2;

    RequestQueue requestQueue;

    private GoogleMap mMap;

    ImageView imageView,camera;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;

    //private final int PICK_IMAGE_REQUEST = 71;
    private static final int REQUEST_CAPTURE_IMAGE = 1888;

    String imageurl = "";

    String urlmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tinyDB = new TinyDB(getApplicationContext());

        ID = tinyDB.getString("ID");
        type = tinyDB.getString("Type");
        Keyword = tinyDB.getString("Keyword");

        reqPermission();

        lin1 = findViewById(R.id.addbuttonlayout);
        lin2 = findViewById(R.id.addbuttonlayout2);
        lin3 = findViewById(R.id.showbuttonlayout);
        lin4 = findViewById(R.id.usersbuttonlayout);
        lin5 = findViewById(R.id.mapbuttonlayout);

        img1 = findViewById(R.id.addimage);
        img2 = findViewById(R.id.addimage2);
        img3 = findViewById(R.id.showicon);
        img4 = findViewById(R.id.usersicon);
        img5 = findViewById(R.id.mapicon);

        txt1 = findViewById(R.id.addtext);
        txt2 = findViewById(R.id.addtext2);
        txt3 = findViewById(R.id.showtext);
        txt4 = findViewById(R.id.userstext);
        txt5 = findViewById(R.id.maptext);

        cardsnumbertext = findViewById(R.id.cardnumberstext);

        addlayout = findViewById(R.id.addlayout);
        addlayout2 = findViewById(R.id.add2layout);
        map1layout = findViewById(R.id.mapfield);

        if(type.equals("seller"))
        {
            lin2.setVisibility(View.GONE);
            lin4.setVisibility(View.GONE);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");


        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("dis"))
                {
                    tinyDB.putString("disLogedIn", "False");

                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    startActivity(intent);
                }else if(type.equals("seller"))
                {
                    tinyDB.putString("sellerLogedIn", "False");

                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    startActivity(intent);
                }


            }
        });

        if(type.equals("dis"))
        {
            startService(new Intent(getApplicationContext(), LocationService.class));
        }

        nameedittext = findViewById(R.id.nameedittext);
        addressedittext = findViewById(R.id.addressedittext);
        disnameedittext = findViewById(R.id.disnameedittext);
        phoneedittext = findViewById(R.id.phoneedittext);
        emailedittext = findViewById(R.id.emailedittext);
        websiteedittext = findViewById(R.id.websiteedittext);
        cardsnumberedittext = findViewById(R.id.cardsnumberedittext);
        cityedittext = findViewById(R.id.cityedittext);
        passwordedittext = findViewById(R.id.passwordedittext);

        imageView = findViewById(R.id.imageview);
        camera = findViewById(R.id.camera);

        addbutton = findViewById(R.id.addbutton);

        nameedittext2 = findViewById(R.id.nameedittext2);
        phoneedittext2 = findViewById(R.id.phoneedittext2);
        cardsnumberedittext2 = findViewById(R.id.cardsnumberedittext2);

        addbutton2 = findViewById(R.id.addbutton2);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getCardsNumber();

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAction1();
            }
        });

        addbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAction2();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImagePicker.pickImage(UpdateDisActivity.this, "Select your image:");
                chooseImage();
            }
        });


        //------------------------------------------------------------------------------------------------------------

        distributors = new ArrayList<>();

        rvdistributors = findViewById(R.id.list1);

        rvdistributors.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvdistributors.setLayoutManager(llm);

        JSON_DATA_WEB_CALL();

        //------------------------------------------------------------------------------------------------------------

        users = new ArrayList<>();

        rvusers = findViewById(R.id.list2);

        rvusers.setHasFixedSize(true);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        rvusers.setLayoutManager(llm2);

        JSON_DATA_WEB_CALL2();



        //------------------------------------------------------------------------------------------------------------


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapD);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;




                if(type.equals("seller"))
                {

                    urlmap = "http://podra.compu-base.com/newwebservice.asmx/get_dis?id_sales="+ID;

                }else if(type.equals("dis"))
                {

                    urlmap = "http://podra.compu-base.com/newwebservice.asmx/get_dis_by_id_dis?id_sales="+ID;

                }


                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlmap,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray js = new JSONArray(response);

                                    for(int i = 0;i<js.length();i++)
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
                                            latitude = 21.485811;
                                            longitude = 39.192505;
                                        }




                                        // Add a marker in Sydney and move the camera
                               //-----------         //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(childJSONObject.getString("namecilent")));
                                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                               //-----------         //mMap.getUiSettings().setScrollGesturesEnabled(true);

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
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_white_24dp));

                txt1.setTextColor(Color.parseColor("#9C0B03"));
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.VISIBLE);
                addlayout2.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvusers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);

                camera.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);


            }
        });

        lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_red_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.parseColor("#9C0B03"));
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                addlayout2.setVisibility(View.VISIBLE);
                rvdistributors.setVisibility(View.GONE);
                rvusers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);

                camera.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);



            }
        });

        lin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_red_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.parseColor("#9C0B03"));
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                addlayout2.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.VISIBLE);
                rvusers.setVisibility(View.GONE);
                map1layout.setVisibility(View.GONE);

                camera.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);

                tinyDB.putString("That","dis");

            }
        });

        lin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_red_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_white_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.parseColor("#9C0B03"));
                txt5.setTextColor(Color.WHITE);

                addlayout.setVisibility(View.GONE);
                addlayout2.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvusers.setVisibility(View.VISIBLE);
                map1layout.setVisibility(View.GONE);

                camera.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);

                tinyDB.putString("That","user");

            }
        });

        lin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
                img3.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img4.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white_24dp));
                img5.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_red_24dp));

                txt1.setTextColor(Color.WHITE);
                txt2.setTextColor(Color.WHITE);
                txt3.setTextColor(Color.WHITE);
                txt4.setTextColor(Color.WHITE);
                txt5.setTextColor(Color.parseColor("#9C0B03"));

                addlayout.setVisibility(View.GONE);
                addlayout2.setVisibility(View.GONE);
                rvdistributors.setVisibility(View.GONE);
                rvusers.setVisibility(View.GONE);
                map1layout.setVisibility(View.VISIBLE);

                camera.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);

            }
        });


    }

    public void JSON_DATA_WEB_CALL(){

        if(type.equals("seller"))
        {


            GET_JSON_DATA_HTTP_URL_th = "http://podra.compu-base.com/newwebservice.asmx/get_dis?id_sales="+ID;

        }else if(type.equals("dis"))
        {

            GET_JSON_DATA_HTTP_URL_th = "http://podra.compu-base.com/newwebservice.asmx/get_dis_by_id_dis?id_sales="+ID;

        }



        StringRequest stringRequest = new StringRequest(Request.Method.GET,GET_JSON_DATA_HTTP_URL_th,

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

                        showMessage(error.getMessage());
                        showMessage(type);


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

    //------------------------------------------------------------------------------------------------------------


    public void JSON_DATA_WEB_CALL2(){



        GET_JSON_DATA_HTTP_URL_tt = "http://podra.compu-base.com/newwebservice.asmx/get_users?id_dis="+ID;


        StringRequest stringRequest = new StringRequest(Request.Method.GET,GET_JSON_DATA_HTTP_URL_tt,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL2(response);

                        //showMessage("Successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //showMessage("No Connection");


                    }
                }
        );

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL2(String Jobj){

        try {
            JSONArray js = new JSONArray(Jobj);

            for(int i = 0; i<js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                ListItem GetDataAdapter2 = new ListItem();

                GetDataAdapter2.setDisName(childJSONObject.getString("name"));
                GetDataAdapter2.setCardsNumber(childJSONObject.getString("number_card"));
                GetDataAdapter2.setPhoneNumber(childJSONObject.getString("phone"));
                GetDataAdapter2.setID(childJSONObject.getString("Id"));

                users.add(GetDataAdapter2);
            }

            adapter2 = new ListAdapter(users,this);
            rvusers.setAdapter(adapter2);

            adapter2.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //------------------------------------------------------------------------------------------------------------




    private void addAction2() {


        if(isOnline())
        {
            if(isEmpty2(nameedittext2,phoneedittext2,cardsnumberedittext2))
            {
                showMessage("احد الحقول فارغة");

            }else
                {
                    String name2,phone2,cards2;
                    name2 = nameedittext2.getText().toString().trim();
                    phone2 = nameedittext2.getText().toString().trim();
                    cards2 = nameedittext2.getText().toString().trim();

                    url4 = "http://podra.compu-base.com/newwebservice.asmx/insert_user?name="+name2+"&phone="+phone2+"&number_card="+cards2+"&id_dis="+ID+"&keyword="+Keyword;

                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    progressDialog.dismiss();

                                    if (response.equals("False")) {
                                        showMessage("هناك خطأ فى اسم المستخدم او كلمة المرور");
                                    } else {
                                        tinyDB.putString("sellerLogedIn", "True");

                                        tinyDB.putString("ID",response);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                }

        }else
            {
                showMessage("لا يوجد اتصال يالشبكة");
            }



    }

    private void addAction1() {



        if(isOnline())
        {
            if(isEmpty(nameedittext,addressedittext,disnameedittext,phoneedittext,emailedittext,websiteedittext,cardsnumberedittext,cityedittext,passwordedittext) || imageurl.equals(""))
            {
                showMessage("احد الحقول فارغة");
            }else
            {

                if(Integer.parseInt(cardsnumberedittext.getText().toString())<=Integer.parseInt(cardsnumbertext.getText().toString()))
                {



                    final String name,email,password,phone,cardnumber,address,disname,website,city;
                    name = nameedittext.getText().toString().trim();
                    address = addressedittext.getText().toString().trim();
                    disname = disnameedittext.getText().toString().trim();
                    website = websiteedittext.getText().toString().trim();
                    city = cityedittext.getText().toString().trim();
                    email = emailedittext.getText().toString().trim();
                    password = passwordedittext.getText().toString().trim();
                    phone = phoneedittext.getText().toString().trim();
                    cardnumber = cardsnumberedittext.getText().toString().trim();

                    //url = "http://podra.compu-base.com/wb_sales.asmx/update_dis?name_com="+name+"&address="+address+"&name_dis="+disname+"&phone="+phone+"&email="+email+"&website="+website+"&numberscard="+cardnumber+"&city="+city+"&password="+password+"&id="+ID+"&image="+imageurl+"&full_blance="+cardnumber;

                    if(type.equals("seller"))
                    {

                        url2 =  "http://podra.compu-base.com/newwebservice.asmx/insert_dis?name_com="+name+"&address="+address+"&name_dis="+disname+"&phone="+phone+"&email="+email+"&website="+website+"&numberscard="+cardnumber+"&id_sales="+ID+"&city="+city+"&password="+password+"&lau_map="+latitude+"&long_map="+longitude+"&id_dis="+""+"&image="+imageurl+"&full_blance="+cardnumber+"&keyword="+Keyword;

                    }else if(type.equals("dis"))
                    {
                        url2 =  "http://podra.compu-base.com/newwebservice.asmx/insert_dis?name_com="+name+"&address="+address+"&name_dis="+disname+"&phone="+phone+"&email="+email+"&website="+website+"&numberscard="+cardnumber+"&id_sales="+""+"&city="+city+"&password="+password+"&lau_map="+latitude+"&long_map="+longitude+"&id_dis="+ID+"&image="+imageurl+"&full_blance="+cardnumber+"&keyword="+Keyword;
                    }


                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    progressDialog.dismiss();

                                    String url3;
                                    if (response.equals("True")) {
                                        showMessage("تم التسجيل بنجاح");

                                        if(type.equals("seller"))
                                        {

                                            url3 = "http://podra.compu-base.com/newwebservice.asmx/update_balance_sale?id="+ID+"&newbalance="+String.valueOf(Integer.parseInt(cardsnumbertext.getText().toString()) - Integer.parseInt(cardsnumberedittext.getText().toString()));


                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url3,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            progressDialog.dismiss();

                                                            if (response.equals("True")) {
                                                                showMessage("تم تحديث عدد الكروت بنجاح");



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







                                        }else if(type.equals("dis"))
                                        {

                                            url3 = "http://podra.compu-base.com/newwebservice.asmx/update_balance_dis?id="+ID+"&newbalance="+String.valueOf(Integer.parseInt(cardsnumbertext.getText().toString()) - Integer.parseInt(cardsnumberedittext.getText().toString()));


                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url3,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            progressDialog.dismiss();

                                                            if (response.equals("True")) {
                                                                showMessage("تم تحديث عدد الكروت بنجاح");



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


                }else
                    {
                        showMessage("لا تملك رصيد كافى من الكروت");
                    }

            }


        }else
        {
            showMessage("لا يوجد اتصال بالشبكة");
        }

    }




    public void getCardsNumber()
    {

        if(type.equals("dis"))
        {

            url = "http://podra.compu-base.com/newwebservice.asmx/get_balance_dis?id="+ID;

        }else if(type.equals("seller"))
        {

            url = "http://podra.compu-base.com/newwebservice.asmx/get_balance_saler?id="+ID;

        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        cardsnumbertext.setText(response);

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


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Main2Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri(); //getDownloadUrl not found
                            assert downloadUrl != null;
                            imageurl = downloadUrl.toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Main2Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }



    private void chooseImage() {

        //From Camera

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

                uploadImage();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
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

    private boolean isEmpty(EditText nameedittext,EditText addressedittext,EditText disnameedittext,EditText phoneedittext,EditText emailedittext,EditText websiteedittext,EditText cardsnumberedittext,EditText cityedittext,EditText passwordedittext) {
        return !(nameedittext.getText().toString().trim().length() > 0 && addressedittext.getText().toString().trim().length() > 0 && disnameedittext.getText().toString().trim().length() > 0 && phoneedittext.getText().toString().trim().length() > 0 && emailedittext.getText().toString().trim().length() > 0  && websiteedittext.getText().toString().trim().length() > 0
                && cardsnumberedittext.getText().toString().trim().length() > 0 && cityedittext.getText().toString().trim().length() > 0 && passwordedittext.getText().toString().trim().length() > 0);

    }

    private boolean isEmpty2(EditText nameedittext2,EditText phoneedittext2,EditText cardsnumberedittext2)
    {
        return !(nameedittext2.getText().toString().trim().length() > 0 && phoneedittext2.getText().toString().trim().length() > 0 && cardsnumberedittext2.getText().toString().trim().length() > 0);

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
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

                if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main2Activity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);

                } else {
                    //Toast.makeText(getApplicationContext(), "You need have granted GPS permission", Toast.LENGTH_SHORT).show();


                    gps = new GPSTracker(getApplicationContext(), Main2Activity.this);

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

    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions("android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION")
                .setCallback(this)
                .setErrorCallback(this)
                .request(20);
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
