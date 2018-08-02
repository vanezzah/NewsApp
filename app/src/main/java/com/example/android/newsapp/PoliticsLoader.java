package com.example.android.newsapp;

import android.content.Context;

import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by vanessawanner on 01.08.18.
 */

public class PoliticsLoader extends AsyncTaskLoader<List<Politics>> {

    private static final String LOG_TAG = PoliticsLoader.class.getName();
    private String mUrl;

    public PoliticsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Politics> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Politics> politics = QueryUtils.fetchPoliticsData(mUrl);
        return politics;
    }
}
