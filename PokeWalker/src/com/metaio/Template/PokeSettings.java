package com.metaio.Template;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PokeSettings extends Activity {
	
	private MyAlarm alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poke_settings);
		alarm = new MyAlarm();
	}
	
	public void notification(View v){
		Bundle b = new Bundle();
		MyAlarm a = new MyAlarm(this, b, 10);
    }
    
    public void cancelNotification(View v){
    	Context context = this.getApplicationContext();
    	if(alarm != null){
    		alarm.cancelAlarm(context);
    	}
    	else{
    		Toast.makeText(context, "No Pokemon notification!", Toast.LENGTH_SHORT).show();
    	}
    }
}
