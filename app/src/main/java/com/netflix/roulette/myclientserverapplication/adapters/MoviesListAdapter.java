package com.netflix.roulette.myclientserverapplication.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import com.netflix.roulette.myclientserverapplication.R;
import com.netflix.roulette.myclientserverapplication.activities.DetailsModeActivity;
import com.netflix.roulette.myclientserverapplication.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private Context context;
    private List<Movie> moviesArray;
    private boolean shouldSave;

    public MoviesListAdapter(Context context, List<Movie> moviesArray, boolean shouldSave) {
        this.context = context;
        this.moviesArray= moviesArray;
        this.shouldSave = shouldSave;
    }

    @Override
    public int getItemCount() {
        return moviesArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie currentMovie = moviesArray.get(position);
        Picasso.with(context)
                .load(currentMovie.getPoster())
                .error(R.mipmap.ic_launcher)
                .into(holder.poster);
        holder.category.setText(currentMovie.getCategory());
        holder.director.setText(currentMovie.getDirector());
        holder.title.setText(currentMovie.getShowTitle());
        holder.rating.setText(currentMovie.getRating());
        holder.releaseDate.setText(currentMovie.getReleaseYear());
    }

    public void setNewData(List<Movie> moviesArray){
        this.moviesArray= moviesArray;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView category;
        TextView director;
        TextView title;
        TextView rating;
        TextView releaseDate;

        ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.poster_image);
            category = (TextView) v.findViewById(R.id.category);
            director = (TextView) v.findViewById(R.id.director);
            title = (TextView) v.findViewById(R.id.title);
            rating = (TextView) v.findViewById(R.id.rating);
            releaseDate = (TextView) v.findViewById(R.id.release_date);

            v.setOnClickListener(view -> {
                int itemPosition = getAdapterPosition();
                Intent intent = new Intent(context, DetailsModeActivity.class);
                intent.putExtra(MainActivity.MOVIE_DATA, new Gson().toJson(moviesArray));
                intent.putExtra(MainActivity.MOVIE_INDEX, itemPosition);
                intent.putExtra(MainActivity.MOVIE_SAVE, shouldSave);
                context.startActivity(intent);
            });
        }
    }
}