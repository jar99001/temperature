package com.allander.johan.remotetemperature;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by johan on 2015-10-23.
 */
public class HelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Dra fingret över diagrammet för att få det exakta datumet och temperatur för tidpunken.").setNeutralButton("Stäng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close square
            }
        });

        return builder.create();
    }
}
