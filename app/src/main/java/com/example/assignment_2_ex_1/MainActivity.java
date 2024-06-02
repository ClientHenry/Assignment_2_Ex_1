package com.example.assignment_2_ex_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    UserControllerRESTAPI restapi;
    Users users;
    RecyclerView recyclerView;
    Button btnLoadUsers, btnDisplayUsers, btnNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        btnLoadUsers = (Button) findViewById(R.id.btn_load_users);
        btnDisplayUsers = (Button) findViewById(R.id.btn_display_users);
        btnNextPage = (Button) findViewById(R.id.btn_next_page);

        btnLoadUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUsers(v);
            }
        });

        btnDisplayUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUsers(v);
            }
        });

        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    public void loadUsers(View view) {
        restapi = new UserControllerRESTAPI();
        restapi.start();
    }

    public void displayUsers(View view) {

        if (restapi != null) {
            users = restapi.getUsers();
            if (users != null) {
                AdapterUser adapterUser = new AdapterUser(users.getData(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapterUser);
            }
        }
    }
}