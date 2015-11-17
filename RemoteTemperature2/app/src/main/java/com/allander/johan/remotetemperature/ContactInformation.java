package com.allander.johan.remotetemperature;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactInformation extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactinformation);

        // Activate link in Android text
        TextView cvBtn = (TextView) findViewById(R.id.cv);
        cvBtn.setMovementMethod(LinkMovementMethod.getInstance());

        TextView kandidatBtn = (TextView) findViewById(R.id.kandidat);
        kandidatBtn.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
