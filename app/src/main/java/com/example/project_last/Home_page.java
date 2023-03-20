package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_last.Adapter.RecentListAdapter;
import com.example.project_last.HRF.Cart_viewer;
import com.example.project_last.Model.RoomList;
import com.example.project_last.HRF.Flat_viewer;
import com.example.project_last.HRF.Home_viewer;
import com.example.project_last.HRF.Room_viewer;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button mypoporty, addpoprty, searchpoprty,cart;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    DatabaseReference reference;
    RecyclerView recyclerView;
    RecentListAdapter recentListAdapter;
    List<RoomList> roomLists;


    TextView go;
    CardView homeview, roomview, flatview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        NavigationView navigationView = findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener(this);
        searchpoprty = findViewById(R.id.searchpoprty);
        cart=findViewById(R.id.my_cart);
        addpoprty = (Button) findViewById(R.id.addpoprty);
        mypoporty = (Button) findViewById(R.id.mypoporty);
        homeview = (CardView) findViewById(R.id.imghome);
        roomview = (CardView) findViewById(R.id.imgroom);
        flatview = (CardView) findViewById(R.id.imgflat);
        go=findViewById(R.id.seeall);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent= new Intent(Home_page.this,Search_page.class);
            startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Cart_viewer.class);
                startActivity(intent);
            }
        });


        searchpoprty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_page.this, Search_page.class);
                startActivity(intent);
            }
        });
        mypoporty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_page.this, MyPost.class);
                startActivity(intent);
            }
        });
        addpoprty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_page.this, Add_home.class);
                startActivity(intent);
            }
        });
        homeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Home_viewer.class);
                startActivity(intent);
            }
        });
        roomview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Room_viewer.class);
                startActivity(intent);
            }
        });
        flatview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Flat_viewer.class);
                startActivity(intent);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerId);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recentAddRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        roomLists = new ArrayList<>();
        readData();

    }

    private void readData() {
        reference = FirebaseDatabase.getInstance().getReference("Room_details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RoomList roomList = dataSnapshot.getValue(RoomList.class);

                    roomLists.add(roomList);
                }

                recentListAdapter = new RecentListAdapter(Home_page.this, roomLists);
                recyclerView.setAdapter(recentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), Profile_page.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.nav_home) {

        } else if (item.getItemId() == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home_page.this, Inter_Number.class));
            finish();

        } else if (item.getItemId() == R.id.nav_findFragment) {

        } else if (item.getItemId() == R.id.nav_reportfreafment) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);

            String uriText = "mailto:" + Uri.encode("devoloper.creator@gmail.com") + "subject=" +
                    Uri.encode("FreedBack") + "$body=" + Uri.encode("");
            Uri uri = Uri.parse(uriText);
            intent.setData(uri);
            startActivity(Intent.createChooser(intent, "send email"));

        } else if (item.getItemId() == R.id.nav_communicatefreafment) {
           Intent intent=new Intent(getApplicationContext(),Comunication_page.class);
           startActivity(intent);
        }
        return false;
    }
}