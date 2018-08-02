package com.example.android.newsapp;

/**
 * Created by vanessawanner on 01.08.18.
 */

public class Politics {

    //variables
    private String mTitle;
    private String mDate;
    private String mUrl;
    private String mSection;
    private String mAuthor;

    // constructor
    public Politics(String title, String date, String url, String section, String author) {
        mTitle = title;
        mDate = date;
        mUrl = url;
        mSection = section;
        mAuthor = author;

    }
    // Methods
    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
