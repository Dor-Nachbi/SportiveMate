package com.example.sportivemate.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.sportivemate.MyApp;

import java.util.List;

public class SportModel {
    public static final SportModel instance = new SportModel();

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface CompleteListener {
        void onComplete();
    }

    public LiveData<Sport> getSport(String sportName) {
        LiveData<Sport> sportLiveData = AppLocalDb.db.sportDao().getSport(sportName);
        refreshSportDetails(sportName);
        return sportLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshSportDetails(String sportName) {
        SportFirebase.getSportByName(sportName, new Listener<Sport>() {
            @Override
            public void onComplete(final Sport data) {
                if (data != null) {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            AppLocalDb.db.sportDao().insertAll(data);
                            return null;
                        }
                    }.execute();
                }
            }
        });
    }

    public LiveData<List<Sport>> getAllSports() {
        LiveData<List<Sport>> liveData = AppLocalDb.db.sportDao().getAll();
        initializeSportsList();
        return liveData;
    }

    public void initializeSportsList() {
        SportFirebase.getAllSports(new Listener<List<Sport>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Sport> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for (Sport sport : data) {
                            AppLocalDb.db.sportDao().insertAll(sport);
                            if (sport.getLastUpdated() > lastUpdated)
                                lastUpdated = sport.getLastUpdated();
                        }
                        SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE).edit();
                        editor.putLong("SportsLastUpdateDate", lastUpdated).apply();
                        return "";
                    }
                }.execute();
            }
        });
    }

    public void refreshSportsList(final CompleteListener listener) {
        long lastUpdated = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE)
                .getLong("SportsLastUpdateDate", 0);
        SportFirebase.getAllSportsSince(lastUpdated, new Listener<List<Sport>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Sport> data) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for (Sport sport : data) {
                            AppLocalDb.db.sportDao().insertAll(sport);
                            if (sport.getLastUpdated() > lastUpdated)
                                lastUpdated = sport.getLastUpdated();
                        }
                        SharedPreferences.Editor editor = MyApp.context.getSharedPreferences("lastUpdated", Context.MODE_PRIVATE).edit();
                        editor.putLong("SportsLastUpdateDate", lastUpdated).apply();
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
}
