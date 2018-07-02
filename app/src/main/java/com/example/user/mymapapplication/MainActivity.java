package com.example.user.mymapapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout d1;
    private ActionBarDrawerToggle abdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d1=(DrawerLayout)findViewById(R.id.d1);
        abdt=new ActionBarDrawerToggle(this,d1,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();

        final NavigationView nav_Vier=(NavigationView)findViewById(R.id.nav_view);

        nav_Vier.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.signup)
                {
                    Intent intent =new Intent(getApplicationContext(),signup.class);
                    startActivity(intent);

                }
                if(id==R.id.signin)
                {
                    Intent intent =new Intent(getApplicationContext(),signin.class);
                    startActivity(intent);
                }
                if(id==R.id.track)
                {
                    Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                }



                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void b1_Click(View view)
    {
        Toast.makeText(getApplicationContext(),"hey bro welcome to sign in",Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(getApplicationContext(),signin.class);
        startActivity(intent);
    }


    public void b2_click(View v)
    {
        Toast.makeText(getApplicationContext(),"hey bro welcome to sign up",Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(getApplicationContext(),signup.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        //Put up the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CANNOT GO BACK FOR SAFETY?")
                .setMessage("WANT TO EXIT FROM THE APPLICATION?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                      /*  System.exit(0);
                        finish(); */
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    }
                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();

    }
}
