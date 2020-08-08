package com.example.sportivemate.model;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
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

public class PostFireBase {
    private static final String POST_COLLECTION = "Posts";

    public static void addPost(final Post post, final PostModel.Listener<Post> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).add(jsonFromPost(post)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference result = task.getResult();
                    if (result != null)
                        post.setId(result.getId());
                    post.setOwnerId(UserModel.instance.getCurrentUserId());
                    if (listener != null) {
                        listener.onComplete(post);
                    }
                } else {
                    listener.onComplete(null);
                }

            }
        });
    }

    public static void getPostById(final String ownerId, final PostModel.Listener<Post> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(ownerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();
                if (res != null && res.getData() != null)
                    listener.onComplete(postFromJson(ownerId, res.getData()));
            }
        });
    }

    public static void updatePost(Post post, final PostModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(post.getId()).set(jsonFromPost(post))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task.isSuccessful());
                    }
                });
    }

    public static void setPostImageUrl(String reportId, String imageUrl, final PostModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(reportId).update("imageUrl", imageUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task.isSuccessful());
                    }
                });
    }


    public static void deletePost(final Post post, final PostModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(post.getId()).update("isDeleted", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete(task.isSuccessful());
                    }
                });
    }

    /*static void getAllSportPostsSince(Sport spot, long since, final PostModel.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp timestamp = new Timestamp(new Date(since));
        db.collection(REPORT_COLLECTION).whereEqualTo("sportName", spot.getName())
                .whereEqualTo("isDeleted", false)
                .whereGreaterThanOrEqualTo("lastUpdated", timestamp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts;
                if (task.isSuccessful()) {
                    posts = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Post report = postFromJson(doc.getId(), json);
                            report.setId(doc.getId());
                            posts.add(report);
                        }
                    listener.onComplete(posts);
                } else {
                    throw new RuntimeException(task.getException());
                }
            }
        });
    }*/

    static void getAllPosts(Sport sport, final PostModel.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).whereEqualTo("sportName", sport.getName())
                .whereEqualTo("isDeleted", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts = null;
                if (task.isSuccessful()) {
                    posts = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Post post = postFromJson(doc.getId(), json);
                            post.setId(doc.getId());
                            posts.add(post);
                        }
                }
                if (listener != null)
                    listener.onComplete(posts);
            }
        });
    }

    static void getAllUserPostsSince(String userId, long since, final PostModel.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp timestamp = new Timestamp(new Date(since));
        db.collection(POST_COLLECTION).whereEqualTo("ownerId", userId)
                //.whereEqualTo("isDeleted", false)
                /*.whereGreaterThanOrEqualTo("lastUpdated", timestamp)*/.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> posts;
                if (task.isSuccessful()) {
                    posts = new LinkedList<>();
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Map<String, Object> json = doc.getData();
                            Post post = postFromJson(doc.getId(), json);
                            post.setId(doc.getId());
                            posts.add(post);
                        }
                    listener.onComplete(posts);
                } else {
                    throw new RuntimeException(task.getException());
                }
            }
        });
    }

    private static Map<String, Object> jsonFromPost(Post post) {
        HashMap<String, Object> json = new HashMap<>();
        json.put("postName", post.getName());
        json.put("imageUrl", post.getImageUrl());
        json.put("city", post.getCity());
        json.put("description", post.getDescription());
        json.put("ownerId", UserModel.instance.getCurrentUserId());
        json.put("sportName", post.getSportName());
        json.put("phoneNumber", post.getPhoneNumber());
        json.put("date", (post.getDate() == 0) ? FieldValue.serverTimestamp() : new Timestamp(new Date(post.getDate())));
        json.put("lastUpdated", FieldValue.serverTimestamp());
        json.put("isDeleted", post.isDeleted());
        return json;
    }

    private static Post postFromJson(String id, Map<String, Object> json) {
        Post post = new Post();
        post.setId(id);
        String ownerId = (String) json.get("ownerId");
        String sportName = (String) json.get("sportName");
        if (ownerId == null || sportName ==null) {
            throw new RuntimeException("Invalid owner id or sport name");
        }
        post.setName((String)json.get("postName"));
        post.setCity((String)json.get("city"));
        post.setImageUrl((String)json.get("imageUrl"));
        post.setDescription((String)json.get("description"));
        post.setPhoneNumber((String)json.get("phoneNumber"));
        post.setOwnerId(ownerId);
        post.setSportName(sportName);
        Timestamp dateTimestamp = (Timestamp) json.get("date");
        if (dateTimestamp != null) post.setDate(dateTimestamp.toDate().getTime());
        Timestamp lastUpdatedTimestamp = (Timestamp) json.get("lastUpdated");
        if (lastUpdatedTimestamp != null)
            post.setLastUpdated(lastUpdatedTimestamp.toDate().getTime());
        post.setDeleted((boolean) json.get("isDeleted"));
        return post;
    }
}
