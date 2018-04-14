package com.example.rift.tifr.Event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.rift.tifr.R;
import com.example.rift.tifr.Adapter.CustomAdapter;
import com.example.rift.tifr.data.DatabaseHelper;
import java.util.ArrayList;


public class YourListFragment extends Fragment {
    private static final String TAG = "YourListFragment";
    //Declare the adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;
    private ArrayList<Event> events;
    DatabaseHelper mDatabaseHelper;
    View noDataView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_list_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        events = mDatabaseHelper.getData();
        adapter = new CustomAdapter(getActivity(),events,1);
        recyclerView.setAdapter(adapter);
        noDataView = getView().findViewById(R.id.no_data);

        if (events.size()==0) {
            recyclerView.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);

        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
