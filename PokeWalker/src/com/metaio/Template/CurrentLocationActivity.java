package com.metaio.Template;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CurrentLocationActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener
{
	// Instance variables for a map, location client, and current location button.
	private GoogleMap mMap;
	LocationClient mLocationClient;
	ImageButton mBtnCurrentLocation;
	
	// Callback when this activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        

        
        // Gets the google map.
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        mBtnCurrentLocation = (ImageButton)findViewById(R.id.imbtnCurrentLocation);
        
        // When the current location button is clicked, go to the current location.
        mBtnCurrentLocation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				try
				{
		        	// Connects to google play services through the location client.
					mLocationClient.connect();
				}
				catch (Throwable t)
				{
					Log.e("MainActivity", t.getLocalizedMessage(), t);
				}
			}
		});
        
        // Instantiates a location client.
        mLocationClient = new LocationClient(this, this, this);
    }
    
    // Callback when this activity starts.
    @Override
    protected void onStart() 
    {
        super.onStart();
    }
    
    // Callback when this activity resumes.
    @Override
    protected void onResume() 
    {
        super.onResume();
        
        try
		{
        	// Connects to google play services through the location client.
			mLocationClient.connect();
		}
		catch (Throwable t)
		{
			Log.e("MainActivity", t.getLocalizedMessage(), t);
		}
    }
    
    // Callback when this activity pauses.
    @Override
    protected void onPause() 
    {
        super.onPause();
    }
    
    // Callback when this activity stops.
    @Override
    protected void onStop() 
    {
        super.onStop();
    }
    
    // Callback when this activity restarts.
    @Override
    protected void onRestart() 
    {
        super.onRestart();
    }
    
    // Callback when this activity is destroyed.
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        
        // Disconnect the location client if this activity is destroyed.
        try
		{
			mLocationClient.disconnect();
		}
		catch (Throwable t)
		{
			Log.e("MainActivity", t.getLocalizedMessage(), t);
		}
        
        // Set all variables to null.
        mMap = null;
		mLocationClient = null;
    }

    // Callback for a failed connection.
	@Override
	public void onConnectionFailed(ConnectionResult arg0) 
	{
		// Try connecting again.
		try
		{
			Thread.sleep(2000);
			mLocationClient.connect();
		}
		catch (Throwable t)
		{
			Log.e("MainActivity", t.getLocalizedMessage(), t);
		}
		
	}

	// Callback for when the location client establishes a connection to google play services.
	@Override
	public void onConnected(Bundle arg0) 
	{
		Location myLocation = null;
		LatLng myCoordinates = null;
		
		// Get the last known current location.
		try
		{
			myLocation = mLocationClient.getLastLocation();
			myCoordinates = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
		}
		catch (Throwable t)
		{
			Log.e("MainActivity", t.getLocalizedMessage(), t);
		}
		
		// Move the map to current location.    
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 15));
        
        // Zoom in on current location.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        
        // Set the marker to the current location.
        Marker myMarker = mMap.addMarker(new MarkerOptions()
	    	.position(myCoordinates)
			.title("Trainer")
			.snippet("Level 9001")
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.trainer)));
        
     // Lastly, disconnect from location client.
        try
		{
			mLocationClient.disconnect();
		}
		catch (Throwable t)
		{
			Log.e("MainActivity", t.getLocalizedMessage(), t);
		}
        
		DataSource db = DataSource.getInstance(getApplicationContext());
		db.open();
		db.catchPokemon(1, myLocation.getLatitude() + 0.001, myLocation.getLongitude() + 0.001);
		db.catchPokemon(2, myLocation.getLatitude() + 0.001, myLocation.getLongitude() - 0.001);
		db.catchPokemon(3, myLocation.getLatitude() - 0.001, myLocation.getLongitude() + 0.001);
		db.catchPokemon(4, myLocation.getLatitude() - 0.001, myLocation.getLongitude() - 0.001);
		
        Location[] location = new Location[4];
      Log.v("test", "test1");
      location[0] = new Location("");
      location[0].setLatitude(db.getCoordinates(1)[0]);
      location[0].setLongitude(db.getCoordinates(1)[1]);
      Log.v("test", "test2");
      location[1] = new Location("");
      location[1].setLatitude(db.getCoordinates(2)[0]);
      location[1].setLongitude(db.getCoordinates(2)[1]);
      location[2] = new Location("");
      location[2].setLatitude(db.getCoordinates(3)[0]);
      location[2].setLongitude(db.getCoordinates(3)[1]);
      location[3] = new Location("");
      location[3].setLatitude(db.getCoordinates(4)[0]);
      location[3].setLongitude(db.getCoordinates(4)[1]);
      
      Log.d("lat", Double.toString(location[0].getLatitude()));
      Log.d("long", Double.toString(location[0].getLongitude()));
      
      Log.d("lat", Double.toString(location[1].getLatitude()));
      Log.d("long", Double.toString(location[1].getLongitude()));
      
      Log.d("lat", Double.toString(location[2].getLatitude()));
      Log.d("long", Double.toString(location[2].getLongitude()));
      
      Log.d("lat", Double.toString(location[3].getLatitude()));
      Log.d("long", Double.toString(location[3].getLongitude()));
      setPokemonMarkers(location);
        
//        Log.v("test", "test1");
//        location[0] = new Location("");
//        location[0].setLatitude(myLocation.getLatitude() + .001);
//        location[0].setLongitude(myLocation.getLongitude() + .001);
//        Log.v("test", "test2");
//        location[1] = new Location("");
//        location[1].setLatitude(myLocation.getLatitude() + .001);
//        location[1].setLongitude(myLocation.getLongitude() - .001);
//        location[2] = new Location("");
//        location[2].setLatitude(myLocation.getLatitude() - .001);
//        location[2].setLongitude(myLocation.getLongitude() + .001);
//        location[3] = new Location("");
//        location[3].setLatitude(myLocation.getLatitude() - .001);
//        location[3].setLongitude(myLocation.getLongitude() - .001);
//        setPokemonMarkers(location);
        
        //Intent i = new Intent(this, SomeActivity.class);
        //i.putExtra("coordinates", myLocation);
	}
	
	// Set markers for nearby pokemon.
	public void setPokemonMarkers(Location[] location)
	{
		for (Location pokeLocation : location)
		{
			LatLng pokeCoordinates = null;
			
			// Create coordinates from location.
			try
			{
				pokeCoordinates = new LatLng(pokeLocation.getLatitude(), pokeLocation.getLongitude());
			}
			catch (Throwable t)
			{
				Log.e("MainActivity", t.getLocalizedMessage(), t);
			}
			
			// Set a marker at those coordinates.
			Marker pokeMarker = mMap.addMarker(new MarkerOptions()
		    	.position(pokeCoordinates)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.pokeball)));
		}
	}

	// Callback for when the location client is disconnected.
	@Override
	public void onDisconnected() 
	{
		// Do nothing.
	}
}