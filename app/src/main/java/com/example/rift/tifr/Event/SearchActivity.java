package com.example.rift.tifr.Event;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.example.rift.tifr.Adapter.CustomAdapter;
import com.example.rift.tifr.R;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Event>> {
    public static final String URL = "http://dobby.ddns.net/tifr/fetch.php";
    private static final int EVENT_LOADER_ID = 1;
    //Declare the adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;
    View noInternetScreenView;
    View noDataView;
    View findDataView;
    View loadingScreenView;
    LinearLayoutManager mLayoutManager;
    public  String mQuery;
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CustomAdapter(this, events, 0);
        recyclerView.setHasFixedSize(true);



        /*
        Initialising default Views
         */
        loadingScreenView = findViewById(R.id.loading_screen);
        noInternetScreenView = findViewById(R.id.no_internet_screen);
        noDataView = findViewById(R.id.no_data);
        findDataView = findViewById(R.id.find_data);


        /*
        Getting Connectivity service.
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null && !networkInfo.isConnected()) {
            /*
            This code will work when there is no internet connectivity.
             */
            recyclerView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            findDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            findDataView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public Loader<ArrayList<Event>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("search", mQuery );

        return new EventLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished
            (Loader<ArrayList<Event>> loader, ArrayList<Event> eventsList) {
        if (eventsList.size() != 0) {
            events.clear();
            events.addAll(eventsList);
            //We set the array to the adapter
            adapter.setListContent(events);
            //We in turn set the adapter to the RecyclerView
            recyclerView.setAdapter(adapter);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            findDataView.setVisibility(View.GONE);
        } else {
            loadingScreenView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);
            findDataView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Event>> loader) {
        events.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        setupSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void setupSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setFocusable(true);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Called when query is submitted (by pressing "Search" button on keyboard)
            // Note: empty search queries detected by the SearchView itself and ignored
            @Override
            public boolean onQueryTextSubmit(String query) {
                String newQuery = !TextUtils.isEmpty(query) ? query : null;
                // Don't do anything if the filter hasn't actually changed.
                // Prevents restarting the loader when restoring state.
                if (mQuery == null && newQuery == null) {
                    return true;
                }
                if (mQuery != null && mQuery.equals(newQuery)) {
                    return true;
                }
                events.clear();
                recyclerView.setVisibility(View.GONE);
                loadingScreenView.setVisibility(View.VISIBLE);
                mQuery = query;
                getSupportLoaderManager().restartLoader(EVENT_LOADER_ID, null, SearchActivity.this);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }




}
