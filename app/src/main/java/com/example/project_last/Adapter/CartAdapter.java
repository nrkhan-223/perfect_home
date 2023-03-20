package com.example.project_last.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.project_last.Details_list.Cart_detailsList;
import com.example.project_last.EventBus.MyUpdateCartEvent;
import com.example.project_last.Model.CartModel;
import com.example.project_last.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import org.greenrobot.eventbus.EventBus;
import java.util.List;



public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder>{
    Context context;
    List<CartModel> cartModelList;
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    String myId=firebaseUser.getUid();

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_design3,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CartModel cartModel=cartModelList.get(position);
        Glide.with(context).load(cartModel.getImageUrl()).into(holder.img);


        holder.name.setText(cartModel.getProperty());
        holder.price.setText(cartModel.getPrice());
        holder.location.setText(cartModel.getLocation());
        holder.delete.setOnClickListener(view -> {
            AlertDialog dialog;
            dialog=new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you want to really want to delete item")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        notifyItemRemoved(position);
                        deleteFormFirebase(cartModelList.get(position));
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        });
    }

    private void deleteFormFirebase(CartModel cartModel) {

        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(myId)
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid-> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
        
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ShapeableImageView img;
        ImageView delete;
        TextView name,price,location;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.ImageItem);
            delete=itemView.findViewById(R.id.delete);
            name=itemView.findViewById(R.id.propertyNameItem);
            price=itemView.findViewById(R.id.propertyPriceItem);
            location=itemView.findViewById(R.id.propertyLocationItem);
            delete=itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CartModel cartModel=cartModelList.get(getAdapterPosition());
            String name = cartModel.getProperty();
            String address = cartModel.getAddress();
            String contract = cartModel.getPhone();
            String category = cartModel.getCategory();
            String description = cartModel.getDetails();
            String location = cartModel.getLocation();
            String price = cartModel.getPrice();
            String image = cartModel.getImageUrl();

            Intent intent = new Intent(context, Cart_detailsList.class);
            intent.putExtra("details", description);
            intent.putExtra("property", name);
            intent.putExtra("price", price);
            intent.putExtra("address", address);
            intent.putExtra("phone", contract);
            intent.putExtra("location", location);
            intent.putExtra("imageUrl", image);
            intent.putExtra("category", category);
            context.startActivity(intent);

        }
    }
}
