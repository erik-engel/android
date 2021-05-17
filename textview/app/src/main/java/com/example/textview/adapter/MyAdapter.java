package com.example.textview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.textview.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<String> items; // will hold data
    private LayoutInflater layoutInflater; // can "inflate" layout files

    public MyAdapter(List<String> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // make layout .xml file first ...
//
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.myrow, null);
        }
//        LinearLayout linearLayout = (LinearLayout)convertView;
        TextView textView = convertView.findViewById(R.id.textView1);
        if (textView != null) {
            textView.setText(items.get(position)); // later I will connect to the items list
        }
        return textView;
    }
}
