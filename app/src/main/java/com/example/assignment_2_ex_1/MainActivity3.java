package com.example.assignment_2_ex_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    DatabaseReference myRef;
    Button btn_save, btn_cancel;
    TextInputLayout til_email, til_first_name, til_last_name, til_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        myRef = FirebaseDatabase
                .getInstance().getReference("Users");

        init();

        btn_save.setOnClickListener(v -> {
            if (validateInput()) {
                saveUser();
                finish();
            }
        });

        btn_cancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void saveUser() {

        getUsers(users -> {
            if (users == null) {
                users = new ArrayList<>();
            }
            User user = new User();
            user.setId(Integer.parseInt(til_id.getEditText().getText().toString()));
            user.setEmail(til_email.getEditText().getText().toString());
            user.setFirst_name(til_first_name.getEditText().getText().toString());
            user.setLast_name(til_last_name.getEditText().getText().toString());
            users.add(user);
            myRef.setValue(users);
            Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput() {

        if (til_id.getEditText().getText().toString().isEmpty()) {
            til_id.setError("ID is required");
            return false;
        } else {
            til_id.setError(null);
        }

        if (til_email.getEditText().getText().toString().isEmpty()) {
            til_email.setError("Email is required");
            return false;
        } else {
            til_email.setError(null);
        }

        if (til_first_name.getEditText().getText().toString().isEmpty()) {
            til_first_name.setError("First name is required");
            return false;
        } else {
            til_first_name.setError(null);
        }

        if (til_last_name.getEditText().getText().toString().isEmpty()) {
            til_last_name.setError("Last name is required");
            return false;
        } else {
            til_last_name.setError(null);
        }

        return true;
    }

    private void init() {
        til_email = (TextInputLayout) findViewById(R.id.txt_email);
        til_first_name = (TextInputLayout) findViewById(R.id.txt_first_name);
        til_last_name = (TextInputLayout) findViewById(R.id.txt_last_name);
        til_id = (TextInputLayout) findViewById(R.id.txt_id);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void getUsers(OnUserListener listener) {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<User> users = new ArrayList<>();

                for (DataSnapshot d : snapshot.getChildren()) {
                    User user = d.getValue(User.class);
                    users.add(user);

                }
                listener.onUser(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onUser(null);
            }
        });
    }

    public interface OnUserListener {
        void onUser(List<User> users);
    }
}