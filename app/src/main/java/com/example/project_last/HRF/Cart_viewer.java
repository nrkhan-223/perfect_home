package com.example.project_last.HRF;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_last.Adapter.CartAdapter;
import com.example.project_last.EventBus.MyUpdateCartEvent;
import com.example.project_last.Listener.ICartListener;
import com.example.project_last.Model.CartModel;
import com.example.project_last.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart_viewer extends AppCompatActivity implements ICartListener {
    private Object ThreadMode;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class)){
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
        loadFormFirebase();
    }

    RecyclerView recyclerView;
    ICartListener iCartListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_viewer);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerView);

        init1();
        loadFormFirebase();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void loadFormFirebase() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String myId=firebaseUser.getUid();

        List<CartModel> cartModelList=new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(myId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                CartModel cartModel=dataSnapshot.getValue(CartModel.class);
                                cartModel.setKey(dataSnapshot.getKey());
                                cartModelList.add(cartModel);
                            }
                            iCartListener.onCartLordSuccess(cartModelList);
                        }else {
                            iCartListener.onCartLoadFailed("Cart Empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void init1() {
        iCartListener=this;

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCartLordSuccess(List<CartModel> cartModelList) {
        CartAdapter cartAdapter=new CartAdapter(this,cartModelList);
        recyclerView.setAdapter(cartAdapter);
    }

    @Override
    public void onCartLoadFailed(String message) {

    }
}