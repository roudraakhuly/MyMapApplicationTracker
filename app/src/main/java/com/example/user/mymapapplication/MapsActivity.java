package com.example.user.mymapapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    //Our Map
    LocationManager locationManager;
    String mprovider;
    Location location;

    GoogleMap googlemap;
    public static String add="";
    public static String loc = "";
    public static String imgfile = "";
    public static String mydir = "";
    public static double lat = 0.0;
    public static double lag = 0.0;
    String[] menu;
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    Bitmap originalBitmap, image;
    ImageView iv_ttx;
    EditText et_sample;
    Paint paint;
    TextView latlng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        menu = new String[]{"MAP", "CAMERA", "LIBRARY", "SAVE", "LOG OUT"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        dList.setAdapter(adapter);

        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                String a = menu[position];
                //Toast.makeText(getApplicationContext(),a,Toast.LENGTH_LONG).show();
                if (a == "CAMERA") {
                    mydir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
                    File newdir = new File(mydir);
                    newdir.mkdirs();
                    count++;
                    String file = mydir + String.valueOf(lat) + count + ".jpg";
                    imgfile = mydir + String.valueOf(lat) + count + ".jpg";
                    File newfile = new File(file);
                    try {
                        newfile.createNewFile();

                    } catch (IOException e) {
                    }
                    Uri outputFileUri = Uri.fromFile(newfile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                }
                if (a == "LIBRARY") {
                    Toast.makeText(getApplication(), "library", Toast.LENGTH_SHORT).show();
                    Intent pic = new Intent(Intent.ACTION_PICK);
                    pic.setType("image/*");
                    startActivity(pic);

                }
                if (a == "MAP") {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
                if (a == "SAVE") {
                    Intent it = new Intent(MapsActivity.this, imageshow.class);
                    startActivity(it);
                }

                if (a == "LOG OUT") {
                    Intent it = new Intent(MapsActivity.this, signin.class);
                    startActivity(it);
                }
            }

        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getBaseContext(), "No Location permission not atllow ", Toast.LENGTH_SHORT).show();
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider 2", Toast.LENGTH_SHORT).show();


        }

    }
    @Override
    public void onLocationChanged(Location location) {
            latlng = (TextView) findViewById(R.id.ltlg);

        //  TextView latitude = (TextView) findViewById(R.id.textView1);

        //latlng.setText("Current Longitude:" + location.getLongitude()+"   Current Latitude:" + location.getLatitude());
        loc="LGT:" + location.getLongitude()+"/   LTD:" + location.getLatitude();
        //  latitude.setText("Current Latitude:" + location.getLatitude());
        // now Toast.makeText(this,"Current Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();
        lat=location.getLatitude();lag=location.getLongitude();

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub
    }

    public void btn_current(View v)
    {
          Toast.makeText(this,"Current Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);

            add = add + "\n" + obj.getSubAdminArea();
            Calendar c=Calendar.getInstance();
            c.getTime().toString();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add, Toast.LENGTH_SHORT).show();
            add=add+"-"+c.getTime().toString();

            latlng.setText(add);
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getApplication(),String.valueOf(loc),Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK ) {
            Toast.makeText(getApplication(), String.valueOf("please wait .. then click save"), Toast.LENGTH_LONG).show();
            Canvas canvas = new Canvas();
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(28);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(loc, 100, 100, paint);
            Log.d("CameraDemo", "Pic saved");


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        googlemap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mylocation = new LatLng(lat,lag);

        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mylocation)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googlemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googlemap.addMarker(new MarkerOptions().position(mylocation).title("Marker at your position").draggable(true));



    }
}