package com.example.travelapp_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final Context context;
    private final ArrayList<Destination> destinations;

    public DestinationAdapter(Context context, ArrayList<Destination> destinations) {
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.bind(destination);
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public void addDestination(Destination destination) {
        destinations.add(destination);
        notifyItemInserted(destinations.size() - 1);
    }

    public void clear() {
        destinations.clear();
        notifyDataSetChanged();
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {

        ImageView picImageView;
        TextView titleTextView;
        TextView locationTextView;
        TextView scoreTextView;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            picImageView = itemView.findViewById(R.id.picImg);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTxt);
        }

        public void bind(Destination destination) {
            // Load picture using Glide
            Glide.with(itemView.getContext())
                    .load("file://" + destination.getPic()) // Assuming destination.getPic() returns the image path
                    .into(picImageView);

            titleTextView.setText(destination.getTitle());
            locationTextView.setText(destination.getLocation());
            scoreTextView.setText(String.valueOf(destination.getScore()));
        }
    }
}
