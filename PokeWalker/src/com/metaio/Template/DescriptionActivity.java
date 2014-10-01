package com.metaio.Template;

import java.io.IOException;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.metaio.Template.R;

public class DescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description);
		Bundle b = getIntent().getExtras();
		String x = b.getString("id");
		Log.d("id",x);
		
		//DbHelper db = DbHelper.getInstance(getApplicationContext());
		DataSource db = DataSource.getInstance(getApplicationContext());
		String[] pokemon = db.getPokemon(Integer.parseInt(x));
		if(pokemon != null){
		TextView pokeName = (TextView) findViewById(R.id.dPokeName);
		TextView pokeHeight = (TextView) findViewById(R.id.dPokeHeight);
		TextView pokeWeight = (TextView) findViewById(R.id.dPokeWeight);
		//TextView pokeSex = (TextView) findViewById(R.id.dPokeSex);
		TextView pokeDescription = (TextView) findViewById(R.id.dPokeDescription);
		ImageView pokeImage = (ImageView) findViewById(R.id.dPokeImage);
		ImageView pokeType1 = (ImageView) findViewById(R.id.dPokeType1);
		ImageView pokeType2 = (ImageView) findViewById(R.id.dPokeType2);
		
		pokeName.setText(pokemon[1]);

        Drawable d = null;
        try{
            d = Drawable.createFromStream(getAssets().open("pokemon_" + pokemon[0] + ".png"), null);
        }catch(IOException e){

        }
        if(d != null)
            pokeImage.setImageDrawable(d);


		pokeType1.setImageResource(this.getResources().getIdentifier(pokemon[2], "drawable", this.getPackageName()));
		Log.d("hi",pokemon[3]);
		if(pokemon[3] != "null"){
		pokeType2.setImageResource(this.getResources().getIdentifier(pokemon[3], "drawable", this.getPackageName()));
		}
		else{
		pokeType2.setImageResource(this.getResources().getIdentifier("bug", "drawable", this.getPackageName()));	
		}
		pokeDescription.setText(pokemon[4]);
		pokeHeight.setText(pokemon[5]);
		pokeWeight.setText(pokemon[6]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

}
