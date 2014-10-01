package com.metaio.Template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.metaio.Template.R;

public class PokeHome extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.poke_home);
	
	    // TODO Auto-generated method stub
	}
	
	public void sendPokedex(View v){
		Intent pokedex = new Intent(getApplicationContext(), PokeDexMain.class);
		startActivity(pokedex);
	}
	
	public void sendMap(View v){
		Intent map = new Intent(getApplicationContext(), CurrentLocationActivity.class);
		startActivity(map);
	}

    public void StartAR(View V){
        Intent intent = new Intent(getApplicationContext(), ARMain.class);//Template.class);
        startActivity(intent);
    }
    

    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(1, 1, 0, "Settings").setIcon(R.drawable.pokeball_icon);
		return true;
    }
    
    @Override
    	public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case 1:
    		Intent settings = new Intent(this, PokeSettings.class);
    		startActivity(settings);
    		return true;
    	
    	}
		return super.onOptionsItemSelected(item);
    }
    
}
