package com.djsaiyesh.team.studenthub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
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

public class ArticlePost extends Activity {

    TextView post;
    String postt,feel,year,month,date,hour,minute,posttime;
    Spinner feeling;
    Button post_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .3));

        Calendar calendar = Calendar.getInstance();

              month = getMonthName(calendar.get(Calendar.MONTH)+1);

              date = Integer.toString(calendar.get(Calendar.DATE));
              hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
              minute=Integer.toString(calendar.get(Calendar.MINUTE));



           StringBuilder sb=new StringBuilder(20).append(date).append(" ").append(month).append(" at ").append(hour).append(":").append(minute);
                 posttime = sb.toString();

            post_button = (Button)findViewById(R.id.article_post_button);
           post= (TextView)findViewById(R.id.post);
           feeling=(Spinner)findViewById(R.id.feeling);


    }
    public void poststatus(View view){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {



            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String sidno = pref.getString("idno","");
            String sname = pref.getString("name","");
            postt = post.getText().toString();

            if(postt.equals("")){
                post.setError("Empty Field");

            }else {

                feel = String.valueOf(feeling.getSelectedItem());
                post_button.setEnabled(false);
                BackgroundTask1 backgroundTask1 = new BackgroundTask1();
                backgroundTask1.execute(sidno, postt, feel,sname,posttime);
            }
        }else
        {

            Toast toast = Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG);
            toast.show();
        }


    }


    class BackgroundTask1 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... args) {
            String login_url = "https://studenthubdaljit.000webhostapp.com/poststatus.php";
            String email,data1,data2,data3,data4;
            email = args[0];
            data1 = args[1];
            data2 =args[2];
            data3=args[3];
            data4=args[4];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("data1", "UTF-8") + "=" + URLEncoder.encode(data1, "UTF-8")+"&"+
                        URLEncoder.encode("data3", "UTF-8") + "=" + URLEncoder.encode(data3, "UTF-8")+"&"+
                        URLEncoder.encode("data4", "UTF-8") + "=" + URLEncoder.encode(data4, "UTF-8")+"&"+
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

            Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);

        }




    }


    public static String getMonthName(int month){
        switch(month){
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }


}
