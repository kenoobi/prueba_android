package com.testconcept.android_test.InterfaceItems;

import com.testconcept.android_test.Persistence.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonApi {

    @GET("posts") // terminacion de la ruta "posts"
    Call<List<Post>> getPosts();
}
