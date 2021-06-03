package com.example.safetourbcn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String descriptions[];
    int values[];
    String authors[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, String[] descriptions, int[] values, String[] authors) {
        this.context = context;
        this.descriptions = descriptions;
        this.values = values;
        this.authors = authors;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return descriptions.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_listview, null);
        TextView desc = (TextView) view.findViewById(R.id.textView);
        TextView author = (TextView) view.findViewById(R.id.author);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        desc.setText(descriptions[i]);
        author.setText(authors[i]);
        icon.setImageResource(values[i]);
        return view;
    }
}