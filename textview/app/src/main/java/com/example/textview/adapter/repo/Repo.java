package com.example.textview.adapter.repo;

import android.widget.LinearLayout;

import com.example.textview.Updatable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String> notes = new ArrayList<>();
    private final String BL = "bucketlist";
    private Updatable activity;

    public static Repo r(){
        return repo;
    }

    public void setup(Updatable a, List<String> list){
        activity = a;
        notes = list;
        startListener();
    }

    public void startListener(){
        db.collection(BL).addSnapshotListener((values, error) -> {
           notes.clear();
            for (DocumentSnapshot snap: values.getDocuments()) {
                Object title = snap.get("title");
                if(title != null) {
                    notes.add(title.toString());
                }
                System.out.println("Snap: " + snap.toString());
            }
            activity.update(null);
        });
    }

    public void addNote(String new_note) {
        DocumentReference  ref = db.collection(BL).document();
        Map<String,String> map = new HashMap<>();
        map.put("title", new_note);
        ref.set(map); // will replace any previous value.
        System.out.println("Done inserting new document " + ref.getId());
    }

}
