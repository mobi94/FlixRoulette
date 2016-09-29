package com.netflix.roulette.myclientserverapplication;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class SearchFragment extends Fragment{

    MoviesListAdapter adapter;
    EditText searchBar;
    MyNetflixRoulette myNetflixRoulette;
    JSONArray jsonMoviesArray;
    RecyclerView recyclerView;
    boolean shouldSearchWithTitle;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) actionBar.setTitle("Find movie");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.title_movies_list);
        searchBar = (EditText) rootView.findViewById(R.id.search_box);
        searchBar.addTextChangedListener(new SearchFragment.MyTextWatcher());
        decideWhatToSearch();
        if (shouldSearchWithTitle)
            ((TextInputLayout)rootView.findViewById(R.id.input_layout)).setHint("Enter title of the movie");
        else
            ((TextInputLayout)rootView.findViewById(R.id.input_layout)).setHint("Enter director of the movie");

        myNetflixRoulette = new MyNetflixRoulette();

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

    private void decideWhatToSearch(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shouldSearchWithTitle = bundle.getBoolean(MainActivity.SEARCH_FRAGMENT_TYPE, true);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            SearchFragment.LoadNetflixDataTask loadNetflixDataTask = new SearchFragment.LoadNetflixDataTask(editable.toString());
            loadNetflixDataTask.execute();
        }
    }

    public void initializeRecyclerView(boolean isPortrait){
        recyclerView.setHasFixedSize(true);
        if (jsonMoviesArray != null) {
            adapter = new MoviesListAdapter(getContext(), jsonMoviesArray, true);
            recyclerView.setAdapter(adapter);
        }
        if (isPortrait) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
        else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private class LoadNetflixDataTask extends AsyncTask<Void, Void, Void> {
        String text;

        LoadNetflixDataTask(String text) {
            this.text = text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (shouldSearchWithTitle)
                    jsonMoviesArray = myNetflixRoulette.getMovieByTitle(text);
                else
                    jsonMoviesArray = myNetflixRoulette.getMovieByDirector(text);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (jsonMoviesArray != null) {
                adapter = new MoviesListAdapter(getContext(), jsonMoviesArray, true);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
