package com.example.rift.tifr.Event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rift.tifr.Adapter.CustomAdapter;
import com.example.rift.tifr.MainActivity;
import com.example.rift.tifr.R;
import com.example.rift.tifr.SettingsActivity;

import java.util.ArrayList;


public class EventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Event>> {
    public static final String URL = "http://dobby.ddns.net/tifr/fetch.php";
    public static final String FROM_PREF = "FROM_PREF";
    public static final String TO_PREF = "TO_PREF";
    private static final String TAG = "EventsFragment";
    private static final int EVENT_LOADER_ID = 1;
    private static int count=0;
    private static int CODE = 1; //declare as FIELD
    //Declare the adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;
    View noInternetScreenView;
    View noDataView;
    View loadingScreenView;
    LinearLayoutManager mLayoutManager;
    private ArrayList<Event> events = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CustomAdapter(getActivity(), events, 0);
        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);



        /*
        Initialising default Views
         */
        loadingScreenView = getView().findViewById(R.id.loading_screen);
        noInternetScreenView = getView().findViewById(R.id.no_internet_screen);
        noDataView = getView().findViewById(R.id.no_data);


        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);

        /*
        Getting Connectivity service.
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EVENT_LOADER_ID, null, this);

        } else {
            /*
            This code will work when there is no internet connectivity.
             */
            recyclerView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.VISIBLE);
        }

        /*
        * * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * * performs a swipe-to-refresh gesture.
        * */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  startActivity(new Intent(getActivity(), MainActivity.class)); //reload MainActivity
                //  getActivity().finish();
                getFragmentManager().beginTransaction().detach(EventsFragment.this).attach(EventsFragment.this).commit();
            }
        });
    }


    @Override
    public Loader<ArrayList<Event>> onCreateLoader(int id, Bundle args) {
        //Create a preference file
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(count==0)
        {
           settings.edit().clear().apply();
           count++;
        }
        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("from", settings.getString(FROM_PREF, "today"));
        uriBuilder.appendQueryParameter("to", settings.getString(TO_PREF, "7days"));

        return new EventLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Event>> loader, ArrayList<Event> eventsList) {
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
        } else {
            loadingScreenView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Event>> loader) {
        events.clear();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getActivity(), SettingsActivity.class), CODE);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //SecondActivity closed
        if (requestCode == CODE) {
            events.clear();
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(EVENT_LOADER_ID, null, this);

        }
    }

}
