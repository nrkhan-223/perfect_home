package com.example.project_last.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_last.Details_list.All_detailsList;
import com.example.project_last.Model.RoomList;
import com.example.project_last.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {
    static Context context;
    static List<RoomList> roomLists;

    public SearchAdapter(Context context,List <RoomList> roomLists) {
        SearchAdapter.context = context;
        SearchAdapter.roomLists = roomLists;
    }

    @NonNull
    @Override
    public SearchAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_design,parent,false);
        return new SearchAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyHolder holder, int position) {
        RoomList roomList=roomLists.get(position);
        if (roomList.getImageUrl().equals("imageUrl")){
            holder.imageView.setImageResource(R.drawable.house);
        }else {
            Glide.with(context).load(roomList.getImageUrl()).into(holder.imageView);
        }

        holder.location.setText(roomList.getLocation());
        holder.nameText.setText(roomList.getProperty());
        holder.priceText.setText(roomList.getPrice());

    }

    @Override
    public int getItemCount() {
        return roomLists.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ShapeableImageView imageView;
        TextView nameText,priceText,location;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            location=itemView.findViewById(R.id.propertyLocationItem);
            imageView=itemView.findViewById(R.id.propertyImageItem);
            nameText=itemView.findViewById(R.id.propertyNameItem);
            priceText=itemView.findViewById(R.id.propertyPriceItem);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            RoomList roomList=roomLists.get(getAdapterPosition());
            String details=roomList.getDetails();
            String propertyName=roomList.getProperty();
            String price=roomList.getPrice();
            String address=roomList.getAddress();
            String phone=roomList.getPhone();
            String location=roomList.getLocation();
            String image=roomList.getImageUrl();
            String category=roomList.getCategory();
            String key=roomList.getNew_id();



            Intent intent=new Intent(context, All_detailsList.class);
            intent.putExtra("details",details);
            intent.putExtra("property",propertyName);
            intent.putExtra("price",price);
            intent.putExtra("address",address);
            intent.putExtra("phone",phone);
            intent.putExtra("location",location);
            intent.putExtra("imageUrl",image);
            intent.putExtra("category",category);
            intent.putExtra("key",key);
            context.startActivity(intent);
        }
    }
}
