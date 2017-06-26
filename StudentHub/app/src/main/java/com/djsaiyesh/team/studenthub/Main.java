package com.djsaiyesh.team.studenthub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    ProgressDialog progressDialog;
    ImageView imageHolder,heart,oops;
    WebView mWebView;
    TextView pa, pt, fa, ft, password, repass, headernamee, headeridno,nonet;
    String myJSON, pastattend, pasttotal, futureattend, futuretotal, pass1, pass2, idno, aboutyou, idno11;
    Button cal, button;
    Float a, b, c, d, e;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME="name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_DATA = "data";
    private static final String TAG_FEELING = "feeling";
    private static final String TAG_LIKES = "likes";
    private static final String TAG_DATE="date";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;
    ListView list;


    private static final int requestCode = 2500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        list = (ListView) findViewById(R.id.listview);
        personList = new ArrayList<HashMap<String, String>>();
        progressDialog = new ProgressDialog(Main.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait");
        progressDialog.show();


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {




            getData();
        }else
        {    progressDialog.dismiss();
           oops=(ImageView)findViewById(R.id.oops);
            nonet=(TextView)findViewById(R.id.nonet);
            oops.setVisibility(View.VISIBLE);
            nonet.setVisibility(View.VISIBLE);
        }

        mWebView = (WebView) findViewById(R.id.webview1);


        button = (Button) findViewById(R.id.changepass);
        password = (TextView) findViewById(R.id.changedialogpass1);
        repass = (TextView) findViewById(R.id.changedialogpass2);


        imageHolder = (ImageView) findViewById(R.id.profileimg);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(this);


    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name=c.getString(TAG_NAME);
                String email = c.getString(TAG_EMAIL);
                String data = c.getString(TAG_DATA);
                String feeling = c.getString(TAG_FEELING);
                String likes = c.getString(TAG_LIKES);
                String date = c.getString(TAG_DATE);

                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_EMAIL, email);
                persons.put(TAG_DATA, data);
                persons.put(TAG_FEELING, feeling);
                persons.put(TAG_LIKES, likes);
                persons.put(TAG_DATE, date);

                personList.add(persons);
            }
           progressDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    Main.this, personList, R.layout.row,
                    new String[]{TAG_ID,TAG_NAME, TAG_EMAIL, TAG_DATA, TAG_FEELING, TAG_LIKES, TAG_DATE},
                    new int[]{R.id.id,R.id.name, R.id.email, R.id.data, R.id.feeling, R.id.likes, R.id.date}
            );

            list.setAdapter(adapter);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    TextView textView = (TextView) view.findViewById(R.id.id);
                    TextView textView1 =(TextView) view.findViewById(R.id.likes);
                    heart=(ImageView)view.findViewById(R.id.heart);
                    heart.setImageResource(R.drawable.filledheart);
                    String likee = textView1.getText().toString();
                    Integer a = Integer.parseInt(likee);
                    Integer b =a+1;
                    String likeee = Integer.toString(b);
                    textView1.setText(likeee);
                    BackgroundTask2 backgroundTask2 = new BackgroundTask2();
                    backgroundTask2.execute(textView.getText().toString(),likeee);

                }
            };
            list.setOnItemClickListener(listener);









        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("https://studenthubdaljit.000webhostapp.com/fetchpost.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    private static final int TIME_DELAY = 2000;
    private static long back_pressed;






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.home){

           Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);



        }else if (id == R.id.jntu) {


            Jntu jntu = new Jntu();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, jntu);
            fragmentTransaction.commit();

        }else if(id==R.id.feedback){

            Intent intent = new Intent(getApplicationContext(),feedback.class);
            startActivity(intent);
        }

        else if (id == R.id.calculator) {

            Calculator calculator = new Calculator();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, calculator);
            fragmentTransaction.commit();


        } else if (id == R.id.logout) {

            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("logged_in", "false");
            edit.commit();

            Intent intent = new Intent(Main.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.setting) {

               Intent intent = new Intent(getApplicationContext(),Setting1.class);
               startActivity(intent);




        } else if(id == R.id.profile){

            Intent intent = new Intent(getApplicationContext(),CheckProfile.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void calculate(View view) {


        pa = (TextView) findViewById(R.id.pastattend);
        pt = (TextView) findViewById(R.id.pasttotal);
        fa = (TextView) findViewById(R.id.futureattend);
        ft = (TextView) findViewById(R.id.futuretotal);
        cal = (Button) findViewById(R.id.cal);
        pastattend = pa.getText().toString();
        pasttotal = pa.getText().toString();
        futureattend = fa.getText().toString();
        futuretotal = ft.getText().toString();

        cal.setEnabled(false);


        pa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cal.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cal.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cal.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cal.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (pastattend.matches("")) {

            pa.setError("Empty Field");
        } else {
            if (pasttotal.matches("")) {
                pt.setError("Empty Field");
            } else {
                if (futureattend.matches("")) {
                    fa.setError("Empty Fields");

                } else {
                    if (futuretotal.matches("")) {
                        ft.setError("Empty Fields");

                    } else {

                        a = Float.parseFloat(pastattend);
                        b = Float.parseFloat(pasttotal);
                        c = Float.parseFloat(futureattend);
                        d = Float.parseFloat(futuretotal);


                        e =(a + c) / (b + d);



                        String abc = String.valueOf(e);

                        Toast toast = Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_LONG);
                        toast.show();


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Your attendance will be" + "  " + abc + "%")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Will go to home", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
            }
        }


    }
    public void change(View view){



        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {





            }


        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }





    }

    public void changepwd(View view) {




        Context context = this;
        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.dialog_change_pass);
        openDialog.setTitle("Custom Dialog Box");

        openDialog.show();
    }


    class BackgroundTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/changepassword.php";
            String email, password;
            email = args[0];
            password = args[1];


            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (Objects.equals(result, "1")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Password Changed Successfully", Toast.LENGTH_LONG);
                toast.show();


            }

        }


    }
    class BackgroundTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/like.php";
            String id, likes;
            id = args[0];
            likes = args[1];


            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("likes", "UTF-8") + "=" + URLEncoder.encode(likes, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (Objects.equals(result, "1")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Liked", Toast.LENGTH_LONG);
                toast.show();


            }

        }


    }



public  void imgchg(View view){

    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(cameraIntent, requestCode);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Main.requestCode == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageHolder.setImageBitmap(bitmap);
        }
    }


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


       public void changeaboutyou(View view){




        Toast toast = Toast.makeText(getApplicationContext(),aboutyou,Toast.LENGTH_LONG);
        toast.show();

    }



    public void articlepost(View view){

        Intent intent = new Intent(getApplicationContext(),ArticlePost.class);
        startActivity(intent);
    }

    public void increaselike(View view){



    }


}