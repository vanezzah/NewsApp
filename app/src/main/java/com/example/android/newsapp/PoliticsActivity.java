package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.content.AsyncTaskLoader;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.app.DownloadManager;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.AsyncTaskLoader;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PoliticsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Politics>> {

    private static final int NEWS_LOADER_ID = 1;

    private static final String LOG_TAG = PoliticsActivity.class.getName();

    /**
     * URL for the query
     */
    private static final String Guardian_REQUEST_URL = "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&q=politics&api-key=1efd1d07-e784-4814-9003-012dd161330b";
    //

    private PoliticsAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView NewslistView = (ListView) findViewById(R.id.list);

        mAdapter = new PoliticsAdapter(this, new ArrayList<Politics>());
        NewslistView.setAdapter(mAdapter);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty st ate with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        NewslistView.setEmptyView(mEmptyStateTextView);
        NewslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Politics currentNews = mAdapter.getItem(position);

                Uri NewsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);

                startActivity(websiteIntent);
            }

        });

    }

    @Override
    public Loader<List<Politics>> onCreateLoader(int i, Bundle bundle) {
        return new PoliticsLoader(this, Guardian_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Politics>> loader, List<Politics> politics) {
        mAdapter.clear();
        mEmptyStateTextView.setText(R.string.no_news);
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        if (politics != null && !politics.isEmpty()) {
            mAdapter.addAll(politics);
            }
    }
    @Override
    public void onLoaderReset(Loader<List<Politics>> loader) {
        mAdapter.clear();
    }
}



