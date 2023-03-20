package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_last.Details_list.All_detailsList;
import com.example.project_last.Model.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Checkout_page extends AppCompatActivity {
    TextView namee, id, typee, pricee, location, name2, email, phone,totalprice;
    ShapeableImageView img;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String myID;
    int mulprice;
    private RadioGroup radioGroup;
    RadioButton pricebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chackout_page);
        totalprice=findViewById(R.id.price2);
        radioGroup = findViewById(R.id.payment_type);
        img = findViewById(R.id.img1);
        namee = findViewById(R.id.namep);
        id = findViewById(R.id.idnum);
        typee = findViewById(R.id.type);
        pricee = findViewById(R.id.prices);
        location = findViewById(R.id.locations);
        name2 = findViewById(R.id.myname);
        email = findViewById(R.id.mymail);
        phone = findViewById(R.id.myphone);

        Intent intent = getIntent();
        String id1 = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String type = intent.getStringExtra("type");
        String price = intent.getStringExtra("price");
        String locations = intent.getStringExtra("location");
        String imgurl = intent.getStringExtra("img");


        id.setText(String.format("ID%s", id1));
        namee.setText(String.format("Name%s", name));
        typee.setText(String.format("Room Type%s", type));
        pricee.setText(String.format("price-%stk", price));
        location.setText(String.format("Location%s", locations));
        Glide.with(Checkout_page.this).load(imgurl).into(img);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String myid = firebaseUser.getUid();
        totalprice.setText(String.format("%sTk", price));
        reference = FirebaseDatabase.getInstance().getReference("User").child(myid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                name2.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Checkout_page.this, "Error network", Toast.LENGTH_SHORT).show();
            }
        });
        mulprice = Integer.parseInt(price);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.full) {
                    totalprice.setText(String.format("%stk", price));                }
                else if (checkedId == R.id.half){
                    int price1 = mulprice/2;
                    totalprice.setText(String.valueOf(price1+"tk"));
                }
            }
        });

    }
}