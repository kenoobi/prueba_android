package com.testconcept.android_test.Persistence;

//import android.arch.persistence.romm.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Post {

    private int userId;

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "body")
    private String body;
    @ColumnInfo(name = "favorite")
    private boolean favorite;
    @ColumnInfo(name = "view")
    private boolean visto;

    public Post(int i, int id, String title, String body, int favorite, int visto){

    }

    public Post(int userId, int id, String title, String body, boolean favorite, boolean visto) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.favorite = favorite;
        this.visto = visto;
    }


    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public boolean getVisto() {
        return visto;
    }

    // Sets

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean setFavorite(boolean favorite) {
        this.favorite = favorite;
        return favorite;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }
}

