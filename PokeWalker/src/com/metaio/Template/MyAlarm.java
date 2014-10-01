package com.metaio.Template;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


//myAlarm was adapted from 
//http://www.learn-android-easily.com/2013/05/android-alarm-manager_31.html
public class MyAlarm extends BroadcastReceiver {
    private final String REMINDER_BUNDLE = "MyReminderBundle"; 

    // this constructor is called by the alarm manager.
    public MyAlarm(){ }
    
    // this will cancel the alarm
    public void cancelAlarm(Context context){
    	Intent intent = new Intent(context, MyAlarm.class);
    	PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	alarmManager.cancel(sender);
    }
    // you can use this constructor to create the alarm. 
    //  Just pass in the main activity as the context, 
    //  any extras you'd like to get later when triggered 
    //  and the timeout
     public MyAlarm(Context context, Bundle extras, int timeoutInSeconds){
         AlarmManager alarmMgr = 
             (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         Intent intent = new Intent(context, MyAlarm.class);
         intent.putExtra(REMINDER_BUNDLE, extras);
         PendingIntent pendingIntent =
             PendingIntent.getBroadcast(context, 0, intent, 
             PendingIntent.FLAG_UPDATE_CURRENT);
         Calendar time = Calendar.getInstance();
         time.setTimeInMillis(System.currentTimeMillis());
         time.add(Calendar.SECOND, timeoutInSeconds);
         alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), time.getTimeInMillis(),
                      pendingIntent);
         /////////////////////////////////////////////////////////////////////////////////
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
  		.setSmallIcon(R.drawable.pokeball_icon)
 		 .setContentTitle("PokeWalker")
 		 .setContentText("It's time to catch Pokemon!");
  
  //the activity that the notify sends to
  Intent i = new Intent(context, PokeHome.class);
  PendingIntent resultIntent = PendingIntent.getActivity(context, getResultCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);
  
//  //adds stack so pressing back in the AR will go to the home
//  TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//  stackBuilder.addParentStack(MainActivity.class);
//  //adds top stack
//  stackBuilder.addNextIntent(resultIntent);
//  PendingIntent resultPendingIntent =
// 		 stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  
  mBuilder.setContentIntent(resultIntent);
//set only if notify is persistent
  mBuilder.setOngoing(true);
  mBuilder.setAutoCancel(true);
  NotificationManager mNotificationManager =
  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  // mId allows you to update the notification later on.
  mNotificationManager.notify(1, mBuilder.build());
}
     

      @Override
     public void onReceive(Context context, Intent intent) {
         // here you can get the extras you passed in when creating the alarm
         //intent.getBundleExtra(REMINDER_BUNDLE));

         Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
         Log.d("alarm", "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
         
         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
         		.setSmallIcon(R.drawable.pokeball_icon)
        		 .setContentTitle("PokeWalker")
        		 .setContentText("There is a Pokemon in the Area!");
         
         //the activity that the notify sends to
         Intent i = new Intent(context, ARMain.class);
         PendingIntent resultIntent = PendingIntent.getActivity(context, getResultCode(), i, PendingIntent.FLAG_UPDATE_CURRENT);
         
//         //adds stack so pressing back in the AR will go to the home
//         TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//         stackBuilder.addParentStack(MainActivity.class);
//         //adds top stack
//         stackBuilder.addNextIntent(resultIntent);
//         PendingIntent resultPendingIntent =
//        		 stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
         
         mBuilder.setContentIntent(resultIntent);
//set only if notify is persistent
//         mBuilder.setOngoing(true);
         mBuilder.setAutoCancel(true);
         NotificationManager mNotificationManager =
         (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
         // mId allows you to update the notification later on.
         mNotificationManager.notify(0, mBuilder.build());
     }
}