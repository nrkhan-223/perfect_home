package com.example.project_last.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_last.Details_list.All_detailsList;
import com.example.project_last.Model.RoomList;
import com.example.project_last.R;

import java.util.List;

public class FlatListAdapter extends RecyclerView.Adapter<FlatListAdapter.MyViewHolder> {
    static Context context;
    static List<RoomList> roomLists;

    public FlatListAdapter(Context context, List<RoomList> roomLists) {
        this.context = context;
        this.roomLists = roomLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_design,parent,false);
        return new FlatListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RoomList roomList=roomLists.get(position);
        if (roomList.getImageUrl().equals("imageUrl")){
            holder.img.setImageResource(R.drawable.house);
        }else {
            Glide.with(context).load(roomList.getImageUrl()).into(holder.img);
        }
        holder.location.setText(roomList.getLocation());
        holder.name.setText(roomList.getProperty());
        holder.price.setText(roomList.getPrice());

    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,price,location;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            location=itemView.findViewById(R.id.propertyLocationItem);
            name=itemView.findViewById(R.id.propertyNameItem);
            price=itemView.findViewById(R.id.propertyPriceItem);
            img=itemView.findViewById(R.id.propertyImageItem);
            itemView.setOnClickListener(this);

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
            String key=room.getNew_id();

            Intent intent = new Intent(context, All_detailsList.class);
            intent.putExtra("details", description);
            intent.putExtra("property", name);
            intent.putExtra("price", price);
            intent.putExtra("address", address);
            intent.putExtra("phone", contract);
            intent.putExtra("location", location);
            intent.putExtra("imageUrl", image);
            intent.putExtra("category", category);
            intent.putExtra("key",key);
            context.startActivity(intent);
        }
    }

}
