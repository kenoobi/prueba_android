package com.testconcept.android_test;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.testconcept.android_test.ConnectionItems.ConnectionDetector;
import com.testconcept.android_test.CustomItems.CustomAdapter;
import com.testconcept.android_test.CustomItems.CustomDetalle;
import com.testconcept.android_test.InterfaceItems.JsonApi;
import com.testconcept.android_test.Persistence.AppDatabase;
import com.testconcept.android_test.Persistence.Post;
import com.testconcept.android_test.PreferenceItems.Preference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CustomAdapter.IonPortListener{
    //Enlace a datos

    ArrayList<Post> datos = new ArrayList<>();
    //ArrayList<Post> filtroFavorite = new ArrayList<>();
    RecyclerView recycler;
    CustomAdapter adapter = new CustomAdapter(datos,this, this);
    AppDatabase db;
    ConnectionDetector cd = new ConnectionDetector(this);
    Preference pf = new Preference(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "Datos")
                .allowMainThreadQueries()
                .build();

        // Reciclador de vistas
        recycler = findViewById(R.id.RecyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pf.savePreferences();

        if (pf.reloadPreferences() == 1){
            if (cd.isConnected()){
                if (db.userDao().getAllUsers().size() != 0){
                    onService();
                }else{
                    if ((pf.reloadPreferences() == 1 )&& (db.userDao().getAllUsers().size() != 0 )){
                        Toast.makeText(getApplicationContext(), "Debe recargar los datos", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Recargar para obtener datos", Toast.LENGTH_SHORT).show();
                        //onService();
                    }
                }
            }else{
                onService();
            }
        }

        ReloadServices();
        PostDelete();
        deleteAllPost();
        getFavorite();

    }// Termina el metodo On Create!!

    private void onService() { // LLenar todos los posts
        // El json para el consumo
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi JsonApi = retrofit.create(JsonApi.class);

        // la lista
        Call<List<Post>> call = JsonApi.getPosts();

        // Decorador para refresh
        final RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(divider);

        ((Call) call).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<Post> posts = (ArrayList<Post>) response.body();
                if (db.userDao().getAllUsers().size() == 0) {
                    for (Post post : posts) {
                        if (post.getId() <= 20){
                            db.userDao().insertAll(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), true, true));
                        }else{
                            db.userDao().insertAll(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), true, false));
                        }
                        //Toast.makeText(getApplicationContext(), "Datos traidos ", Toast.LENGTH_SHORT).show();
                    }
                }
                datos.addAll(db.userDao().getAllUsers());
                recycler.setAdapter(adapter);
            }
            // Esto trae los datos de json y los inserta en el objeto para mostrarlos
            //datos =(ArrayList<Post>) db.userDao().getAllUsers();

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Trabajando sin internet", Toast.LENGTH_SHORT).show();
                datos.addAll(db.userDao().getAllUsers());
                recycler.setAdapter(adapter);
            }
        });
    }

    // el detalle de los posts
    @Override
    public void onPostDetail(Post post) {
        //Toast.makeText(getApplicationContext(),"Vista de : " + post.getId(), Toast.LENGTH_SHORT).show();
        onChangeVisto(post);

        //db.userDao().UpdateVisto(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), post.getFavorite(), false));

        // Esto envia los datos a la vista detalle
        Intent intent = new Intent(MainActivity.this, CustomDetalle.class);
        intent.putExtra("id", post.getId());
        intent.putExtra("title", post.getTitle());
        intent.putExtra("body", post.getBody());
        //startActivity(intent);
        startActivityForResult(intent,1);
    }

    // Cambiar el estado del boton favorito
    @Override
    public void onChangeFavorite(Post post, int position) {
        if (datos.get(position).setFavorite(!post.getFavorite())) {
            //Toast.makeText(getApplicationContext(), "Verdaero. ", Toast.LENGTH_SHORT).show();
            db.userDao().UpdateFavorite(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), true, post.getVisto()));
            if (c == 1) {
                datos.remove(adapter.getNote(position));
                fillOff();
            }
        } else {
            db.userDao().UpdateFavorite(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), false, post.getVisto()));
        }
        adapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onChangeVisto(Post post){
        post.setVisto(false);
        db.userDao().UpdateVisto(new Post(post.getUserId(), post.getId(), post.getTitle(), post.getBody(), post.getFavorite(), false));
        adapter.notifyDataSetChanged();
    }

    @Override // Resultado de la actividad detalles
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if (resultCode == RESULT_OK){
                int result = data.getIntExtra("result",0);
                Toast.makeText(getApplicationContext(), "Leido. " + result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void PostDelete() { // Swipe para borrar Posts

        // Esto es para borrar los post.
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                db.userDao().delete(adapter.getNote(viewHolder.getAdapterPosition()));
                datos.remove(adapter.getNote(viewHolder.getAdapterPosition()));
                fillOff();
                adapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(recycler);
    }

    private void fillOff(){
        if (adapter.getItemCount() < 1){
            c = 0;
            filterOff();
        }
    }

    private void deleteAllPost(){ // Borrar todos los posts
        // Eliminar todos los posts
        Button btn_Eliminar = findViewById(R.id.btn_Delete);

        // Eliminacion de todos los posts
        btn_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datos.size() != 0) {
                    c = 0;
                    db.userDao().deleteAllNotes();
                    Toast.makeText(getApplicationContext(), "Se han borrado los posts. ", Toast.LENGTH_SHORT).show();
                    datos.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "No tiene posts que borrar. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    int c = 0;
    private void getFavorite(){ // Obtener los favoritos presionando el boton
        Button btn_Favorite = findViewById(R.id.btn_Favorite);

        btn_Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c == 0) {
                    if (db.userDao().getAllFavorites().size() != 0) {
                        c = 1;
                        Toast.makeText(getApplicationContext(), "Filtro activo", Toast.LENGTH_SHORT).show();
                        filterOn();
                    }else{
                        Toast.makeText(getApplicationContext(), "No tiene items favoritos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    filterOff();
                    Toast.makeText(getApplicationContext(), "Filtro inactivo", Toast.LENGTH_SHORT).show();
                    c = 0;
                }
            }
        });
    }

    private void filterOn() { // Filtro de favoritos
        datos.clear();
        if (db.userDao().getAllFavorites().size() != 0) {
            datos.addAll(db.userDao().getAllFavorites());
            adapter.notifyDataSetChanged();
        }else{
            filterOff();
            Toast.makeText(getApplicationContext(), "No tiene items favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterOff() { // Filtro off de favoritos
        datos.clear();
        datos.addAll(db.userDao().getAllUsers());
        adapter.notifyDataSetChanged();
    }

    private void ReloadServices(){ // Recarga de servicios Posts
        Button btn_Reload = findViewById(R.id.btn_Reload);

        btn_Reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getItemCount() < 1) {
                    onService();
                }else{
                    Toast.makeText(getApplicationContext(), "Solo se carga si no hay datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

