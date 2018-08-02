package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vanessawanner on 01.08.18.
 */

public class PoliticsAdapter extends ArrayAdapter<Politics> {

    public PoliticsAdapter(Activity context, ArrayList<Politics> politics) {
        super(context, 0, politics);
        }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Politics currentNews = getItem(position);
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView TitleTextView = (TextView) listItemView.findViewById(R.id.tvTitle);
        TitleTextView.setText(currentNews.getTitle());

        String originaldate = currentNews.getDate();
        String[] parts = originaldate.split("T");
        String date = parts[0];
        String time = parts[1].substring(0, parts[1].length() - 4);

        TextView DateTextView = (TextView) listItemView.findViewById(R.id.tvDate);
        DateTextView.setText(date);

        TextView TimeTextView = (TextView) listItemView.findViewById(R.id.tvtime);
        TimeTextView.setText(time);

        TextView SectionTextView = (TextView) listItemView.findViewById(R.id.tvSection);
        SectionTextView.setText(currentNews.getSection());

        TextView AuthorTextView = (TextView) listItemView.findViewById(R.id.tvAuthor);
        AuthorTextView.setText(currentNews.getAuthor());

        return listItemView;

    }
}
