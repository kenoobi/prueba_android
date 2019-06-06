package com.testconcept.android_test.CustomItems;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.testconcept.android_test.MainActivity;
import com.testconcept.android_test.Persistence.Post;
import com.testconcept.android_test.R;

public class CustomDetalle extends MainActivity {

    private Post item;
    private TextView txid,txtitle,txbody;
    private CustomAdapter.IonPortListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_detalle);

        int id =  getIntent().getIntExtra("id", 0);
        String title = getIntent().getStringExtra("title");
        String body =  getIntent().getStringExtra("body");
        int favorite =  getIntent().getIntExtra("favorite", 0);
        int visto =  getIntent().getIntExtra("visto", 0);

        item = new Post(0,id,title,body, favorite, visto);

       /* Intent resulIntent = new Intent();
        resulIntent.putExtra("result", id-1);
        setResult(RESULT_OK, resulIntent);*/

        //Toast.makeText(getApplicationContext(), "Detalle id :  " + id, Toast.LENGTH_SHORT).show();

        //finish();

        TextView identi = (TextView)findViewById(R.id.detail_id);
        identi.setText(String.valueOf(id));

        TextView titulo = (TextView)findViewById(R.id.detail_title);
        titulo.setText(String.valueOf(title));

        TextView cuerpo = (TextView)findViewById(R.id.detail_body);
        cuerpo.setText(String.valueOf(body));
    }

}
