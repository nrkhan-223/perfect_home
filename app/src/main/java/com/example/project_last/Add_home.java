package com.example.project_last;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Add_home extends AppCompatActivity {
    EditText addhome_popertyname, addhome_address, addhome_phone, addhome_details, addhome_location, addhome_price;
    Button addimg_button, addhome_button,add_video;
    Spinner Spinner;
    private ProgressDialog progressDialog;

    String[] category;

    private Uri imageUri = null;
    private Uri videoUri = null;


    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String myId;

    private static final int VIDEO_GALLERY_CODE1 = 301;
    private static final int GALLERY_CODE1 = 300;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    StorageTask storageTask;

    String address, phone, location, price, category1, details, property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");

        add_video = findViewById(R.id.addvid_button);
        addhome_popertyname = findViewById(R.id.addhome_popertyname);
        category = getResources().getStringArray(R.array.category);
        addhome_address = findViewById(R.id.addhome_address);
        addhome_phone = findViewById(R.id.addhome_phone);
        addhome_location = findViewById(R.id.addhome_location);
        addhome_price = findViewById(R.id.addhome_price);
        addhome_details = findViewById(R.id.addhome_details);
        addimg_button = findViewById(R.id.addimg_button);
        addimg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addhome_button = findViewById(R.id.addhome_button);
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery2();
            }
        });


        Spinner = findViewById(R.id.Spinnerid);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Add_home.this, R.layout.spinner_item, R.id.spinner_ID, category);
        Spinner.setAdapter(arrayAdapter);


        addhome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                myId = firebaseUser.getUid();
                property = addhome_popertyname.getText().toString();
                address = addhome_address.getText().toString();
                phone = addhome_phone.getText().toString();
                details = addhome_details.getText().toString();
                location = addhome_location.getText().toString();
                price = addhome_price.getText().toString();
                category1 = Spinner.getSelectedItem().toString();

                newSentData(property, address, phone, location, price, category1, myId,details);

            }
        });

    }

    private void openGallery2() {

        Intent intent = new Intent();
        intent.setType(("video/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, VIDEO_GALLERY_CODE1);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private void openGallery() {

        Intent intent=new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_CODE1);

    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.image_view_item, null, false);
            builder.setView(view);
            AlertDialog alertDialog = builder.show();
            alertDialog.setCancelable(false);
            ImageView imageView = view.findViewById(R.id.viewImage_ID);
            Button sentButton = view.findViewById(R.id.sentImage);
            if (requestCode == GALLERY_CODE1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Glide.with(view).load(imageUri).into(imageView);
            }

            sentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        } else {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            View view2 = LayoutInflater.from(this).inflate(R.layout.video_view_item, null, false);
            builder2.setView(view2);
            AlertDialog alertDialog2 = builder2.show();
            alertDialog2.setCancelable(false);
            alertDialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            VideoView videoView = view2.findViewById(R.id.video);
            Button button = view2.findViewById(R.id.send_video);

            if (requestCode == VIDEO_GALLERY_CODE1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                videoUri = data.getData();
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                videoView.start();


            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog2.dismiss();
                }
            });
        }
    }

    private void newSentData(String property, String address, String phone, String location, String price, String category1, String myId,String details) {

        if (imageUri == null) {
//            reference = FirebaseDatabase.getInstance().getReference();
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("property", property);
//            hashMap.put("address", address);
//            hashMap.put("phone", phone);
//            hashMap.put("location", location);
//            hashMap.put("price", price);
//            hashMap.put("id", myId);
//            hashMap.put("imageUrl", "imageUrl");
//            hashMap.put("category", category1);
//            reference.child("Room_details").push().setValue(hashMap);
            Toast.makeText(Add_home.this, "Add Image", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference("Room_details");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String ImageList = uri.toString();
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Room_details");
                            String new_id=reference2.push().getKey();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("details",details);
                            hashMap.put("property", property);
                            hashMap.put("address", address);
                            hashMap.put("phone", phone);
                            hashMap.put("location", location);
                            hashMap.put("price", price);
                            hashMap.put("id", myId);
                            hashMap.put("new_id",new_id);
                            hashMap.put("imageUrl", ImageList);
                            hashMap.put("category", category1);
                            reference2.child(new_id).setValue(hashMap);
                            //reference2.updateChildren(hashMap);
                            progressDialog.dismiss();
                            Toast.makeText(Add_home.this, "submit", Toast.LENGTH_SHORT).show();

                            addhome_popertyname.setText("");
                            addhome_address.setText("");
                            addhome_details.setText("");
                            addhome_location.setText("");
                            addhome_phone.setText("");
                            addhome_price.setText("");


                            addhome_popertyname.setFocusable(false);
                            addhome_address.setFocusable(false);
                            addhome_details.setFocusable(false);
                            addhome_location.setFocusable(false);
                            addhome_phone.setFocusable(false);
                            addhome_price.setFocusable(false);





                        }
                    });

                }
            });


        }


    }


}