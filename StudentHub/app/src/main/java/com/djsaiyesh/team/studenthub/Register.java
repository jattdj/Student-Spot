package com.djsaiyesh.team.studenthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.Objects;

public class Register extends AppCompatActivity {
     TextView reg_name,reg_pass1,reg_pass2,reg_phone,reg_email,securityans;
    String name,email,password1,password2,mobile,securityansstring,securityquestionstring,type;
    Spinner securityspinner,typespinner;
    Button regbutton;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regbutton=(Button)findViewById(R.id.regbutton);
        reg_name = (TextView) findViewById(R.id.reg_name);
        reg_email = (TextView) findViewById(R.id.reg_email);
        reg_pass1 = (TextView) findViewById(R.id.reg_pass1);
        reg_pass2 = (TextView) findViewById(R.id.reg_pass2);
        reg_phone = (TextView) findViewById(R.id.reg_Phone);

        securityspinner=(Spinner)findViewById(R.id.securityspinner);
        typespinner=(Spinner)findViewById(R.id.typespinner);

        securityans=(TextView)findViewById(R.id.securityans);


        reg_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              regbutton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reg_pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                regbutton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reg_pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                regbutton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        reg_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                regbutton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }


    public void saveInfo (View view){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()){



            name = reg_name.getText().toString();
            email = reg_email.getText().toString();
            password1 = reg_pass1.getText().toString();
            password2 = reg_pass2.getText().toString();
            mobile = reg_phone.getText().toString();
            securityansstring=securityans.getText().toString();
            securityquestionstring=String.valueOf(securityspinner.getSelectedItem());
              type=String.valueOf(typespinner.getSelectedItem());



            if(email.matches("") || name.matches("")|| password1.matches("")||password2.matches("")||mobile.matches("")||securityansstring.matches("")){
                Toast toast = Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_SHORT);
                toast.show();

            }else{

                if(type.equals("Select Account Type..")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Account Type Not Selected",Toast.LENGTH_LONG);
                    toast.show();
                }else{


                if((email.toLowerCase().contains("891A".toLowerCase())|| email.contains("891A"))&& email.length()==10) {

                    if (mobile.length() == 10) {


                    if (password1.equals(password2)) {

                     if(securityquestionstring.equals("Choose a Security Question..")){

                         Toast toast = Toast.makeText(getApplicationContext(),"No security Question Choosen",Toast.LENGTH_LONG);
                         toast.show();

                     }else {

                         regbutton = (Button) findViewById(R.id.regbutton);
                         regbutton.setEnabled(false);

                         progressDialog = new ProgressDialog(Register.this);
                         progressDialog.setTitle("Registering");
                         progressDialog.setMessage("Please Wait");
                         progressDialog.show();
                         FirebaseMessaging.getInstance().subscribeToTopic("GetNotification");
                         FirebaseInstanceId.getInstance().getId();
                         BackgroundTask1 backgroundTask1 = new BackgroundTask1();
                         backgroundTask1.execute(email);
                     }

                    } else {

                        reg_pass1=(TextView)findViewById(R.id.reg_pass1);
                        reg_pass2=(TextView)findViewById(R.id.reg_pass2);
                        reg_pass1.setError("Error");
                        reg_pass2.setError("Error");


                    }}else{
                        reg_phone=(TextView)findViewById(R.id.reg_Phone);
                        reg_phone.setError("Error");

                    }
                }
                else{
                    reg_email=(TextView)findViewById(R.id.reg_email);
                    reg_email.setError("Error");


                }
            }}

            }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG);
            toast.show();
        }


    }


    class BackgroundTask extends AsyncTask<String,Void,String>{
        String add_info_url;
        @Override
        protected void onPreExecute() {
           add_info_url="https://studenthubdaljit.000webhostapp.com/insert.php";
        }

        @Override
        protected String doInBackground(String... args){
            String name,acctype,email,password,mobile,question,answer;
            name = args[0];
            acctype=args[1];
            email=args[2];
            password=args[3];
            mobile=args[4];
            question=args[5];
            answer=args[6];

            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("acctype", "UTF-8") + "=" + URLEncoder.encode(acctype, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8")+"&"+
                        URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&" +
                        URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(answer, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Successfully Registered";
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
            }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String Result) {
            Toast toast = Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(Register.this,Login.class);
            startActivity(intent);
        }
    }


    class BackgroundTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/forgotsearch.php";
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
            if (Objects.equals(result, "2")) {

                progressDialog.dismiss();
                Toast toast = Toast.makeText(getApplication(),"ID No already Registered",Toast.LENGTH_SHORT);
                toast.show();



            } else {
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute(name,type, email, password1, mobile,securityquestionstring,securityansstring);


            }

        }


    }

    public void login(View view){
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }



}
