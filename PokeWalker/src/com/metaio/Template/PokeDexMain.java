package com.metaio.Template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.metaio.Template.CustomAdapter;

public class PokeDexMain extends Activity {
    ListView listview1 = null;
    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//		DbHelper db = DbHelper.getInstance(getApplicationContext());
        DataSource d = DataSource.getInstance(getApplicationContext());
        d.open();
        ArrayList<String[]> s = d.getAllPokemon();
        Log.d("s",Integer.toString(s.size()));
        if(s != null){
            adapter = new CustomAdapter(this, R.layout.pokedexlist, s);
            listview1 = (ListView) findViewById(R.id.listView1);
            listview1.setAdapter(adapter);
            listview1.setOnItemClickListener(new OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position,
                                        long id) {
                    String[] tmp = adapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class );
                    intent.putExtra("id", tmp[0]);
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
