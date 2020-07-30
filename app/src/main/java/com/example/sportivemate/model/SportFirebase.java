package com.example.sportivemate.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SportFirebase {
    private final static String SPORT_COLLECTION = "Sports";

    public static void addSport(Sport sport, final SportModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SPORT_COLLECTION).document(sport.getName()).set(jsonFromSport(sport)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    static void getAllSports(final SportModel.Listener<List<Sport>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SPORT_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Sport> sports = null;
                if (task.isSuccessful()) {
                    sports = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Sport sport = sportFromJson(json);
                            sports.add(sport);
                        }
                }
                listener.onComplete(sports);
            }
        });
    }

    static void getAllSportsSince(long since, final SportModel.Listener<List<Sport>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp timestamp = new Timestamp(new Date(since));
        db.collection(SPORT_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", timestamp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Sport> sports = null;
                if (task.isSuccessful()) {
                    sports = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Sport sport = sportFromJson(json);
                            sports.add(sport);
                        }
                }
                listener.onComplete(sports);
            }
        });
    }

    private static Map<String, Object> jsonFromSport(Sport sport) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("name", sport.getName());
        json.put("imageUrl", sport.getImageUrl());
        json.put("description", sport.getDescription());
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }

    private static Sport sportFromJson(Map<String, Object> json) {
        Sport sport = new Sport();
        String name = (String)json.get("name");
        if ((name != null)) {
            sport.setName(name);
        } else {
            sport.setName("");
        }
        sport.setImageUrl((String) json.get("imageUrl"));
        sport.setDescription((String) json.get("description"));
        Timestamp timestamp = (Timestamp) json.get("lastUpdated");
        if (timestamp != null) sport.setLastUpdated(timestamp.toDate().getTime());
        return sport;
    }

    public static void getSportByName(final String sportName, final SportModel.Listener<Sport> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SPORT_COLLECTION).document(sportName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();
                if (res != null && res.getData()!=null)
                    listener.onComplete(sportFromJson(res.getData()));
            }
        });
    }
}