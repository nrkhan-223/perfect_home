package com.example.project_last.Listener;

import com.example.project_last.Model.CartModel;

import java.util.List;

public interface ICartListener {

    void  onCartLordSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
