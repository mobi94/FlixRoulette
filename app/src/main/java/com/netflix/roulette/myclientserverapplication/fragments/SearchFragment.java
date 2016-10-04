package com.netflix.roulette.myclientserverapplication.fragments;

import android.content.res.Configuration;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import com.netflix.roulette.myclientserverapplication.adapters.MoviesListAdapter;
import com.netflix.roulette.myclientserverapplication.R;
import com.netflix.roulette.myclientserverapplication.activities.MainActivity;
import com.netflix.roulette.myclientserverapplication.presenter.PresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements com.netflix.roulette.myclientserverapplication.view.View{

    MoviesListAdapter adapter;
    EditText searchBar;
    RecyclerView recyclerView;
    boolean shouldSearchWithTitle;

    PresenterImpl presenterImpl;
    List<Movie> moviesArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) actionBar.setTitle("Find movie");

        moviesArray = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.title_movies_list);
        searchBar = (EditText) rootView.findViewById(R.id.search_box);
        searchBar.addTextChangedListener(new SearchFragment.MyTextWatcher());
        decideWhatToSearch();
        if (shouldSearchWithTitle)
            ((TextInputLayout)rootView.findViewById(R.id.input_layout)).setHint("Enter title of the movie");
        else
            ((TextInputLayout)rootView.findViewById(R.id.input_layout)).setHint("Enter director of the movie");

        presenterImpl = new PresenterImpl(this);

        initializeRecyclerView(true, moviesArray);

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeRecyclerView(false, moviesArray);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            initializeRecyclerView(true, moviesArray);
        }
    }

    @Override
    public void showMovie(List<Movie> movies) {
        moviesArray = movies;
        updateRecyclerView();
    }

    @Override
    public void showError(String error) {
        //Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        Log.d(getClass().getName(), error);
    }

    @Override
    public void showEmptyList() {
        //Toast.makeText(getContext(), "Movies not found", Toast.LENGTH_LONG).show();
        Log.d(getClass().getName(), "Movies not found");
    }

    @Override
    public boolean getType() {
        return shouldSearchWithTitle;
    }

    @Override
    public void onComplete() {
        updateRecyclerView();
    }

    private void decideWhatToSearch(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shouldSearchWithTitle = bundle.getBoolean(MainActivity.SEARCH_FRAGMENT_TYPE, true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenterImpl != null) {
            presenterImpl.onStop();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            presenterImpl.onTextChanged(editable.toString());
        }
    }

    public void updateRecyclerView(){
        adapter.setNewData(moviesArray);
    }

    public void initializeRecyclerView(boolean isPortrait, List<Movie> moviesArray){
        recyclerView.setHasFixedSize(true);
        adapter = new MoviesListAdapter(getContext(), moviesArray, true);
        recyclerView.setAdapter(adapter);
        if (isPortrait) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
        else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
