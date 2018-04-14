package com.example.rift.tifr.Event;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


class EventExtractData {

    /**
     * This function initiates the process of making HTTP connection and returns ArrayList of events.
     *
     * @param stringUrl
     * @return
     */

    static ArrayList<Event> initiateConnection(String stringUrl) {
        String jsonResponse = "";
        URL url = getURL(stringUrl);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("EventAsyncTask", "Error establishing Connection!!!");
        }
        return extractFromJson(jsonResponse);
    }

    /**
     * This function take a string url and the URL Object.
     *
     * @param stringUrl
     * @return
     */
    private static URL getURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("EventExtractData", "URL Exception => Not able to convert to url object.");
        }
        return url;
    }

    /**
     * Returns jsonresponse in String format.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("EventExtractData", "Error response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("EventExtractData", "Error IOExeception");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return String from inputstream
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return arrayList of event Objects.
     *
     * @param jsonData
     * @return
     */
    private static ArrayList<Event> extractFromJson(String jsonData) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            JSONObject baseObject = new JSONObject(jsonData);
            JSONArray featuresArray = baseObject.getJSONArray("results");
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject sectionObj = (JSONObject) featuresArray.get(i);
                JSONArray sectionsArray = sectionObj.getJSONArray("section");
                for (int j = 0; j < sectionsArray.length(); j++) {
                    JSONObject arrayObject = sectionsArray.optJSONObject(j);
                    events.add(new Event(arrayObject.optString("id"), arrayObject.optString("title"),
                            arrayObject.optString("start_date"), arrayObject.optString("start_time"),
                            arrayObject.optString("description"), arrayObject.optString("venue"),
                            arrayObject.optString("url"), arrayObject.optString("image"),
                            arrayObject.optString("end_date"), arrayObject.optString("end_time")));
                }
            }
        } catch (JSONException e) {
            Log.e("EventExtractData", "JSON data extract error.");
        }
        return events;
    }

}
