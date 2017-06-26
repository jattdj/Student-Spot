package com.djsaiyesh.team.studenthub;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        setContentView(R.layout.activity_splash_screen);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,1);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);




        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String logged_in = pref.getString("logged_in","");
                    String type=pref.getString("type","");
                    if(logged_in.equals("true")){
                        if(type.equals("Admin"))
                        {
                            Intent intent = new Intent(getApplicationContext(),AdminPanel.class);
                            startActivity(intent);
                        }
                         else{


                        Intent intent = new Intent(SplashScreen.this,Main.class);
                        startActivity(intent);}
                    }else {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                    }         }
            }
        };
        timerThread.start();
    }


}
