package com.example.sportivemate.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sportivemate.MyApp;

@Database(entities = {Sport.class}, version = 4)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract SportDao sportDao();
    public  abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApp.context,
            AppLocalDbRepository.class, "sportivemateDb.db")
            .fallbackToDestructiveMigration().build();
}
