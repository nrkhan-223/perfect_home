package com.example.project_last.HRF;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project_last.Adapter.HomeListAdapter;
import com.example.project_last.Adapter.RecentListAdapter;
import com.example.project_last.Adapter.RoomListAdapter;
import com.example.project_last.Home_page;
import com.example.project_last.Model.RoomList;
import com.example.project_last.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room_viewer extends AppCompatActivity {
    RecyclerView recyclerView;
    private SearchView search;
    Spinner spinner;
    String[] filter;

    DatabaseReference reference;
    List<RoomList> roomLists;
    RoomListAdapter roomListAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_viewer);
        spinner = findViewById(R.id.filter_spinnerr);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        search = findViewById(R.id.rsearch);
        search.clearFocus();
        filter = getResources().getStringArray(R.array.filter);


        recyclerView = findViewById(R.id.roomviewRecicle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        roomLists = new ArrayList<>();
        ArrayAdapter<String> arrayAdapterr = new ArrayAdapter<>(Room_viewer.this, R.layout.spinner_item2, R.id.spinner_ID2, filter);
        spinner.setAdapter(arrayAdapterr);
        spinner.clearFocus();

        readData();

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
                    Toast.makeText(Room_viewer.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.high_to_low);
                    roomListAdapter.notifyDataSetChanged();
                    return;
                }
                if (spinner.getSelectedItem().toString().equals("Low To High")) {
                    Toast.makeText(Room_viewer.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.low_to_high);
                    roomListAdapter.notifyDataSetChanged();
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

                    if (roomList.getCategory().equals("Room")) {
                        roomLists.add(roomList);
                    }
                }

                roomListAdapter = new RoomListAdapter(Room_viewer.this, roomLists);
                recyclerView.setAdapter(roomListAdapter);

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
        roomListAdapter = new RoomListAdapter(Room_viewer.this, Lists);
        recyclerView.setAdapter(roomListAdapter);
    }

}