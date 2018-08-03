package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String Guardian_REQUEST_URL = "https://content.guardianapis.com/search?&api-key=1efd1d07-e784-4814-9003-012dd161330b&show-fields=thumbnail&show-tags=contributor";
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

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        NewslistView.setEmptyView(mEmptyStateTextView);

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

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(Guardian_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.

        if (!section.equals(getString(R.string.settings_section_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }

        uriBuilder.appendQueryParameter("order-by", orderBy);

        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new PoliticsLoader(this, uriBuilder.toString());
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

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings_Activity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



