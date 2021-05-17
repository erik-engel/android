package com.example.textview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.textview.adapter.MyAdapter;
import com.example.textview.adapter.repo.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable{

    List<String> items = new ArrayList<>(Arrays.asList("How to prepare Salmon", "Remember Joe's Bday", "What to Netflix?"));
    ListView listView;
    MyAdapter myAdapter;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupListView();
        setupAddButton();
        Repo.r().setup(this, items);
        //Repo.r().prepareRepo("new note");

    }

    public void setupAddButton() {
        addButton = findViewById(R.id.addButton);
        // following could be done it it's own method, withouth the lambda
        addButton.setOnClickListener(e -> {
//            items.add("New note " + (items.size() +1));
//            myAdapter.notifyDataSetChanged(); // tell the listView to reload data
            System.out.println("add button pressed");
            Repo.r().addNote("new note");
        });
    }

    public void setupListView() {
        listView = findViewById(R.id.listView1);
        myAdapter = new MyAdapter(items, this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println(items.get(position));
            System.out.println(view.getContext().getClass().getName());
            System.out.println(this.getClass().getName());
            Intent myIntent = new Intent(view.getContext(), DetailActivity.class);
            myIntent.putExtra("note", items.get(position));

            startActivity(myIntent);

        });
    }

    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }
}