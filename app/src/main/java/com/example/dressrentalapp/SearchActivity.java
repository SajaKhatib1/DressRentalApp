package com.example.dressrentalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private Spinner categorySpinner;
    private EditText searchEditText;
    private CheckBox newOnlyCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        try {
            setupViews();
            setupSpinner();
        } catch (Exception e) {
            Log.e("SearchActivity", "Initialization error", e);
            Toast.makeText(this, "حدث خطأ في تهيئة البحث", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupViews() {
        categorySpinner = findViewById(R.id.categorySpinner);
        searchEditText = findViewById(R.id.searchEditText);
        newOnlyCheckbox = findViewById(R.id.newOnlyCheckbox);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            try {
                performSearch();
            } catch (Exception e) {
                Log.e("SearchActivity", "Search error", e);
                Toast.makeText(this, "حدث خطأ أثناء البحث", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        try {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.dress_categories,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("SearchActivity", "Spinner setup error", e);
            throw e;
        }
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        boolean newOnly = newOnlyCheckbox.isChecked();

        Log.d("SEARCH_DEBUG", "Sending -> Query: " + query + ", Category: " + category + ", NewOnly: " + newOnly);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("query", query);
        resultIntent.putExtra("category", category);
        resultIntent.putExtra("newOnly", newOnly);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}