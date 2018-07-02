package com.example.user.mymapapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class imageshow extends Activity {

    Bitmap originalBitmap,image;
    ImageView iv_ttx;
    EditText et_sample;
    Paint paint;
    Button back;
    EditText pname,address,year,cost;
    Button savedb;
    ProgressBar progressBar;
    static String pid="";
    static String fname="";
    String picturePath;
    String ba1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageshow);

        iv_ttx = (ImageView) findViewById(R.id.iv_ttx);
        pname=(EditText) findViewById(R.id.editText1);
        address=(EditText) findViewById(R.id.editText2);
        year=(EditText) findViewById(R.id.editText3);
        cost=(EditText) findViewById(R.id.editText4);
        savedb=(Button) findViewById(R.id.button1);


        //progess_msz.setVisibility(View.GONE);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        back=(Button)findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
//to get screen width and hight
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//dimentions x,y of device to create a scaled bitmap having similar dimentions to screen size
        int height1 = displaymetrics.heightPixels;
        int width1 = displaymetrics.widthPixels;
//paint object to define paint properties
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setTextSize(25);
        //String RootDir = Environment.getExternalStorageDirectory() + File.separator + "txt_imgs/Image-3180.jpg";
//loading bitmap from drawable
        originalBitmap = BitmapFactory.decodeFile(MapsActivity.imgfile);
//scaling of bitmap
        originalBitmap =Bitmap.createScaledBitmap(originalBitmap, width1, height1, false);
//creating anoter copy of bitmap to be used for editing
        image = originalBitmap.copy(Bitmap.Config.RGB_565, true);

        Button btn_save_img = (Button) findViewById(R.id.btn_save_image);
        btn_save_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//funtion save image is called with bitmap image as parameter

                float scr_x = 30;
                float scr_y = 200;
//funtion called to perform drawing
                String str=MapsActivity.add;String str1="",str2="",str3="";
                int lstr=0;
                lstr=str.length();
                str1=str.substring(0,lstr/3);
                str2=str.substring(lstr/3,2*lstr/3+1);
                createImage(scr_x, scr_y,str1);
                scr_x=30;
                scr_y=300;
                createImage(scr_x,scr_y,str2);
                scr_x=30;
                scr_y=450;
                str3=str.substring(2*lstr/3+1,lstr);
//funtion called to perform drawing
                createImage(scr_x, scr_y, str3);
                saveImage(image);
            }
        });


    }

    public void btn_savedb(View v)
    {
        String pn=pname.getText().toString();
        String ad=address.getText().toString();
        String yr=year.getText().toString();
        String ct=cost.getText().toString();
        //  Toast.makeText(this,pn+ad,Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);



        new ExecuteTask().execute(pn,ad,yr,ct);

    }

    class ExecuteTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... params) {

            String res=PostData(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            //progess_msz.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
        }

    }

    public String PostData(String[] valuse) {
        String s="";
        try
        {
            HttpClient httpClient=new DefaultHttpClient();
             //  HttpPost httpPost=new HttpPost("http://10.0.2.2:80/webproject-tracker/saveproject.php");
           HttpPost httpPost=new HttpPost("http://192.168.43.166:8181/android/saveprojectapp.php");
          // HttpPost httpPost=new HttpPost("http://roudra-application-server.000webhostapp.com/saveprojectapp.php");
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("pname", valuse[0]));
            list.add(new BasicNameValuePair("address",valuse[1]));
            list.add(new BasicNameValuePair("year", valuse[2]));
            list.add(new BasicNameValuePair("cost",valuse[3]));
            //new data
            list.add(new BasicNameValuePair("projectid",pid));
            list.add(new BasicNameValuePair("lt",String.valueOf(MapsActivity.lat)));
            list.add(new BasicNameValuePair("lg",String.valueOf(MapsActivity.lag)));
            list.add(new BasicNameValuePair("imgname",fname));

            // image send data
            BitmapDrawable drawable = (BitmapDrawable) iv_ttx.getDrawable();
            Bitmap btm = drawable.getBitmap();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            list.add(new BasicNameValuePair("image", encodedImage));

            // Toast.makeText(getApplicationContext(),encodedImage,Toast.LENGTH_LONG).show();



            //end new data
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=  httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);

        }
        catch(Exception exception)  {}
        return s;

    }

    public String readResponse(HttpResponse res) {
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
    void saveImage(Bitmap img) {

        File myDir=new File(MapsActivity.mydir);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        fname = n+"-"+String.valueOf(MapsActivity.lat)+".jpg";
        pid=n+"-"+String.valueOf(MapsActivity.lag);
        File file = new File (myDir, fname);
        picturePath=file.toString();
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap createImage(float scr_x,float scr_y,String user_text){
        //canvas object with bitmap image as constructor
        Canvas canvas = new Canvas(image);
        int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //removing title bar hight
        scr_y=scr_y- viewTop;
        //fuction to draw text on image. you can try more drawing funtions like oval,point,rect,etc...
        canvas.drawText("" + user_text, scr_x, scr_y, paint);
        iv_ttx.setImageBitmap(image);
        return image;
    }



}
