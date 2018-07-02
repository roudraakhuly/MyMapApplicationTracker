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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class forgotpassword extends AppCompatActivity {

    TextView tw3;
    EditText ans;
    Button btn;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        tw3=(TextView)findViewById(R.id.tw3);
        ans=(EditText)findViewById(R.id.ans);
        btn=(Button)findViewById(R.id.press);
        tw3.setText("Press The Button Below to Get the Security Question!!!");
        btn.setText("press");
         }

    public void c_click(View v)
    {
        String u=signin.sender;

        if(btn.getText().toString()=="press")
        {
            new ExecuteTask().execute(u);
            btn.setText("Check AND Proceed");

        }
        else if (btn.getText().toString()=="Check AND Proceed")
        {
            if((ans.getText().toString()).equals(check))
            {
                Intent i=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
            else Toast.makeText(getApplicationContext(),"INCORRECT ANSWER ENTERED!!!", Toast.LENGTH_SHORT).show();

        }

    }

    private void fetch(String response) {
        try
        {
            JSONArray array=new JSONArray(response);
            int size=array.length();
            String[] user_id=new String[size];
            String[] securityquestion=new String[size];
            String[] answer=new String[size];

            for(int i=0;i<array.length();i++) {
                JSONObject obj = array.getJSONObject(i);
                user_id[i]=obj.getString("user_id");
                securityquestion[i]=obj.getString("securityquestion");
                answer[i]=obj.getString("answer");
                check=answer[i];

                // String passdata=securityquestion[i];

                tw3.setText(securityquestion[i]);
                Toast.makeText(getApplicationContext(), check, Toast.LENGTH_SHORT).show();
            }

        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "fuck you", Toast.LENGTH_SHORT).show();

        }
    }
    class ExecuteTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {

            String res=FetchData(params);
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            fetch(result);
        }
    }
    public String FetchData(String[] valuse) {

        String s="";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.166:8181/android/idea.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("userid", valuse[0]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            s = readdata(httpResponse);

        } catch (Exception exception) {
        }
        return s;
    }
    public String readdata(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;
    }
}
