package com.testconcept.android_test.Persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.testconcept.android_test.InterfaceItems.UserDao;


@Database(entities = {Post.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
