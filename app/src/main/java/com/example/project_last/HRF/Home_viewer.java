package com.example.project_last.HRF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project_last.Adapter.FlatListAdapter;
import com.example.project_last.Adapter.HomeListAdapter;
import com.example.project_last.Model.RoomList;
import com.example.project_last.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Home_viewer extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinner;
    String[] filter;
    private SearchView search;
    DatabaseReference reference;
    List<RoomList> roomLists;
    HomeListAdapter homeListAdapter;

    FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_viewer);
        spinner = findViewById(R.id.filter_spinnerh);
        search = findViewById(R.id.hsearch);
        search.clearFocus();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        filter = getResources().getStringArray(R.array.filter);

        recyclerView = findViewById(R.id.homeviewerRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        roomLists = new ArrayList<>();
        readData();
        ArrayAdapter<String> arrayAdapterh = new ArrayAdapter<>(Home_viewer.this, R.layout.spinner_item2, R.id.spinner_ID2, filter);
        spinner.setAdapter(arrayAdapterh);
        spinner.clearFocus();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void readData() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equals("High To Low")) {
                    Toast.makeText(Home_viewer.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.high_to_low);
                    homeListAdapter.notifyDataSetChanged();
                    return;
                }
                if (spinner.getSelectedItem().toString().equals("Low To High")) {
                    Toast.makeText(Home_viewer.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.low_to_high);
                    homeListAdapter.notifyDataSetChanged();
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Room_details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RoomList roomList = dataSnapshot.getValue(RoomList.class);

                    if (roomList.getCategory().equals("Sublets")) {
                        roomLists.add(roomList);
                    }


                }

                homeListAdapter = new HomeListAdapter(Home_viewer.this, roomLists);
                recyclerView.setAdapter(homeListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    searchv(s);
                    return true;
                }
            });


        }

    }

    private void searchv(String s) {
        List<RoomList> Lists = new ArrayList<>();
        for (RoomList object : roomLists) {
            if (object.getLocation().toLowerCase().contains(s.toLowerCase()) || object.getProperty().toLowerCase().contains(s.toLowerCase())) {
                Lists.add(object);
            }
        }
        homeListAdapter = new HomeListAdapter(Home_viewer.this, Lists);
        recyclerView.setAdapter(homeListAdapter);
    }

}