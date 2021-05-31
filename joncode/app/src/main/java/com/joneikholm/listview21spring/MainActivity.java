package com.joneikholm.listview21spring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.joneikholm.listview21spring.adapter.MyAdapter;
import com.joneikholm.listview21spring.model.Note;
import com.joneikholm.listview21spring.repo.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable {

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
    }

    private void setupAddButton() {
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(e ->{
//            items.add("New note " + items.size());
//            myAdapter.notifyDataSetChanged(); // tell the listView to reload data
            System.out.println("Add Btn pressed");
            Repo.r().addNote("new note");
        });
    }

    private void setupListView() {
        listView = findViewById(R.id.listView1);
        myAdapter = new MyAdapter(items, this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("click on row: " + position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("noteid", items.get(position).getId());
            startActivity(intent);
        });
    }

    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }
}