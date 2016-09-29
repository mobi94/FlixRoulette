package com.netflix.roulette.myclientserverapplication;

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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsModeActivity extends AppCompatActivity {

    MoviesListAdapter adapter;
    RecyclerView recyclerView;
    JSONArray jsonMoviesArray;
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
            JSONArray savedJSONArray = new JSONArray();
            boolean isIncluded = false;
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
            try {
                savedJSONArray = new JSONArray(sharedpreferences.getString(MainActivity.PREFERENCES_DATA, "[]"));
                for (int i = 0; i <= savedJSONArray.length(); i++) {
                    if (savedJSONArray.getJSONObject(i).getString("show_title").
                            equals(jsonMoviesArray.getJSONObject(currentIndex).getString("show_title"))) {
                        isIncluded = true;
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {
                if (!isIncluded) {
                    try {
                        savedJSONArray.put(jsonMoviesArray.getJSONObject(currentIndex));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sharedpreferences.edit().putString(MainActivity.PREFERENCES_DATA, savedJSONArray.toString()).apply();
                }
            }
            return true;
        }
        else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(){
        Intent intent = getIntent();
        try {
            jsonMoviesArray = new JSONArray(intent.getStringExtra(MainActivity.MOVIE_DATA));
            ActionBar actionBar = getSupportActionBar();
            movieIndex = intent.getIntExtra(MainActivity.MOVIE_INDEX, 0);
            if (actionBar != null) actionBar.setTitle((String)jsonMoviesArray.getJSONObject(movieIndex).get("show_title"));
            currentIndex = movieIndex;
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            return jsonMoviesArray.length();
        }

        @Override
        public MoviesListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            View view = mInflater.inflate(R.layout.detailed_activity_list_item, parent, false);
            return new MoviesListAdapter.ViewHolder(view);
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
                String title = "";
                try {
                    title = (String) jsonMoviesArray.getJSONObject(positionAttached).get("show_title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) actionBar.setTitle(title);
            }
        }

        @Override
        public void onBindViewHolder(MoviesListAdapter.ViewHolder holder, int position) {
            try {
                JSONObject currentMovie = jsonMoviesArray.getJSONObject(position);
                Picasso.with(context)
                        .load((String) currentMovie.get("poster"))
                        .error(R.mipmap.ic_launcher)
                        .into(holder.poster);
                holder.release.setText((String) currentMovie.get("release_year"));
                holder.rating.setText((String) currentMovie.get("rating"));
                holder.director.setText((String) currentMovie.get("director"));
                holder.summary.setText((String) currentMovie.get("summary"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
