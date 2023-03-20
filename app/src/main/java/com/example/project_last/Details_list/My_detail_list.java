package com.example.project_last.Details_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_last.Edit_post;
import com.example.project_last.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class My_detail_list extends AppCompatActivity {

    Intent intent;
    String property, price, address, phone, location, imageUrl, category, details,key;
    TextView name, pricev, locationv, catagory, Description, contract, addrerss;
    ImageView imageView;
    //Button checkout,addCart;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myId=firebaseUser.getUid();

        intent = getIntent();
        details = intent.getStringExtra("details");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        location = intent.getStringExtra("location");
        imageUrl = intent.getStringExtra("imageUrl");
        category = intent.getStringExtra("category");
        property = intent.getStringExtra("property");
        price = intent.getStringExtra("price");
        //key=intent.getStringExtra("key");

        Description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        addrerss = findViewById(R.id.address);
        pricev = findViewById(R.id.price);
        locationv = findViewById(R.id.location);
        catagory = findViewById(R.id.cdetails);
        contract = findViewById(R.id.contract);
        imageView = findViewById(R.id.image);

        Glide.with(My_detail_list.this).load(imageUrl).into(imageView);
        name.setText(property);
        Description.setText(details);
        pricev.setText(price);
        locationv.setText(location);
        catagory.setText(category);
        contract.setText(phone);
        addrerss.setText(address);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}