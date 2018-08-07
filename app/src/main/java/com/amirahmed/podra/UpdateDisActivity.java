package com.amirahmed.podra;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;

import java.io.IOException;
import java.util.UUID;

public class UpdateDisActivity extends Activity{

    EditText nameedittext,addressedittext,disnameedittext,phoneedittext,emailedittext,websiteedittext,cardsnumberedittext,cityedittext,passwordedittext;

    Button updateButton;

    ImageView imageView,camera;

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;

    //private final int PICK_IMAGE_REQUEST = 71;
    private static final int REQUEST_CAPTURE_IMAGE = 1888;

    String imageurl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedis);

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

        updateButton = findViewById(R.id.updatebutton);


        Bundle bundle = getIntent().getExtras();

        nameedittext.setText(bundle.getString("companyName"));
        addressedittext.setText(bundle.getString("address"));
        disnameedittext.setText(bundle.getString("disName"));
        phoneedittext.setText(bundle.getString("phoneNumber"));
        emailedittext.setText(bundle.getString("emailAddress"));
        websiteedittext.setText(bundle.getString("website"));
        cardsnumberedittext.setText(bundle.getString("cardsNumber"));
        cityedittext.setText(bundle.getString("city"));
        passwordedittext.setText(bundle.getString("password"));

        final String ID = bundle.getString("ID");


        ImagePicker.setMinQuality(600, 600);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImagePicker.pickImage(UpdateDisActivity.this, "Select your image:");
                chooseImage();
            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isOnline())
                {
                    if(isEmpty(nameedittext,addressedittext,disnameedittext,phoneedittext,emailedittext,websiteedittext,cardsnumberedittext,cityedittext,passwordedittext) || imageurl.equals(""))
                    {
                        showMessage("احد الحقول فارغة");
                    }else
                    {
                        String url;
                        String name,email,password,phone,cardnumber,address,disname,website,city;
                        name = nameedittext.getText().toString().trim();
                        address = addressedittext.getText().toString().trim();
                        disname = disnameedittext.getText().toString().trim();
                        website = websiteedittext.getText().toString().trim();
                        city = cityedittext.getText().toString().trim();
                        email = emailedittext.getText().toString().trim();
                        password = passwordedittext.getText().toString().trim();
                        phone = phoneedittext.getText().toString().trim();
                        cardnumber = cardsnumberedittext.getText().toString().trim();

                        url = "http://podra.compu-base.com/newwebservice.asmx/update_dis?name_com="+name+"&address="+address+"&name_dis="+disname+"&phone="+phone+"&email="+email+"&website="+website+"&numberscard="+cardnumber+"&city="+city+"&password="+password+"&id="+ID+"&image="+imageurl+"&full_blance="+cardnumber;

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

    private void chooseImage() {

        //From Gallery

        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


        //From Camera

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }


    }


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateDisActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri(); //getDownloadUrl not found
                            assert downloadUrl != null;
                            imageurl = downloadUrl.toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateDisActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


}
