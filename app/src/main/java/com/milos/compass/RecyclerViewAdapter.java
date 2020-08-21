package com.milos.compass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milos.compass.Model.Restaurants;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Restaurants> restaurantsList;


    public RecyclerViewAdapter(Context context, List<Restaurants> restaurantsList) {

        this.context = context;
        this.restaurantsList = restaurantsList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Restaurants restaurants = restaurantsList.get(position);

        holder.restaurantName.setText(String.format("Name: %s", restaurants.getmName()));
        holder.restaurantAddress.setText(String.format("Address: %s", restaurants.getmAddress()));
        holder.restaurantRating.setText(restaurants.getmRating() + "/5");



    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView restaurantName;
        public TextView restaurantAddress;
        public TextView restaurantRating;
        //public TextView restaurantOpened;




        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;


            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantAddress = itemView.findViewById(R.id.restaurantAddress);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);

        }
    }
}
