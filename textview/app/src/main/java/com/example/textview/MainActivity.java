package com.example.textview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.textview.adapter.MyAdapter;
import com.example.textview.model.Note;
import com.example.textview.repo.Repo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable{

    List<Note> items = new ArrayList<>();
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
            String id = Repo.r().addNote("Empty Snap");
            System.out.println(id);
            Intent intent = new Intent(listView.getContext(), DetailActivity.class);
            intent.putExtra("noteid", id);

            startActivity(intent);
        });
    }

    public void setupListView() {
        listView = findViewById(R.id.listView1);
        myAdapter = new MyAdapter(items, this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
//            System.out.println(items.get(position));
//            System.out.println(view.getContext().getClass().getName());
//            System.out.println(this.getClass().getName());
            Intent myIntent = new Intent(view.getContext(), DetailActivity.class);
            myIntent.putExtra("noteid", items.get(position).getId());

            startActivity(myIntent);

        });
    }

    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }
}