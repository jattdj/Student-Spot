package com.djsaiyesh.team.studenthub;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Objects;

public class Login extends AppCompatActivity {
    TextView Email, password,wrong;
    Button login;
    String email, pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        Email = (TextView) findViewById(R.id.headeridno);


        password = (TextView) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        wrong=(TextView)findViewById(R.id.wrong);

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  wrong.setVisibility(View.INVISIBLE);
                  login.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wrong.setVisibility(View.INVISIBLE);
                login.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    public void register(View view){
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }
    public void forgot(View view){
        Intent intent = new Intent(Login.this,forgot.class);
        startActivity(intent);

    }

    public void login(View view) {


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {





            login.setEnabled(false);
            email = Email.getText().toString();
            pass = password.getText().toString();

            if(email.matches("") || pass.matches("")){
                Email =(TextView)findViewById(R.id.headeridno);
                Email.setError("Field Cannot be Empty");

                password =(TextView)findViewById(R.id.password);
                password.setError("Field Cannot be Empty");


            }else{


                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Loggin In");
                progressDialog.setMessage("Please Wait");
                progressDialog.show();


            BackgroundTask1 backgroundTask1 = new BackgroundTask1(this);
            backgroundTask1.execute(email, pass);
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }

    }


    class BackgroundTask1 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        Context ctx;
        BackgroundTask1(Context ctx)
        {this.ctx =ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Login Information....");

        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/login.php";
            String email, pass;
            email = args[0];
            pass = args[1];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
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
            if(Objects.equals(result, "1")){


                 progressDialog.dismiss();
               wrong.setVisibility(View.VISIBLE);
                login.setEnabled(true);
            }else {
                if (Objects.equals(result, "Admin")) {
                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("logged_in", "true");
                    edit.putString("type","Admin");
                    edit.putString("idno", email);
                    edit.commit();

                    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
                    startActivity(intent);

                } else {


                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("logged_in", "true");
                    edit.putString("type","Student");
                    edit.putString("idno", email);
                    edit.commit();


                    BackgroundTask4 backgroundTask4 = new BackgroundTask4();
                    backgroundTask4.execute(email);


                    BackgroundTask2 backgroundTask2 = new BackgroundTask2();
                    backgroundTask2.execute(email);

                    BackgroundTask3 backgroundTask3 = new BackgroundTask3();
                    backgroundTask3.execute(email);


                    Intent intent = new Intent(Login.this, Main.class);
                    intent.putExtra("idno", email);
                    startActivity(intent);
                }
            }
        }

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



    class BackgroundTask4 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/fetchname.php";
            String data1;
            data1 = args[0];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8");
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
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG);
                toast.show();

            } else {

                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("name", result);
                edit.commit();


            }

        }
    }


    class BackgroundTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/fetchmobile.php";
            String data1;
            data1 = args[0];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8");
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
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG);
                toast.show();

            } else {

                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("mobile", result);
                edit.commit();


            }

        }
    }


    class BackgroundTask3 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/fetchaboutyou.php";
            String data1;
            data1 = args[0];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8");
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
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG);
                toast.show();

            } else {

                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("aboutyou", result);
                edit.commit();


            }

        }


    }











}
