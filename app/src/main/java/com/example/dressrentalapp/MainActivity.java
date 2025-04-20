package com.example.dressrentalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int SEARCH_REQUEST_CODE = 1;
    private ListView dressListView;
    private ArrayList<Dress> dressList;
    private DressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initializeDresses();
            setupListView();
            setupButtons();
        } catch (Exception e) {
            Log.e("MainActivity", "Initialization error", e);
            Toast.makeText(this, "حدث خطأ في تهيئة التطبيق", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeDresses() {
        dressList = new ArrayList<>();
        dressList.add(new Dress("blue dress", 200, R.drawable.dress1, 5, "سهرة"));
        dressList.add(new Dress("red dress", 130, R.drawable.dress2, 3, "خطوبة"));
        dressList.add(new Dress("black dress", 170, R.drawable.dress3, 2, "سهرة"));
        dressList.add(new Dress("white wedding dress", 2000, R.drawable.dress4, 5, "زفاف"));
    }

    private void setupListView() {
        dressListView = findViewById(R.id.dressListView);
        adapter = new DressAdapter(this, R.layout.list_item_dress, dressList);
        dressListView.setAdapter(adapter);

        dressListView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                Dress selected = dressList.get(position);
                addToCart(selected);
            } catch (Exception e) {
                Log.e("MainActivity", "Item click error", e);
                Toast.makeText(this, "حدث خطأ أثناء إضافة الفستان", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
        Button searchBtn = findViewById(R.id.searchButton);
        Button cartBtn = findViewById(R.id.cartButton);

        searchBtn.setOnClickListener(v -> {
            try {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
            } catch (Exception e) {
                Log.e("MainActivity", "Search button error", e);
                Toast.makeText(this, "حدث خطأ أثناء فتح البحث", Toast.LENGTH_SHORT).show();
            }
        });

        cartBtn.setOnClickListener(v -> {
            try {
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
            } catch (Exception e) {
                Log.e("MainActivity", "Cart button error", e);
                Toast.makeText(this, "حدث خطأ أثناء فتح السلة", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String query = data.getStringExtra("query");
                String category = data.getStringExtra("category");
                boolean newOnly = data.getBooleanExtra("newOnly", false);

                Log.d("SEARCH_DEBUG", "Received -> Query: " + query + ", Category: " + category + ", NewOnly: " + newOnly);

                filterDresses(query, category, newOnly);
            }
        }
    }

    private void filterDresses(String query, String category, boolean newOnly) {
        Log.d("SEARCH_DEBUG", "Filtering -> Query: " + query + ", Category: " + category);
        List<Dress> filteredList = new ArrayList<>();
        for (Dress dress : dressList) {
            boolean matchesQuery = query.isEmpty() ||
                    dress.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = category.equals("الكل") ||
                    dress.getCategory().equals(category);
            boolean matchesNewOnly = !newOnly || dress.getQuantity() > 0;

            if (matchesQuery && matchesCategory && matchesNewOnly) {
                filteredList.add(dress);
            }
        }
        adapter.updateDresses(filteredList);
    }

    private void addToCart(Dress dress) {
        try {
            if (dress == null) {
                Toast.makeText(this, "الفستان غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dress.getQuantity() > 0) {
                dress.setQuantity(dress.getQuantity() - 1);
                adapter.notifyDataSetChanged();

                SharedPreferences prefs = getSharedPreferences("DressCart", MODE_PRIVATE);
                Set<String> cartItems = new HashSet<>(prefs.getStringSet("cart", new HashSet<>()));
                cartItems.add(dress.getName() + " - " + dress.getPrice() + "₪");

                prefs.edit()
                        .putStringSet("cart", cartItems)
                        .apply();

                Toast.makeText(this, "تمت إضافة " + dress.getName() + " للسلة", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "هذا الفستان غير متوفر", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Add to cart error", e);
            Toast.makeText(this, "حدث خطأ أثناء الإضافة للسلة", Toast.LENGTH_SHORT).show();
        }
    }
}