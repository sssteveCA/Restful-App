package com.example.restfulactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.restfulactivity.dialog.MyDialog;
import com.example.restfulactivity.fragments.AllPersonaFragment;
import com.example.restfulactivity.fragments.CreatePersonaFragment;
import com.example.restfulactivity.fragments.DeletePersonaFragment;
import com.example.restfulactivity.fragments.EditPersonaFragment;
import com.example.restfulactivity.fragments.GetPersonaFragment;
import com.example.restfulactivity.fragments.HomeFragment;
import com.example.restfulactivity.fragments.SelectFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SelectFragment.passaPosizione {

    private Button bt_esci;
    private RelativeLayout mainLayout; //activity_main.xml
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity","onCreate");
        setContentView(R.layout.activity_main);
        //RelativeLayout activity_main.xml
        mainLayout = findViewById(R.id.mainLayout);

        bt_esci = findViewById(R.id.bt_esci);
        bt_esci.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity","onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","onDestroy");
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener
        switch(view.getId()){
            case R.id.bt_esci:
                MyDialog myd = new MyDialog(this,"Esci dall'app","Sei sicuro di voler uscire?",R.drawable.ic_warning);
                myd.setYesNoDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void cambia(int pos) {
       //SelectFragment.passaPosizione
        Log.d("MainActivity","posizione => "+pos);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.setReorderingAllowed(true);
        switch(pos){
            case 0: //home
                ft.replace(R.id.outputFragment, HomeFragment.class,null);
                break;
            case 1: //tutte le persone registrate
                ft.replace(R.id.outputFragment, AllPersonaFragment.class,null);
                break;
            case 2: //una persona registrata a scelta
                ft.replace(R.id.outputFragment, GetPersonaFragment.class,null);
                break;
            case 3: //nuova persona
                ft.replace(R.id.outputFragment, CreatePersonaFragment.class,null);
                break;
            case 4: //modifica persona
                ft.replace(R.id.outputFragment, EditPersonaFragment.class,null);
                break;
            case 5: //elimina persona
                ft.replace(R.id.outputFragment, DeletePersonaFragment.class,null);
                break;
            default:
                break;
        }
        ft.commit();
    }
}