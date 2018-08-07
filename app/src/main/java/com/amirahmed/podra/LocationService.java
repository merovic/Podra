package com.amirahmed.podra;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    double latitude;
    double longitude;

    GPSTracker gps;

    TimerTask hourTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         Timer timer = new Timer();
                hourTask = new TimerTask() {
                    @Override
                    public void run() {

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                gps = new GPSTracker(getApplicationContext(),LocationService.this);

                                if (gps.canGetLocation()) {

                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();

                                    TinyDB tinyDB;
                                    tinyDB = new TinyDB(getApplicationContext());

                                    String ID = tinyDB.getString("ID");


                                    String url = "http://podra.compu-base.com/newwebservice.asmx/update_map_saler?id="+ID+"&lau_map="+latitude+"&long_map="+longitude;



                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    //showMessage("Done");

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


                                    //showMessage("location is "+latitude+" and "+longitude);

                                }else
                                    {
                                        //showMessage("cant get location");
                                    }


                            }
                        });


                    }
                };
                timer.schedule(hourTask, 0L, 10000 * 10);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        hourTask.cancel();
        super.onDestroy();
    }

    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

}
