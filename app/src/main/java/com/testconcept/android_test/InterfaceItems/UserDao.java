package com.testconcept.android_test.InterfaceItems;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.testconcept.android_test.Persistence.Post;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from Post")
    List<Post> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post posts);

    @Delete
    void delete(Post posts);

    @Query("Delete from Post")
    void deleteAllNotes();

    @Update
    void UpdateFavorite(Post posts);

    @Update
    void UpdateVisto(Post posts);

    @Query("Select * from Post where favorite = 0")
    List<Post> getAllFavorites();

}