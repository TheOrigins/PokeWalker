package com.metaio.Template;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.metaio.Template.R;

public class CustomAdapter extends ArrayAdapter<String[]> {
	//adapter inspired by http://www.ezzylearning.com/tutorial.aspx?tid=1763429
		
		Context context;
		int resource;
		ArrayList<String[]> data =null;
		
		public CustomAdapter(Context context, int resource, ArrayList<String[]> s) {
			super(context, resource, resource, s);
			// TODO Auto-generated constructor stub
			
			this.context = context;
			this.resource = resource;
			this.data = s;
			
		}
		
		public View getView(int position, View convertRow, ViewGroup parent) {
			
			View row =convertRow;
			RowHolder tmp = null;
			//rowholder is optional, it just opens an object so it doesnt have to be created everytime
			
			
			if(row == null){
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(resource, parent,false);
				
				tmp = new RowHolder();
				tmp.pokeName = (TextView) row.findViewById(R.id.pokeName);
				tmp.pokeImage = (ImageView) row.findViewById(R.id.pokeImage);
				tmp.pokeId = (TextView) row.findViewById(R.id.pokeId);
				row.setTag(tmp);
			}
			else{
				tmp = (RowHolder)row.getTag();
			}
			
			//sets the data in the array
			String[] listRow = data.get(position);
			tmp.pokeName.setText(listRow[1]);

            Drawable d = null;
            try{
            d = Drawable.createFromStream(context.getAssets().open("pokemon_" + listRow[0] + ".png"), null);
            }catch(IOException e){

            }
            if(d != null)
                tmp.pokeImage.setImageDrawable(d);

			tmp.pokeId.setText(listRow[0]);
			//tmp.pokeImage.setImageResource(R.drawable.pokemon_152);
			return row;
			}

		static class RowHolder{
			TextView pokeName;
			ImageView pokeImage;
			TextView pokeId;
		}
		
	}

