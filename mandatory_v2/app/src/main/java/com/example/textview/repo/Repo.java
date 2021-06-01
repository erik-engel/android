package com.example.textview.repo;

import android.graphics.Bitmap;

import com.example.textview.TaskListener;
import com.example.textview.Updatable;
import com.example.textview.model.Note;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    public List<Note> notes = new ArrayList<>();
    private final String NOTES = "bucketlist";
    private Updatable activity;

    public static Repo r(){
        return repo;
    }

    public void setup(Updatable a, List<Note> list){
        activity = a;
        notes = list;
        startListener();
    }

    public Note getNoteWith (String id){
        for (Note note : notes){
            if (note.getId().equals(id)){
                return note;
            }
        }
        return null;
    }

    public void startListener(){
        db.collection(NOTES).addSnapshotListener((values, error) -> {
           notes.clear();
            for (DocumentSnapshot snap: values.getDocuments()) {
                Object title = snap.get("title");
                if(title != null) {
                    notes.add(new Note(snap.getId(), title.toString()));
                }
                System.out.println("Snap: " + snap.toString());
            }
            activity.update(null);
        });
    }

    public String addNote(String new_note) {
        DocumentReference  ref = db.collection(NOTES).document();
        Map<String,String> map = new HashMap<>();
        map.put("title", new_note);
        ref.set(map); // will replace any previous value.
        System.out.println("Done inserting new document " + ref.getId());
        return ref.getId();
    }

    public void updateNote(Note note) {
        DocumentReference ref = db.collection(NOTES).document(note.getId());
        Map<String,String> map = new HashMap<>();
        map.put("title", note.getText());
        ref.set(map);
    }

    public void deleteNote(Note note) {
        DocumentReference ref = db.collection(NOTES).document(note.getId());
        ref.delete();

    }

    public void deleteImage(Note note){
        StorageReference ref = storage.getReference(note.getId());
        ref.delete();

    }

    public void updateNoteAndImage(Note note, Bitmap bitmap) {
        updateNote(note);
        System.out.println("uploadBitmap called" + bitmap.getByteCount());
        StorageReference ref = storage.getReference(note.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ref.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("OK to upload" + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("Failure to upload" + exception);
        });
    }

    public void downloadBitmap(String id, TaskListener taskListener){
        StorageReference ref = storage.getReference(id);
        int max = 1024 * 1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            taskListener.receive(bytes);
            System.out.println("Download OK");
        }).addOnFailureListener(ex -> {
            System.out.println("Error in Downalod" + ex);
        });
    }
}
