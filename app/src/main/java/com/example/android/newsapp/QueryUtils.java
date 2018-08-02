package com.example.android.newsapp;

import android.app.DownloadManager;
import android.text.TextUtils;
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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vanessawanner on 01.08.18.
 */
// Helper method to retrieve News data
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Politics> fetchPoliticsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Politics> politics = extractFeatureFromJson(jsonResponse);
        // Return the {@link Event}
        return politics;
    }

    private static List<Politics> extractFeatureFromJson(String politicsJSON) {
        if (TextUtils.isEmpty(politicsJSON)) {
            return null;
        }

        List<Politics> politics = new ArrayList<>();
        try {
            JSONObject rawJsonResponse = new JSONObject(politicsJSON);
            JSONObject politicsList = rawJsonResponse.getJSONObject("response");
            JSONArray politicsArray = politicsList.getJSONArray("results");
            for (int i = 0; i < politicsArray.length(); i++) {
                JSONObject currentResult = politicsArray.getJSONObject(i);
                String title = currentResult.getString("webTitle");
                String date = currentResult.getString("webPublicationDate");
                String url = currentResult.getString("webUrl");
                String section = currentResult.getString("sectionName");
                JSONArray tags = currentResult.getJSONArray("tags");
                String author;
                if (tags.length() != 0) {
                    JSONObject tagsObject = tags.getJSONObject(0);
                    author = tagsObject.getString("webTitle");
                } else author = "No author";
                Politics result = new Politics(title, date, url, section, author);
                politics.add(result);

            }

            } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);
        }
        // Return the list of earthquakes
        return politics;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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

}
