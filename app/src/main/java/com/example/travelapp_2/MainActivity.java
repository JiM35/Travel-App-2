package com.example.travelapp_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText searchEditText;
    private RecyclerView destinationRecyclerView;
    private SQLiteDatabase database;
    private DestinationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        destinationRecyclerView = findViewById(R.id.destinationRecyclerView);
        destinationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Request permissions
        requestStoragePermission();

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                // Permission already granted, proceed with database initialization
                initializeDatabase();
            }
        } else {
            // Permissions not needed for devices below Android 6.0
            initializeDatabase();
        }
    }

    private void initializeDatabase() {
        // Create or open the database
        database = openOrCreateDatabase("travel_app_db", MODE_PRIVATE, null);
        createTable();

        // Setup RecyclerView adapter
        adapter = new DestinationAdapter(this, new ArrayList<Destination>());
        destinationRecyclerView.setAdapter(adapter);

        // Load data initially
        loadData("");
    }

    private void createTable() {
        // Create table for popular destinations
        database.execSQL("CREATE TABLE IF NOT EXISTS popular_destinations (title TEXT, location TEXT, description TEXT, pic TEXT, bed INTEGER, price INTEGER, guide TEXT, wifi TEXT, score REAL)");

        // Create table for categories
        database.execSQL("CREATE TABLE IF NOT EXISTS categories (id INTEGER PRIMARY KEY, title TEXT, picPath TEXT)");
    }

    @SuppressLint("Range")
    private void loadData(String searchText) {
        adapter.clear();
        String query = "SELECT title, location, pic, score FROM popular_destinations WHERE description LIKE '%" + searchText + "%' OR location LIKE '%" + searchText + "%'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String pic = cursor.getString(cursor.getColumnIndex("pic"));
                double score = cursor.getDouble(cursor.getColumnIndex("score"));
                Destination destination = new Destination(title, location, pic, score);
                adapter.addDestination(destination);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, initialize the database
            initializeDatabase();
        } else {
            // Permission denied, handle accordingly
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (database != null) {
            database.close();
        }
    }
}