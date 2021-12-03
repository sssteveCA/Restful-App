package com.example.restfulactivity.tabelle;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//Tabella che contiene le informazioni ottenute dalla risposta del server con i metodi GET
public class MyTable extends TableLayout {

    public static final float DIM_TESTO = 18; //dimensione del testo in sp
    public static final int PADDING = 20; //padding in px

    private Context context;
    private TableLayout.LayoutParams lp;
    private TableRow tr;
    private TextView tv;

    public MyTable(Context context) {
        super(context);
        this.context = context;
        lp = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    public TableRow getRow(){return this.tr;}

    //aggiunge una nuova riga alla tabella
    public boolean addRow(){
        boolean ok = false;
        if(this.tr != null){
            //se la TableRow è stata creata viene aggiunta alla tabella
            this.addView(this.tr);
            ok = true;
        }
        return ok;
    }

    //crea una nuova riga
    public void createRow(){
        this.tr = new TableRow(this.context);
        TableRow.LayoutParams trlp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.tr.setLayoutParams(trlp);
    }

    //aggiunge una View alla riga della tabella
    public boolean addToRow(View v){
        boolean ok = false;
        if(this.tr != null && v != null){
            //la View (se diversa da null) viene aggiunta alla TableRow solo se esiste
            this.tr.addView(v);
            ok = true;
        }
        return ok;

    }

    //crea una nuova TextView da aggiungere alla TableRow
    public TextView createTv(String testo, float size, Typeface tf,int p){
        this.tv = new TextView(this.context);
        this.tv.setText(testo);
        this.tv.setTextSize(size);
        this.tv.setTypeface(tf);
        this.tv.setPadding(p,p,p,p);
        return this.tv;
    }

}
