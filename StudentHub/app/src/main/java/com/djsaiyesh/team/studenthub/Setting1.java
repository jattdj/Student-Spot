package com.djsaiyesh.team.studenthub;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class Setting1 extends AppCompatActivity {

    TextView id, name, aboutyou, mobile;
    ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    private Uri fileUri;
    String image_path = "",token;
    boolean thread_running=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting1);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getId();




        id = (TextView) findViewById(R.id.idn);
        name = (TextView) findViewById(R.id.email);
        aboutyou = (TextView) findViewById(R.id.aboutyou);
        mobile = (TextView) findViewById(R.id.mobile);
        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sname = pref.getString("name", "");
        String smobile = pref.getString("mobile", "");
        String saboutyou = pref.getString("aboutyou","");
        String sidno=pref.getString("idno","");
        id.setText(sidno);
        name.setText(sname);
        mobile.setText(smobile);
        aboutyou.setText(saboutyou);


        imageView = (ImageView)this.findViewById(R.id.profileimg);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void editaboutyou (View view){

        Intent intent = new Intent(getApplicationContext(),dialog_edit_aboutyou.class);
        startActivity(intent);
    }

    public  void change(View view){


        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,CAMERA_REQUEST);

        }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Setting1.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);



        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Main.class));
        finish();

    }

    public void changepwd(View view){

        Intent intent = new Intent(Setting1.this,ChangePass.class);
        startActivity(intent);
    }

    public void edit(View view){

        Intent intent= new Intent(Setting1.this,EditProfile.class);
        startActivity(intent);

    }


}
