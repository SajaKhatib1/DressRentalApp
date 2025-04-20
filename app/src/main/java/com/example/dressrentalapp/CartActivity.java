package com.example.dressrentalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CartActivity extends AppCompatActivity {
    private ListView cartListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupCart();
        setupCheckoutButton();
    }

    private void setupCart() {
        cartListView = findViewById(R.id.cartListView);

        SharedPreferences prefs = getSharedPreferences("DressCart", MODE_PRIVATE);
        Set<String> cartSet = prefs.getStringSet("cart", new HashSet<>());
        cartItems = new ArrayList<>(cartSet);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        cartListView.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                clearCart();
                Toast.makeText(this, "تم تأكيد الحجز بنجاح!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "السلة فارغة", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCart() {
        SharedPreferences prefs = getSharedPreferences("DressCart", MODE_PRIVATE);
        prefs.edit().remove("cart").apply();
        cartItems.clear();
        adapter.notifyDataSetChanged();
    }

}