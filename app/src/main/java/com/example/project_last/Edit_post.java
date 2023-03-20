package com.example.project_last;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_last.Details_list.My_detail_list;
import com.example.project_last.Model.RoomList;
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
import java.util.List;
import java.util.Objects;

public class Edit_post extends AppCompatActivity {

    EditText nameEdit, addressEdit, contractEdit, descriptionEdit, locationEdit, priceEdit;
    ImageView imageView1;
    Spinner spinner;
    Button add_image, update;
    List<RoomList> roomLists;
    Intent intent;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String myId;
    private static final int GALLERY_CODE1 = 300;
    private Uri imageUri = null;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    StorageTask storageTask;


    String[] categorysp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        roomLists = new ArrayList<>();
        //String name, String address, String category, String contract, String description, String location, String price, String image,

        nameEdit = findViewById(R.id.edit_name);
        addressEdit = findViewById(R.id.edit_address);
        contractEdit = findViewById(R.id.edit_contract);
        spinner = findViewById(R.id.category_sp);
        categorysp = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Edit_post.this, R.layout.spinner_item, R.id.spinner_ID, categorysp);
        spinner.setAdapter(arrayAdapter);
        descriptionEdit = findViewById(R.id.edit_Description);
        locationEdit = findViewById(R.id.edit_location);
        priceEdit = findViewById(R.id.edit_price);
        imageView1 = findViewById(R.id.edit_imageview);
        add_image = findViewById(R.id.edit_image);
        update = findViewById(R.id.update);


        intent = getIntent();
        //String category = intent.getStringExtra("category");
        String contract = intent.getStringExtra("phone");
        String description = intent.getStringExtra("details");
        String location = intent.getStringExtra("location");
        String price = intent.getStringExtra("price");
        String address = intent.getStringExtra("address");
        String imageUrl = intent.getStringExtra("imageUrl");
        String name = intent.getStringExtra("property");
        String new_id = intent.getStringExtra("new_id");
        categorysp = getResources().getStringArray(R.array.category);

        Toast.makeText(this, "" + new_id, Toast.LENGTH_SHORT).show();

        nameEdit.setText(name);
        addressEdit.setText(address);
        contractEdit.setText(contract);
        descriptionEdit.setText(description);
        locationEdit.setText(location);
        priceEdit.setText(price);
        Glide.with(Edit_post.this).load(imageUrl).into(imageView1);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                myId = firebaseUser.getUid();
                String name = nameEdit.getText().toString();
                String address = addressEdit.getText().toString();
                String contract = contractEdit.getText().toString();
                String description = descriptionEdit.getText().toString();
                String location = locationEdit.getText().toString();
                String price = priceEdit.getText().toString();
                String category1 = spinner.getSelectedItem().toString();
                newSentData(name, address, contract, description, location, price, myId, category1, new_id);

            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

        private void openGallery() {
        Intent intent = new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_CODE1);
    }

    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.image_view_item, null, false);
        builder.setView(view);

        AlertDialog alertDialog = builder.show();
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
                Glide.with(view).load(imageUri).into(imageView1);
            }
        });

    }

    private void newSentData(String name, String address, String contract, String description, String location, String price, String myId, String category1, String new_id) {
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
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Room_details").child(new_id);


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("details", description);
                        hashMap.put("property", name);
                        hashMap.put("address", address);
                        hashMap.put("phone", contract);
                        hashMap.put("location", location);
                        hashMap.put("price", price);
                        hashMap.put("id", myId);
                        hashMap.put("new_id", new_id);
                        hashMap.put("imageUrl", ImageList);
                        hashMap.put("category", category1);

                        reference2.updateChildren(hashMap);
                        // progressDialog.dismiss();
                        Toast.makeText(Edit_post.this, "submit", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


}