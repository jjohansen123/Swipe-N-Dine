package com.example.cst438_hungry_hungry_programmers.swipe_n_dine.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.R;
import com.example.cst438_hungry_hungry_programmers.swipe_n_dine.models.Restaurant;

import java.util.List;

/**
 * Created by coleman on 12/14/17.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvName;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.favoriteTexts);
        }
    }


    private List<Restaurant> mFavorites;
    private Context mContext;

    public FavoritesAdapter(Context context, List<Restaurant> favorites) {
        mFavorites = favorites;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_favorite, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Restaurant restaurant = mFavorites.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.tvName;
        textView.setText(restaurant.getName());

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse(restaurant.getUrl()));

                getContext().startActivity(intent);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFavorites.size();
    }
}