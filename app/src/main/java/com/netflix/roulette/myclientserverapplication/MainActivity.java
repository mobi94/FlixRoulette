package com.netflix.roulette.myclientserverapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SavedMoviesFragment savedMoviesFragment;
    SearchFragment searchWithTitleFragment, searchWithDirectorFragment;

    public final static String SEARCH_FRAGMENT_TYPE = "SEARCH_FRAGMENT_TYPE";

    public final static String MOVIE_DATA = "MOVIE_DATA";
    public final static String MOVIE_INDEX = "MOVIE_INDEX";
    public final static String MOVIE_SAVE = "MOVIE_SAVE";

    public final static String MY_PREFERENCES = "MY_PREFERENCES";
    public final static String PREFERENCES_DATA = "PREFERENCES_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "See my app \"Screen Off Widget\"", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sergeystasyuk.screenoffwidget"));
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_saved_movies);

        savedMoviesFragment = new SavedMoviesFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(SEARCH_FRAGMENT_TYPE, true);
        searchWithTitleFragment = new SearchFragment();
        searchWithTitleFragment.setArguments(bundle);

        bundle = new Bundle();
        bundle.putBoolean(SEARCH_FRAGMENT_TYPE, false);
        searchWithDirectorFragment = new SearchFragment();
        searchWithDirectorFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_main, savedMoviesFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_saved_movies) {
            fragmentManager.beginTransaction().replace(R.id.content_main, savedMoviesFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        } else if (id == R.id.nav_search_with_title) {
            fragmentManager.beginTransaction().replace(R.id.content_main, searchWithTitleFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        } else if (id == R.id.nav_search_with_director) {
            fragmentManager.beginTransaction().replace(R.id.content_main, searchWithDirectorFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
