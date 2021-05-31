package com.joneikholm.listview21spring;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.joneikholm.listview21spring.model.Note;
import com.joneikholm.listview21spring.repo.Repo;

public class DetailActivity extends AppCompatActivity implements TaskListener {

    private ImageView imageView;
    private Bitmap currentBitmap;
    private Note currentNote;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        editText = findViewById(R.id.editText1);
        String noteID = getIntent().getStringExtra("noteid");
        currentNote = Repo.r().getNoteWith(noteID);
        editText.setText(currentNote.getText());
        imageView = findViewById(R.id.imageView2);
    }


    public void save(View view){ // Will also update an existing note.
        imageView.buildDrawingCache(true);
        currentBitmap = Bitmap.createBitmap(imageView.getDrawingCache(true));
        currentNote.setText(editText.getText().toString());
        Repo.r().updateNoteAndImage(currentNote, currentBitmap);
        System.out.println("you pressed save");
        System.out.println("The bitmap size: " + currentBitmap.getByteCount());
    }

    @Override
    public void receive(byte[] bytes) {
        // figure out, how to get the byte array to an image, and from there to the imageView
    }

    public void delete(View view){
        Repo.r().deleteNote(currentNote.getId());
        finish();
    }

}