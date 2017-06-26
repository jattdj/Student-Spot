package com.djsaiyesh.team.studenthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

      TextView name,mobile;
    String namee,mobilee;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .4));

        name=(TextView)findViewById(R.id.email);
        mobile=(TextView)findViewById(R.id.mobile);

        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sname = pref.getString("name", "");
        String smobile = pref.getString("mobile", "");

        name.setText(sname);
        mobile.setText(smobile);




    }

    public void update(View view){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String sidno=pref.getString("idno","");

            namee=name.getText().toString();
            mobilee=mobile.getText().toString();

            progressDialog= new ProgressDialog(EditProfile.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait");
            progressDialog.show();

            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(sidno,namee,mobilee);




        }else{

            Toast toast = Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG);
            toast.show();
        }
        }


    class BackgroundTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/updateprofile.php";
            String email, data1,data2;

            email = args[0];
            data1 = args[1];
            data2 = args[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8")+"&"+
                        URLEncoder.encode("data2", "UTF-8") + "=" + URLEncoder.encode(data2, "UTF-8");
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
                progressDialog.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Updated Successfully", Toast.LENGTH_LONG);
                toast.show();

                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("name", namee);
                edit.putString("mobile",mobilee);
                edit.commit();


                Intent intent = new Intent(EditProfile.this, Setting1.class);
                startActivity(intent);

            }

        }


    }

    }


