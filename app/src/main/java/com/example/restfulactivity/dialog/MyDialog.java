package com.example.restfulactivity.dialog;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.restfulactivity.ExitActivity;

public class MyDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener {
    private Context context;
    private String title;
    private String message;
    private int icon;

    public MyDialog(@NonNull Context context, String title, String message, int icon) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.icon = icon;
    }

    //crea una finestra di dialogo con i bottoni Sì e No
    public void setYesNoDialog(){
        this.setTitle(this.title);
        this.setMessage(this.message);
        this.setIcon(this.icon);
        this.setCancelable(false);
        this.setPositiveButton("SÌ",this);
        this.setNegativeButton("NO",this);
        AlertDialog ad = this.create();
        ad.show();
    }

    //crea una finestra di dialogo che mostra un messaggio
    public void setOkDialog(){
        this.setTitle(this.title);
        this.setMessage(this.message);
        this.setIcon(this.icon);
        this.setCancelable(false);
        this.setNegativeButton("OK",this);
        AlertDialog ad = this.create();
        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        //DialogInterface.OnClickListener
        Log.d("MyDialog","Dialog onClick");
        switch(i){
            case BUTTON_POSITIVE:
                //Chiusura del'app
                Intent exitIntent = new Intent(this.context, ExitActivity.class);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                this.context.startActivity(exitIntent);
                break;
            case BUTTON_NEGATIVE:
                dialogInterface.dismiss();
                break;
        }
    }

}
