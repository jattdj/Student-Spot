package com.djsaiyesh.team.studenthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangePass extends AppCompatActivity {
    TextView pass1, pass2;
    String password, password2;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .4));


        pass1 = (TextView) findViewById(R.id.pass);
        pass2 = (TextView) findViewById(R.id.pass2);
    }

    public void change(View view) {

        password = pass1.getText().toString();
        password2 = pass2.getText().toString();

        if (password.matches("") || password2.matches("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT);
            toast.show();

        } else {

            if(password.equals(password2)) {
                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String email =pref.getString("idno","");
                progressDialog= new ProgressDialog(ChangePass.this);
                progressDialog.setTitle("Updating");
                progressDialog.setMessage("Please wait");
                progressDialog.show();

                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute(email, password);
            }else{   Toast toast = Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_SHORT);
                toast.show();

            }
        }

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
                progressDialog.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Intent intent = new Intent(ChangePass.this, Setting1.class);
                startActivity(intent);

            }

        }


    }

}
