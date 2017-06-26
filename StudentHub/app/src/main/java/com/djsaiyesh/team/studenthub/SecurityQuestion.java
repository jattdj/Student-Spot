package com.djsaiyesh.team.studenthub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class SecurityQuestion extends AppCompatActivity {
      TextView setquestion,answer;
    String data;
    ProgressDialog progressDialog;

          @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);
              Bundle bundle = getIntent().getExtras();
              String message = bundle.getString("securityquestion");

                setquestion = (TextView)findViewById(R.id.setquestion);
              setquestion.setText(message);
              answer=(TextView)findViewById(R.id.answer);
    }

    public void submit(View view)
    {
        data = answer.getText().toString();
        if(data.matches("")){
            Toast toast = Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_SHORT);
            toast.show();
        }else
        {   Bundle bundle = getIntent().getExtras();
           String idno =bundle.getString("email");
            progressDialog= new ProgressDialog(SecurityQuestion.this);
            progressDialog.setTitle("Checking");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(data,idno);
        }

    }


    class BackgroundTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/verifyanswer.php";
            String data1,email;
            data1 = args[0];
            email = args[1];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String datasend = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8");
                bufferedWriter.write(datasend);
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
                Toast toast = Toast.makeText(getApplicationContext(), "Answer Doesn't Match", Toast.LENGTH_SHORT);
                toast.show();

            } else {

                Intent intent = new Intent(SecurityQuestion.this,ChangePassword.class);
                Bundle bundle = getIntent().getExtras();
                String idno =bundle.getString("email");
                intent.putExtra("email",idno);
                startActivity(intent);

            }

        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_security_question, menu);
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


}
