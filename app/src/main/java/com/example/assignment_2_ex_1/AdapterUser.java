package com.example.assignment_2_ex_1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterUser extends RecyclerView.Adapter{

    private List<User> users;
    private Context context;

    public AdapterUser(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        View view = View.inflate(context, R.layout.recycler, null);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UserViewHolder viewHolder = (UserViewHolder) holder;
        viewHolder.txt_id.setText("ID: "+users.get(position).getId());
        viewHolder.txt_email.setText("Email: "+users.get(position).getEmail());
        viewHolder.txt_first_name.setText("First Name: "+users.get(position).getFirst_name());
        viewHolder.txt_last_name.setText("Last Name: "+users.get(position).getLast_name());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        TextView txt_id;
        TextView txt_email;
        TextView txt_first_name;
        TextView txt_last_name;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id = itemView.findViewById(R.id.rec_txt_id);
            txt_email = itemView.findViewById(R.id.rec_txt_email);
            txt_first_name = itemView.findViewById(R.id.rec_txt_first_name);
            txt_last_name = itemView.findViewById(R.id.rec_txt_last_name);
        }
    }
}
