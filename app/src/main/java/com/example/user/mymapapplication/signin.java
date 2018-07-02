package com.example.user.mymapapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class signin extends AppCompatActivity  {


    TextView t1;
    EditText uid, pass;
    static String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        uid = (EditText) findViewById(R.id.etu);
        pass = (EditText) findViewById(R.id.etp);
          }


    public void b4_click(View view) {
        sender=uid.getText().toString();
        Intent intent = new Intent(getApplicationContext(), forgotpassword.class);
        startActivity(intent);
    }



    public void b11_click(View view) {


        String user="";
                String pwd="";
        user = uid.getText().toString();
        pwd = pass.getText().toString();
        //Toast.makeText(getApplicationContext(),user, Toast.LENGTH_SHORT).show();
       new ExecuteTask().execute(user, pwd);

    }



    class ExecuteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            String res = FetchData(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            if (result.trim().equals("success")) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                 intent.putExtra("username", uid.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }




        }
        public String FetchData(String[] valuse) {

            String s = "";
            //String url = "http://10.0.2.2:8181/android/log.php";
          String url = "http://192.168.43.166:8181/android/log.php";
           // String url="http://roudra-application-server.000webhostapp.com/log.php";
            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("user_id", valuse[0]));
                list.add(new BasicNameValuePair("password", valuse[1]));
                //end new data
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                s = readdata(httpResponse);
            } catch (Exception exception) {

            }
            return s;

        }

        public String readdata(HttpResponse res) {
            InputStream is = null;
            String return_text = "";
            try {
                is = res.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                return_text = sb.toString();
            } catch (Exception e) {

            }
            return return_text;
        }


    }
}
