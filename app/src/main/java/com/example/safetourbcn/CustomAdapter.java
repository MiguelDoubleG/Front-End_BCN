package com.example.safetourbcn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String descriptions[];
    int values[];
    String authors[];
    String boxes[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, String[] descriptions, int[] values, String[] authors, String[] boxes) {
        this.context = context;
        this.descriptions = descriptions;
        this.values = values;
        this.authors = authors;
        this.boxes = boxes;
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
        boolean a = true;
        view = inflater.inflate(R.layout.activity_listview, null);
        TextView desc = (TextView) view.findViewById(R.id.textView);
        TextView author = (TextView) view.findViewById(R.id.author);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        CheckBox check1 = (CheckBox) view.findViewById(R.id.box1);
        CheckBox check2 = (CheckBox) view.findViewById(R.id.box2);
        CheckBox check3 = (CheckBox) view.findViewById(R.id.box3);
        CheckBox check4 = (CheckBox) view.findViewById(R.id.box4);
        if(boxes[i].charAt(0) == '1')
            check1.setChecked(a);
        if(boxes[i].charAt(2) == '1')
            check2.setChecked(a);
        if(boxes[i].charAt(4) == '1')
            check3.setChecked(a);
        if(boxes[i].charAt(6) == '1')
            check4.setChecked(a);
        desc.setText(descriptions[i]);
        author.setText(authors[i]);
        icon.setImageResource(values[i]);
        return view;
    }
}