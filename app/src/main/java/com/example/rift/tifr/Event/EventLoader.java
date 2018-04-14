package com.example.rift.tifr.Event;


import android.content.Context;
import java.util.ArrayList;

public class EventLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<Event>> {

    private String mUrl;

    EventLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Event> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return EventExtractData.initiateConnection(mUrl);
    }
}
