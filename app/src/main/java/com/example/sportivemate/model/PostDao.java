package com.example.sportivemate.model;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Query("SELECT * FROM Posts WHERE sportName LIKE :name")
    LiveData<List<Post>> getSportPosts(String name);

    @Query("SELECT * FROM Posts WHERE ownerId LIKE :userId")
    LiveData<List<Post>> getUserPosts(String userId);

    @Query("SELECT * FROM Posts WHERE id LIKE :Id")
    LiveData<Post> getPost(String Id);

    @Delete
    void delete(Post post);
}
