package com.muskan.quizapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    User user;
    private Context context;
    private List<User> userList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName,tvEmail;


        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        }


    }

    public UserAdapter(Context mContext, List<User> userList) {
        this.context = mContext;
        this.userList = userList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);


        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        user = userList.get(position);
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}