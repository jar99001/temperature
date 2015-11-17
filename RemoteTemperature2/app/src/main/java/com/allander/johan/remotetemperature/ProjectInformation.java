package com.allander.johan.remotetemperature;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by johan on 2015-10-09.
 */
public class ProjectInformation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectinformation);

        LinearLayout firstBtn = (LinearLayout) findViewById(R.id.firstInfo);
        final TextView firstExtBtn = (TextView) findViewById(R.id.firstExtendedInfo);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstExtBtn.getHeight()==0) {
                    firstExtBtn.setHeight(14);
                } else {
                    firstExtBtn.setHeight(0);
                }
            }
        });

    }
}
