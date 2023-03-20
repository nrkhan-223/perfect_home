package com.example.project_last.Details_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project_last.Checkout_page;
import com.example.project_last.R;

import java.util.Objects;

public class Cart_detailsList extends AppCompatActivity {
    Intent intent;
    String property, price, address, phone, location, imageUrl, category, details,key;
    TextView name, pricev, locationv, catagory, Description, contract, addrerss;
    ImageView imageView;
    Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        details = intent.getStringExtra("details");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        location = intent.getStringExtra("location");
        imageUrl = intent.getStringExtra("imageUrl");
        category = intent.getStringExtra("category");
        property = intent.getStringExtra("property");
        price = intent.getStringExtra("price");
        key=intent.getStringExtra("key");

        Description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        addrerss = findViewById(R.id.address);
        pricev = findViewById(R.id.price);
        locationv = findViewById(R.id.location);
        catagory = findViewById(R.id.cdetails);
        contract = findViewById(R.id.contract);
        imageView = findViewById(R.id.image);
        checkout=findViewById(R.id.checkout);

        Glide.with(Cart_detailsList.this).load(imageUrl).into(imageView);
        name.setText(property);
        Description.setText(details);
        pricev.setText(price);
        locationv.setText(location);
        catagory.setText(category);
        contract.setText(phone);
        addrerss.setText(address);

        checkout.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), Checkout_page.class);
            intent.putExtra("id",key);
            intent.putExtra("img",imageUrl);
            intent.putExtra("name",property);
            intent.putExtra("price",price);
            intent.putExtra("type",category);
            intent.putExtra("location",location);
            startActivity(intent);
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}