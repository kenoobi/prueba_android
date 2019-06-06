package com.testconcept.android_test.CustomItems;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.testconcept.android_test.Persistence.Post;
import com.testconcept.android_test.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolderDatos>
{
    ArrayList<Post> datos;
    private Context context;
    private IonPortListener listener;
    private int valor;

    public void filterList(ArrayList<Post> filtroFavorite) {
        datos = filtroFavorite;
        notifyDataSetChanged();
    }

    public interface IonPortListener{
        void onPostDetail(Post post);
        void onChangeFavorite(Post post, int position);
        void onChangeVisto(Post post);
    }

    public Post getNote(int position){
        return datos.get(position);

    }

    public CustomAdapter(ArrayList<Post> datos, Context context, IonPortListener listener) {
        this.datos = datos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_relative_layout, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos holder, final int position) {
        holder.mId.setText(Integer.toString(datos.get(position).getId()));
        holder.mTitle.setText(datos.get(position).getTitle());
        holder.mBody.setText(datos.get(position).getBody());


        if (datos.get(position).getFavorite()){
            holder.bFavorito.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dee1e5")));
            holder.bFavorito.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }else{
            holder.bFavorito.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d8d20f")));
            holder.bFavorito.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        }

        if (datos.get(position).getVisto()){
            holder.bVisto.setVisibility(View.VISIBLE);
        }else{
            holder.bVisto.setVisibility(View.INVISIBLE);
        }

        holder.Cusdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPostDetail(datos.get(position));
            }
        });

        holder.bFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangeFavorite(datos.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView mId, mTitle, mBody;
        ImageView bVisto, bFavorito;
        RelativeLayout Cusdetail;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            mId = (TextView) itemView.findViewById(R.id.custom_id);
            Cusdetail = (RelativeLayout) itemView.findViewById(R.id.Custom_Layout);
            mTitle = (TextView) itemView.findViewById(R.id.custom_title);
            mBody = (TextView) itemView.findViewById(R.id.custom_body);
            bVisto = (ImageView) itemView.findViewById(R.id.btn_visto);
            bFavorito = (ImageView) itemView.findViewById(R.id.btn_fav);
        }
    }
}

