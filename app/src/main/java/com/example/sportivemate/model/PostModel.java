package com.example.sportivemate.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.sportivemate.MyApp;

import java.util.List;

public class PostModel {

    public static final PostModel instance = new PostModel();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface CompleteListener {
        void onComplete();
    }

    public LiveData<List<Post>> getAllPosts(Sport sport) {
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getSportPosts((sport.getName()));
        refreshSportPostsList(sport, null);
        return liveData;
    }

    public void refreshSportPostsList(final Sport sport, final CompleteListener listener) {
        long lastUpdated = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE)
                .getLong("ReportsLastUpdateDate", 0);
        PostFireBase.getAllSportPostsSince(sport, lastUpdated, new Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        if (data != null) {
                            for (Post post : data) {
                                AppLocalDb.db.postDao().insertAll(post);
                                if (post.getLastUpdated() > lastUpdated)
                                    lastUpdated = post.getLastUpdated();
                            }
                            SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE).edit();
                            editor.putLong("PostsLastUpdateDate", lastUpdated).commit();
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null) listener.onComplete();
                    }
                }.execute();

            }
        });
    }

    public LiveData<List<Post>> getUserPosts(String userId) {
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getUserPosts(userId);
        refreshUserPostsList(userId, null);
        return liveData;
    }

    public void refreshUserPostsList(String userId, final CompleteListener listener) {
        long lastUpdated = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE)
                .getLong("PostsLastUpdateDate", 0);
        PostFireBase.getAllUserPostsSince(userId, lastUpdated, new Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        if (data != null) {
                            for (Post post : data) {
                                AppLocalDb.db.postDao().insertAll(post);
                                if (post.getLastUpdated() > lastUpdated)
                                    lastUpdated = post.getLastUpdated();
                            }
                            SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE).edit();
                            editor.putLong("PostsLastUpdateDate", lastUpdated).commit();
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener != null) listener.onComplete();
                    }
                }.execute();
            }
        });
    }

    public LiveData<Post> getPost(String Id) {
        LiveData<Post> postLiveData = AppLocalDb.db.postDao().getPost(Id);
        refreshPostDetails(Id);
        return postLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshPostDetails(String Id) {
        PostFireBase.getPostById(Id, new Listener<Post>() {
            @Override
            public void onComplete(final Post data) {
                if (data != null) {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            AppLocalDb.db.postDao().insertAll(data);
                            return null;
                        }
                    }.execute("");
                }
            }
        });
    }

    public void addPost(final Post post, final Listener<Post> listener) {
        PostFireBase.addPost(post, listener);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateReport(final Post post, final Listener<Boolean> listener) {
        PostFireBase.updatePost(post, new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data) {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            AppLocalDb.db.postDao().insertAll(post);
                            return null;
                        }
                    }.execute("");
                }
                listener.onComplete(data);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void setPostImageUrl(String Id, String imageUrl, final Listener<Boolean> listener) {
        PostFireBase.setPostImageUrl(Id, imageUrl, listener);
    }


    @SuppressLint("StaticFieldLeak")
    public void deletePost(final Post post, Listener<Boolean> listener) {
        PostFireBase.deletePost(post, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().delete(post);
                return null;
            }
        }.execute();

    }


}
