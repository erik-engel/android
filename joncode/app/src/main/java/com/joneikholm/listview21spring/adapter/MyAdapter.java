package com.joneikholm.listview21spring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joneikholm.listview21spring.R;
import com.joneikholm.listview21spring.model.Note;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Note> items; // will hold data
    private LayoutInflater layoutInflater; // can "inflate" layout files
    public MyAdapter(List<Note> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // make layout .xml file first...
        if(view == null){
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
       // LinearLayout linearLayout = (LinearLayout)view;
        TextView textView = view.findViewById(R.id.textView1);
        if(textView != null) {
            textView.setText(items.get(i).getText()); // later I will connect to the items list
        }
        return textView;
    }
}















