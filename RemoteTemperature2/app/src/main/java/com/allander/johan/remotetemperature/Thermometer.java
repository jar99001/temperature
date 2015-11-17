package com.allander.johan.remotetemperature;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Thermometer extends Activity {
    private CanvasView customCanvas;
    ImageView helpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature);

        final Context context = this;

        helpBtn = (ImageView) findViewById(R.id.helpButton);
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        TextView idagBtn = (TextView) findViewById(R.id.idag);
        TextView veckaBtn = (TextView) findViewById(R.id.vecka);
        TextView manadBtn = (TextView) findViewById(R.id.manad);
        TextView arBtn = (TextView) findViewById(R.id.ar);

        idagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                new Thread(new ClientThread("DAY")).start();
            }
        });

        veckaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                new Thread(new ClientThread("WEEK")).start();
            }
        });

        manadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                new Thread(new ClientThread("MONTH")).start();
            }
        });

        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                new Thread(new ClientThread("YEAR")).start();
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new HelpDialog();
                dialog.show(getFragmentManager(), "Information");
            }
        });
    }


    class ClientThread implements Runnable {

        InputStream is = null;
        String result = "";
        JSONArray jArray;
        ArrayList<ValuePoint> graphValues = new ArrayList<ValuePoint>();
        float largestValue = 0;
        float smallestValue = -200;
        String intervall;

        ClientThread(String _intervall) {
            intervall = _intervall;
        }

        @Override
        public void run() {
            // Erase all old values and points
            graphValues.clear();
            customCanvas.clearPoints();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("intervall",intervall));

            //http post
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://allander.duckdns.org/info.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                is.close();

                result = sb.toString();
            }catch(Exception e){
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            //parse json data
            try{
                jArray = new JSONArray(result);
//                values = new double[jArray.length()];
                for(int i=0;i<jArray.length();i++) {
                    final JSONObject json_data = jArray.getJSONObject(i);
                    Time tid = Time.valueOf(json_data.getString("klockslag"));
                    Date datum = Date.valueOf(json_data.getString("datum"));
                    double tempV = json_data.getDouble("temperatur");
                    if(smallestValue==-200) smallestValue = (float) tempV; // Init smallestValue;
                    if(tempV>largestValue) largestValue = (float) tempV;        // Get largest Y-value from data
                    if(tempV<smallestValue && tempV!=-127f) smallestValue = (float) tempV;      // Get smallest Y-value from data
                    if(tempV!=-127f) graphValues.add(new ValuePoint(datum,tid,tempV));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //eTUpdate.setText((String.format("Largest value: %f\n", largestValue)));
                        //eTUpdate.append(String.format("Smallest value: %f\n", smallestValue));
                        customCanvas.setYSize(largestValue,smallestValue);
                        for(int a=0;a<graphValues.size();a++)
                            customCanvas.setPoint(a, (float) graphValues.get(a).temperatur, graphValues.size(), graphValues.get(a).datum, (float) graphValues.get(a).temperatur, graphValues.get(a).tid);
                        }
                });

//                gettingValues = false;
            } catch(JSONException e) {
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

        }
    }
}
