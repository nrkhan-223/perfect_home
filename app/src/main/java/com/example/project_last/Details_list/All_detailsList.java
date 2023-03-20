package com.example.project_last.Details_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_last.Checkout_page;
import com.example.project_last.Model.CartModel;
import com.example.project_last.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class All_detailsList extends AppCompatActivity {

    Intent intent;
    String property, price, address, phone, location, imageUrl, category, details,key;
    TextView name, pricev, locationv, catagory, Description, contract, addrerss;
    ImageView imageView;
    Button checkout,addCart;
    String myId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details_list);
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
        addCart=findViewById(R.id.addCart_ID);

        Glide.with(All_detailsList.this).load(imageUrl).into(imageView);
        name.setText(property);
        Description.setText(details);
        pricev.setText(String.format("%stk", price));
        locationv.setText(location);
        catagory.setText(category);
        contract.setText(phone);
        addrerss.setText(address);



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Checkout_page.class);
                intent.putExtra("id",key);
                intent.putExtra("img",imageUrl);
                intent.putExtra("name",property);
                intent.putExtra("price",price);
                intent.putExtra("type",category);
                intent.putExtra("location",location);
                startActivity(intent);

            }
        });

        addCart.setOnClickListener(view -> addCart(myId,key,property,price,imageUrl,details,address,phone,location,category));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void addCart(String myId, String key, String property, String price, String imageUrl, String details, String address, String phone, String location, String category) {
        DatabaseReference userCart= FirebaseDatabase.getInstance().getReference("Cart").child(myId);
        userCart.child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            //CartModel cartModel=snapshot.getValue(CartModel.class);

//                            updateData.put("quantity",cartModel.getQuantity());
//                            updateData.put("totalPrice",cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));
                            Map<String,Object> updateData=new HashMap<>();
                            userCart.child(key)
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(unused -> Toast.makeText(All_detailsList.this, "Add Cart", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(All_detailsList.this, "Not Add", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else {
                            CartModel cartModel=new CartModel();
                            cartModel.setProperty(property);
                            cartModel.setImageUrl(imageUrl);
                            cartModel.setPrice(price);
                            cartModel.setDetails(details);
                            cartModel.setAddress(address);
                            cartModel.setPhone(phone);
                            cartModel.setLocation(location);
                            cartModel.setCategory(category);
                            //cartModel.setQuantity(1);

                           // cartModel.setTotalPrice(Float.parseFloat(price));

                            userCart.child(key)
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> Toast.makeText(All_detailsList.this, "Add Cart", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(All_detailsList.this, "Not Add", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}