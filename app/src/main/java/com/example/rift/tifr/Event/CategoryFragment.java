package com.example.rift.tifr.Event;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rift.tifr.Adapter.CategoryAdapter;
import com.example.rift.tifr.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class CategoryFragment extends Fragment {
    public static final String URL = "http://dobby.ddns.net/tifr/parsed.json";
    private static final String TAG = "CategoryFragment";
    private static final int Event_LOADER_ID = 1;
    //Declare the adapter, RecyclerView and our custom ArrayList
    List<Category> categories;
    View noInternetScreenView;
    View noDataView;
    View connectionErrorView;
    //public static final String URL = "";
    View loadingScreenView;
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        categories = new ArrayList<Category>();
        /*
        Initialising default Views
         */
        loadingScreenView = getView().findViewById(R.id.loading_screen);
        noInternetScreenView = getView().findViewById(R.id.no_internet_screen);
        noDataView = getView().findViewById(R.id.no_data);
        connectionErrorView = getView().findViewById(R.id.connection_error);
        recyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);


        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);

        /*
        Getting Connectivity service.
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            testWebService(URL);
        } else {
            /*
            This code will work when there is no internet connectivity.
             */
            recyclerView.setVisibility(View.GONE);
            loadingScreenView.setVisibility(View.GONE);
            noDataView.setVisibility(View.GONE);
            connectionErrorView.setVisibility(View.GONE);
            noInternetScreenView.setVisibility(View.VISIBLE);
        }

        /*
        * * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * * performs a swipe-to-refresh gesture.
        * */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(CategoryFragment.this).attach(CategoryFragment.this).commit();
            }
        });

    }


    public void testWebService(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (statusCode == 200 && response != null) {
                    Log.i("response-", response.toString());


                    try {
                        JSONArray dataArary = response.getJSONArray("results");
                        for (int i = 0; i < dataArary.length(); i++) {
                            JSONObject sectionObj = (JSONObject) dataArary.get(i);
                            String title = sectionObj.getString("title");
                            ArrayList<Event> events = new ArrayList<Event>();
                            JSONArray sectionsArray = sectionObj.getJSONArray("section");
                            for (int j = 0; j < sectionsArray.length(); j++) {
                                JSONObject arrayObject = sectionsArray.optJSONObject(j);
                                events.add(new Event(arrayObject.optString("id"), arrayObject.optString("title"),
                                        arrayObject.optString("start_date"), arrayObject.optString("start_time"),
                                        arrayObject.optString("description"), arrayObject.optString("venue"),
                                        arrayObject.optString("url"), arrayObject.optString("image"),
                                        arrayObject.optString("end_date"), arrayObject.optString("end_time")));
                            }
                            if (events.size() > 0) {
                                Category category = new Category();
                                category.setTitle(title);
                                category.setEvent(events);
                                categories.add(category);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
                    }

                    // setting data to RecyclerView

                    if (categories != null) {
                        //We set the array to the adapter
                        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        //We in turn set the adapter to the RecyclerView
                        recyclerView.setAdapter(adapter);
                        loadingScreenView.setVisibility(View.GONE);
                        noDataView.setVisibility(View.GONE);
                        noInternetScreenView.setVisibility(View.GONE);
                        connectionErrorView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        loadingScreenView.setVisibility(View.GONE);
                        noInternetScreenView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        connectionErrorView.setVisibility(View.GONE);
                        noDataView.setVisibility(View.VISIBLE);

                    }
                } else {
                    Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    loadingScreenView.setVisibility(View.GONE);
                    connectionErrorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                loadingScreenView.setVisibility(View.GONE);
                connectionErrorView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                loadingScreenView.setVisibility(View.GONE);
                connectionErrorView.setVisibility(View.VISIBLE);
            }
        });
    }


}
