package com.example.assignment_2_ex_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    TextInputLayout txt_id, txt_email, txt_first_name, txt_last_name;
    Button btn_update, btn_delete, btn_next, btn_previous, btn_add, btn_update_save, btn_update_cancel, btn_refresh;
    SearchView searchView;
    int index = 0;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        init();

        myRef = FirebaseDatabase
                .getInstance().getReference("Users");

        getUsers(new OnUserListener() {
            @Override
            public void onUser(List<User> users) {
                if (users != null && !users.isEmpty()) {

                    displayUser();
                } else {
                    Toast.makeText(MainActivity2.this, "No users", Toast.LENGTH_SHORT).show();
                    btn_delete.setEnabled(false);
                    btn_update.setEnabled(false);
                    btn_next.setEnabled(false);
                    btn_previous.setEnabled(false);
                }
            }
        });

        btn_delete.setOnClickListener(v -> {
            deleteUser();
        });

        btn_update.setOnClickListener(v -> {
            txt_id.getEditText().setEnabled(true);
            txt_email.getEditText().setEnabled(true);
            txt_first_name.getEditText().setEnabled(true);
            txt_last_name.getEditText().setEnabled(true);
            btn_update_save.setVisibility(Button.VISIBLE);
            btn_update_cancel.setVisibility(Button.VISIBLE);
            btn_update.setEnabled(false);
            btn_delete.setEnabled(false);
            btn_next.setEnabled(false);
            btn_previous.setEnabled(false);

        });

        btn_update_save.setOnClickListener(v -> {
            updateUser();
            txt_id.getEditText().setEnabled(false);
            txt_email.getEditText().setEnabled(false);
            txt_first_name.getEditText().setEnabled(false);
            txt_last_name.getEditText().setEnabled(false);
            btn_update_save.setVisibility(Button.INVISIBLE);
            btn_update_cancel.setVisibility(Button.INVISIBLE);
            btn_update.setEnabled(true);
            btn_delete.setEnabled(true);
            btn_next.setEnabled(true);
            btn_previous.setEnabled(true);
        });

        btn_update_cancel.setOnClickListener(v -> {
            txt_id.getEditText().setEnabled(false);
            txt_email.getEditText().setEnabled(false);
            txt_first_name.getEditText().setEnabled(false);
            txt_last_name.getEditText().setEnabled(false);
            btn_update_save.setVisibility(Button.INVISIBLE);
            btn_update_cancel.setVisibility(Button.INVISIBLE);
            btn_update.setEnabled(true);
            btn_delete.setEnabled(true);
            btn_next.setEnabled(true);
            btn_previous.setEnabled(true);
        });

        btn_next.setOnClickListener(v -> {
            getUsers(new OnUserListener() {
                @Override
                public void onUser(List<User> users) {
                    if (users != null) {
                        if (index < users.size() - 1) {
                            index++;
                            displayUser();
                        } else {
                            Toast.makeText(MainActivity2.this, "No more users", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        });

        btn_previous.setOnClickListener(v -> {

            getUsers(new OnUserListener() {
                @Override
                public void onUser(List<User> users) {
                    if (index > 0) {
                        index--;
                        displayUser();
                    } else {
                        Toast.makeText(MainActivity2.this, "No more users", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        });

        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            startActivity(intent);
        });

        btn_refresh.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getUsers(new OnUserListener() {
                    @Override
                    public void onUser(List<User> users) {
                        if (users != null) {
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getFirst_name().equals(query)) {
                                    index = i;
                                    displayUser();
                                    return;
                                }
                            }
                            Toast.makeText(MainActivity2.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void init() {
        txt_id = findViewById(R.id.txt_id);
        txt_email = findViewById(R.id.txt_email);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_next = findViewById(R.id.btn_next);
        btn_previous = findViewById(R.id.btn_previous);
        btn_add = findViewById(R.id.btn_add);
        btn_update_save = findViewById(R.id.btn_update_save);
        btn_update_cancel = findViewById(R.id.btn_update_cancel);
        btn_refresh = findViewById(R.id.btn_refresh);
        searchView = findViewById(R.id.searchView);
    }

    public void displayUser() {

        getUsers(new OnUserListener() {
            @Override
            public void onUser(List<User> users) {

                if (users.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "No Users", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = users.get(index);
                txt_id.getEditText().setText(String.valueOf(user.getId()));
                txt_email.getEditText().setText(user.getEmail());
                txt_first_name.getEditText().setText(user.getFirst_name());
                txt_last_name.getEditText().setText(user.getLast_name());
            }
        });
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

    public void updateUser() {

        getUsers(new OnUserListener() {
            @Override
            public void onUser(List<User> users) {

                if (validateInput()) {

                    User user = users.get(index);
                    user.setId(Integer.parseInt(txt_id.getEditText().getText().toString()));
                    user.setEmail(txt_email.getEditText().getText().toString());
                    user.setFirst_name(txt_first_name.getEditText().getText().toString());
                    user.setLast_name(txt_last_name.getEditText().getText().toString());
                    myRef.setValue(users);
                    Toast.makeText(MainActivity2.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteUser() {

        getUsers(new OnUserListener() {
            @Override
            public void onUser(List<User> users) {

                if (users.size() == 1) {
                    users.remove(index);
                    myRef.setValue(users);
                    Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                } else if (index == users.size() - 1) {

                    users.remove(index);
                    myRef.setValue(users);
                    index--;
                } else {
                    users.remove(index);

                    myRef.setValue(users);
                }
                Toast.makeText(MainActivity2.this, "User deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validateInput() {
        if (txt_id.getEditText().getText() == null || txt_email.getEditText().getText() == null || txt_first_name.getEditText().getText() == null || txt_last_name.getEditText().getText() == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}