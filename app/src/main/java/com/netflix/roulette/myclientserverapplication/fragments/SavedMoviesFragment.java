package com.netflix.roulette.myclientserverapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import com.netflix.roulette.myclientserverapplication.adapters.MoviesListAdapter;
import com.netflix.roulette.myclientserverapplication.R;
import com.netflix.roulette.myclientserverapplication.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SavedMoviesFragment extends Fragment {

    MoviesListAdapter adapter;
    RecyclerView recyclerView;
    List<Movie> moviesArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_movies, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.saved_movies_list);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) actionBar.setTitle("Saved movies");

        moviesArray = new ArrayList<>();
        getSavedMovies();
        initializeRecyclerView(true);
        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeRecyclerView(false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            initializeRecyclerView(true);
        }
    }

    public void initializeRecyclerView(boolean isPortrait) {
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesListAdapter(getContext(), moviesArray, false);
        recyclerView.setAdapter(adapter);
        if (isPortrait) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
        else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void getSavedMovies() {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        moviesArray = new Gson().fromJson(sharedpreferences.getString(MainActivity.PREFERENCES_DATA, "[]"),
                new TypeToken<List<Movie>>(){}.getType());
    }
}