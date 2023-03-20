package com.example.project_last.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_last.Model.RoomList;
import com.example.project_last.Details_list.My_detail_list;
import com.example.project_last.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MypostAdapter extends RecyclerView.Adapter<MypostAdapter.MyHolder> {
    Context context;
    List<RoomList> roomLists;
    DatabaseReference databaseReference;
    String userID;
    private onItemClickListener listener;


    public MypostAdapter(Context context, List<RoomList> roomLists) {
        this.context = context;
        this.roomLists = roomLists;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_design, parent, false);
        return new MypostAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        RoomList roomList = roomLists.get(position);
        if (roomList.getImageUrl().equals("imageUrl")) {
            holder.img.setImageResource(R.drawable.house);
        } else {
            Glide.with(context).load(roomList.getImageUrl()).into(holder.img);
        }

        holder.name.setText(roomList.getProperty());
        holder.price.setText(roomList.getPrice());


    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView name, price;
        ImageView img;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.propertyNameItem);
            price = itemView.findViewById(R.id.propertyPriceItem);
            img = itemView.findViewById(R.id.propertyImageItem);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {


            RoomList room = roomLists.get(getAdapterPosition());
            String name = room.getProperty();
            String address = room.getAddress();
            String contract = room.getPhone();
            String category = room.getCategory();
            String description = room.getDetails();
            String location = room.getLocation();
            String price = room.getPrice();
            String image = room.getImageUrl();
            String new_id = room.getNew_id();


            Intent intent = new Intent(context, My_detail_list.class);
            intent.putExtra("details", description);
            intent.putExtra("property", name);
            intent.putExtra("price", price);
            intent.putExtra("address", address);
            intent.putExtra("phone", contract);
            intent.putExtra("location", location);
            intent.putExtra("imageUrl", image);
            intent.putExtra("category", category);
            context.startActivity(intent);

            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }

        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null) {
                RoomList room = roomLists.get(getAdapterPosition());
                userID = room.getNew_id();
                String name = room.getProperty();
                String address = room.getAddress();
                String contract = room.getPhone();
                String category = room.getCategory();
                String description = room.getDetails();
                String location = room.getLocation();
                String price = room.getPrice();
                String image = room.getImageUrl();


                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            listener.setEdit(position, userID,name,address,contract,category,description,location,price,image);
                            return true;
                        case 2:
                            listener.setDelete(position,userID);
                            return true;
                    }
                }
            }


            return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Options");
        MenuItem setStatus=menu.add(Menu.NONE,1,1,"Edit");
        MenuItem setDayAndTime=menu.add(Menu.NONE,2,2,"Delete");

        setStatus.setOnMenuItemClickListener(this);
        setDayAndTime.setOnMenuItemClickListener(this);
    }
}

public interface onItemClickListener {
    void onItemClick(int position);

    void setDelete(int position, String userId);

    void setEdit(int position, String userID, String name, String address, String contract, String category, String description, String location, String price, String image);
}

    public void setOnClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
