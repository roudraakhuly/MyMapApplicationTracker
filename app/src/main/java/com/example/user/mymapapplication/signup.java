package com.example.user.mymapapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

public class signup extends AppCompatActivity {

    EditText username, user_id, password, answer, mobile;
    Spinner sp;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.et1);
        user_id = (EditText) findViewById(R.id.et2);
        mobile = (EditText) findViewById(R.id.et3);
        password = (EditText) findViewById(R.id.et4);
        sp = (Spinner) findViewById(R.id.sp1);
        answer = (EditText) findViewById(R.id.et5);
        t1 = (TextView) findViewById(R.id.tw);
        t1.setText("ENTER MINIMUM OF 8 CHARACTERS");

    }

    public void b7_click(View view) {
        String str;
        str = password.getText().toString();

        int l = 0;
        l = str.length();
        if (l < 8)
            t1.setText("ENTERED LESS THAN 8 CHARACTERS");
        else {

            String uname = username.getText().toString();
            String u_id = user_id.getText().toString();
            String mob = mobile.getText().toString();
            String pass = password.getText().toString();
            String secques = sp.getSelectedItem().toString();
            String ans = answer.getText().toString();
            new ExecuteTask().execute(uname, u_id, mob, pass, secques, ans);


        }
    }



    class ExecuteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            String res = FetchData(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("success")) {
                Intent intent = new Intent(getApplicationContext(),signin.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
            }

        }


        public String FetchData(String[] valuse) {

            String s = "";
           String url = "http://192.168.43.166:8181/android/register.php";
           //String url = "http://10.0.2.2:8181/android/register.php";
           //String url="http://roudra-application-server.000webhostapp.com/register.php";

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("username", valuse[0]));
                list.add(new BasicNameValuePair("user_id", valuse[1]));
                list.add(new BasicNameValuePair("mobile", valuse[2]));
                list.add(new BasicNameValuePair("password", valuse[3]));
                list.add(new BasicNameValuePair("securityquestion", valuse[4]));
                list.add(new BasicNameValuePair("answer", valuse[5]));
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
