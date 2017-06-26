package com.djsaiyesh.team.studenthub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminPanel extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void logout(View view)
    {
        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("logged_in", "false");
        edit.commit();

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            this.finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    public void feedback(View view){

        Intent intent = new Intent(getApplicationContext(),GetFeedback.class);
        startActivity(intent);
    }

    public void publish(View view){
        Intent intent = new Intent(getApplicationContext(),CreateNotification.class);
        startActivity(intent);
    }

    public void updateattendance(View view){

        Intent intent = new Intent(getApplicationContext(),upload.class);
        startActivity(intent);
    }

}
