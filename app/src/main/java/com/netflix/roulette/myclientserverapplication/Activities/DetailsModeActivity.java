package com.netflix.roulette.myclientserverapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.roulette.myclientserverapplication.R;
import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsModeActivity extends AppCompatActivity {

    MoviesListAdapter adapter;
    RecyclerView recyclerView;
    List<Movie> moviesArray;
    int movieIndex, currentIndex;
    boolean shouldSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        moviesArray = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.detailed_movies_list);

        getData();
        initializeRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (shouldSave)
            getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_movie) {
            saveCurrentMovie();
            return true;
        }
        else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveCurrentMovie(){
        boolean isIncluded = false;
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        List<Movie> savedMoviesArray = new Gson().fromJson(sharedpreferences.getString(MainActivity.PREFERENCES_DATA, "[]"),
                new TypeToken<List<Movie>>(){}.getType());
        for (int i = 0; i < savedMoviesArray.size(); i++) {
            if (savedMoviesArray.get(i).getShowTitle().
                    equals(moviesArray.get(currentIndex).getShowTitle())) {
                isIncluded = true;
                break;
            }
        }
        if (!isIncluded) {
            savedMoviesArray.add(moviesArray.get(currentIndex));
            sharedpreferences.edit().putString(MainActivity.PREFERENCES_DATA, new Gson().toJson(savedMoviesArray)).apply();
        }
    }

    public void getData(){
        Intent intent = getIntent();
        moviesArray = new Gson().fromJson(intent
                .getStringExtra(MainActivity.MOVIE_DATA), new TypeToken<List<Movie>>(){}.getType());
        ActionBar actionBar = getSupportActionBar();
        movieIndex = intent.getIntExtra(MainActivity.MOVIE_INDEX, 0);
        if (actionBar != null) actionBar.setTitle(moviesArray.get(movieIndex).getShowTitle());
        currentIndex = movieIndex;
        shouldSave = intent.getBooleanExtra(MainActivity.MOVIE_SAVE, false);
    }

    public void initializeRecyclerView(){
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.scrollToPosition(movieIndex);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

        private Context context;
        int positionAttached = movieIndex, positionDetached=-1;

        MoviesListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return moviesArray.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            View view = mInflater.inflate(R.layout.detailed_activity_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            positionAttached = holder.getAdapterPosition();
            super.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            positionDetached = holder.getAdapterPosition();
            if(positionAttached != positionDetached) {
                currentIndex = positionAttached;
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) actionBar.setTitle(moviesArray.get(positionAttached).getShowTitle());
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Movie movie = moviesArray.get(position);
            Picasso.with(context)
                    .load(movie.getPoster())
                    .error(R.mipmap.ic_launcher)
                    .into(holder.poster);
            holder.release.setText(movie.getReleaseYear());
            holder.rating.setText(movie.getRating());
            holder.director.setText(movie.getDirector());
            holder.summary.setText(movie.getSummary());
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView poster;
            TextView release;
            TextView rating;
            TextView director;
            TextView summary;

            ViewHolder(View v) {
                super(v);
                poster = (ImageView) v.findViewById(R.id.poster);
                release = (TextView) v.findViewById(R.id.release);
                rating = (TextView) v.findViewById(R.id.rating);
                director = (TextView) v.findViewById(R.id.director);
                summary = (TextView) v.findViewById(R.id.summary);
            }
        }
    }
}
