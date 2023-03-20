package com.example.project_last;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.project_last.Adapter.SearchAdapter;
import com.example.project_last.HRF.Flat_viewer;
import com.example.project_last.Model.RoomList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Search_page extends AppCompatActivity {
    private SearchView search;

    String[] filter;
    Spinner spinner;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<RoomList> roomLists;
    SearchAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        filter = getResources().getStringArray(R.array.filter);
        search = findViewById(R.id.search);
        search.clearFocus();
        spinner = findViewById(R.id.filter_spinners);
        recyclerView = findViewById(R.id.search_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ArrayAdapter<String> arrayAdapters = new ArrayAdapter<String>(Search_page.this, R.layout.spinner_item2, R.id.spinner_ID2, filter);
        spinner.setAdapter(arrayAdapters);
        spinner.clearFocus();
        roomLists = new ArrayList<>();

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
                    Toast.makeText(Search_page.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.high_to_low);
                    searchAdapter.notifyDataSetChanged();
                    return;
                }
                if (spinner.getSelectedItem().toString().equals("Low To High")) {
                    Toast.makeText(Search_page.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    roomLists.sort(RoomList.low_to_high);
                    searchAdapter.notifyDataSetChanged();
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

                    roomLists.add(roomList);
                }
                searchAdapter = new SearchAdapter(Search_page.this,roomLists);
                recyclerView.setAdapter(searchAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Search_page.this, "Error database", Toast.LENGTH_SHORT).show();
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
        searchAdapter=new SearchAdapter(Search_page.this,Lists);
        recyclerView.setAdapter(searchAdapter);
    }
}