package com.example.project_last;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_last.Adapter.MypostAdapter;
import com.example.project_last.Model.RoomList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyPost extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    List<RoomList> roomLists;
    MypostAdapter mypostAdapter;

    FirebaseUser firebaseUser;

    private static final int GALLERY_CODE1 = 300;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String myid = firebaseUser.getUid();
        recyclerView = findViewById(R.id.mypostRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        roomLists = new ArrayList<>();
        readData(myid);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void readData(String myid) {
        reference = FirebaseDatabase.getInstance().getReference("Room_details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RoomList roomList = dataSnapshot.getValue(RoomList.class);

                    if (roomList.getId().equals(myid)) {
                        roomLists.add(roomList);
                    }
                }

                mypostAdapter = new MypostAdapter(MyPost.this, roomLists);
                recyclerView.setAdapter(mypostAdapter);

                mypostAdapter.setOnClickListener(new MypostAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }

                    @Override
                    public void setEdit(int position, String userID, String name, String address, String contract, String category, String description, String location, String price, String image) {
                        Intent intent=new Intent(MyPost.this,Edit_post.class);
                        intent.putExtra("details", description);
                        intent.putExtra("property", name);
                        intent.putExtra("price", price);
                        intent.putExtra("address", address);
                        intent.putExtra("phone", contract);
                        intent.putExtra("location", location);
                        intent.putExtra("imageUrl", image);
                        intent.putExtra("category", category);
                        intent.putExtra("new_id",userID);
                        startActivity(intent);
                    }



                    @Override
                    public void setDelete(int position, String userId) {

                        RoomList select = roomLists.get(position);
                        String key = select.getNew_id();
                        reference.child(key).removeValue();
                        Toast.makeText(MyPost.this, "Delete success full...", Toast.LENGTH_SHORT).show();
                    }


                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}