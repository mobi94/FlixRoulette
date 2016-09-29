package com.netflix.roulette.myclientserverapplication;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private Context context;
    private JSONArray jsonMoviesArray;
    private boolean shouldSave;

    MoviesListAdapter(Context context, JSONArray jsonMoviesArray, boolean shouldSave) {
        this.context = context;
        this.jsonMoviesArray= jsonMoviesArray;
        this.shouldSave = shouldSave;
    }

    @Override
    public int getItemCount() {
        return jsonMoviesArray.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject currentMovie = jsonMoviesArray.getJSONObject(position);
            Picasso.with(context)
                    .load((String) currentMovie.get("poster"))
                    .error(R.mipmap.ic_launcher)
                    .into(holder.poster);
            holder.category.setText((String) currentMovie.get("category"));
            holder.director.setText((String) currentMovie.get("director"));
            holder.title.setText((String) currentMovie.get("show_title"));
            holder.rating.setText((String) currentMovie.get("rating"));
            holder.releaseDate.setText((String) currentMovie.get("release_year"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getAdapterPosition();
                    Intent intent = new Intent(context, DetailsModeActivity.class);
                    intent.putExtra(MainActivity.MOVIE_DATA, jsonMoviesArray.toString());
                    intent.putExtra(MainActivity.MOVIE_INDEX, itemPosition);
                    intent.putExtra(MainActivity.MOVIE_SAVE, shouldSave);
                    context.startActivity(intent);
                }
            });
        }
    }
}