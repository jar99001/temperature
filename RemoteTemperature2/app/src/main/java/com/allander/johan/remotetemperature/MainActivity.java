package com.allander.johan.remotetemperature;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

//import android.media.Image;

public class MainActivity extends Activity {
    LinearLayout thermometerText;
    LinearLayout projectInformation;
    LinearLayout contactText;
//    ProgressBar pBTherm;

    final Context context = this;
  //  BackgroundSound mBackgroundSound = new BackgroundSound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thermometerText = (LinearLayout) findViewById(R.id.thermometer);
        projectInformation = (LinearLayout) findViewById(R.id.projectText);
        contactText = (LinearLayout) findViewById(R.id.contactText);
//        pBTherm = (ProgressBar) findViewById(R.id.tProg);


        //final MediaPlayer pling = MediaPlayer.create(this, R.raw.pling);
//        final MediaPlayer applause = MediaPlayer.create(this, R.raw.applause);

        thermometerText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //applause.start();

                // Show wait icon
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(100);

//                int height = pBTherm.getHeight();
//                pBTherm.setLayoutParams(new LinearLayout.LayoutParams(height, height));

                // Init network connection

                Intent i = new Intent(context, Thermometer.class);
                startActivity(i);
            }
        });

        projectInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(100);

                Intent i = new Intent(context, ProjectInformation.class);
                startActivity(i);
            }
        });

        contactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(100);

                Intent i = new Intent(context, ContactInformation.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mBackgroundSound.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mBackgroundSound.cancel(true);
    }
/*
    public class BackgroundSound extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
//            final MediaPlayer bgSound = MediaPlayer.create(MainActivity.this, R.raw.elevator);
//            bgSound.setVolume(20, 20);
//            bgSound.start();

            return null;
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
