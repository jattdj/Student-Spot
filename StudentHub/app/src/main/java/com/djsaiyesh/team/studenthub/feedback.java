package com.djsaiyesh.team.studenthub;

import android.app.AlertDialog;
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
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
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

public class feedback extends AppCompatActivity {
    TextView feedback,remain;
    Button submit;
    String data;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .3));


        feedback = (TextView) findViewById(R.id.feeddetails);
        submit = (Button) findViewById(R.id.feedsubmit);
        remain =(TextView)findViewById(R.id.remain);
        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remain.setText(100 - s.toString().length() + "/100");
            }
        });


    }

    public void feedback(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            data = feedback.getText().toString();

            if (data.equals("")) {
                   feedback.setError("Cannot Be Empty");

            } else {


                SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String sidno = pref.getString("idno", "");
                String sname = pref.getString("name", "");
                progressDialog = new ProgressDialog(feedback.this);
                progressDialog.setTitle("Sending");
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                submit.setEnabled(false);
                BackgroundTask1 backgroundTask1 = new BackgroundTask1(this);
                backgroundTask1.execute(sidno,sname, data);
            }
        }
    else

    {
        Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
        toast.show();
    }}


    class BackgroundTask1 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        Context ctx;

        BackgroundTask1(Context ctx) {
            this.ctx = ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Status");

        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/feedback.php";
            String id,name,details;
            id=args[0];
            name=args[1];
            details = args[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+"&"+
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+"&"+
                        URLEncoder.encode("details", "UTF-8") + "=" + URLEncoder.encode(details, "UTF-8");
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
            progressDialog.dismiss();
            alertDialog.setMessage(result);
            alertDialog.show();

            Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);

        }

    }
}


