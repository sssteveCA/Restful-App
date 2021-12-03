package com.example.restfulactivity.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restfulactivity.MainActivity;
import com.example.restfulactivity.R;
import com.example.restfulactivity.dialog.MyDialog;
import com.example.restfulactivity.retrofit.Persona;
import com.example.restfulactivity.retrofit.RetrofitService;
import com.example.restfulactivity.tabelle.MyTable;
import com.example.restfulactivity.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetPersonaFragment extends Fragment implements View.OnClickListener,RetrofitService.RispostaRetrofit {

    private LinearLayout ll_get_table; //layout che contiene la tabella con le informazioni richieste
    private EditText et_get;
    private Button bt_get;
    private Persona persona;
    private MyTable tl_get;
    private ProgressBar pb_get;
    private MyDialog dialogError;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GetPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetPersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetPersonaFragment newInstance(String param1, String param2) {
        GetPersonaFragment fragment = new GetPersonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_persona, container, false);
        et_get = v.findViewById(R.id.et_get);
        bt_get = v.findViewById(R.id.bt_get);
        ll_get_table = v.findViewById(R.id.ll_get_table);
        pb_get = v.findViewById(R.id.pb_get);
        bt_get.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener
        switch(view.getId()){
            case R.id.bt_get:
                String getId = et_get.getText().toString();
                if(Utils.connesso(getActivity())){
                    if(Utils.numero(getId)){
                        pb_get.setVisibility(ProgressBar.VISIBLE);
                        RetrofitService retrofitService = new RetrofitService(RetrofitService.ipLocale);
                        retrofitService.delega = this;
                        retrofitService.getPersona(Integer.parseInt(getId));
                    }//if(Utils.numero(getId)){
                    else{
                        Toast.makeText(getActivity(),"Devi inserire un numero per continuare",Toast.LENGTH_LONG).show();
                    }
                }//if(Utils.connesso(getActivity())){
                else{
                    Toast.makeText(getActivity(),"Collegati ad internet per continuare",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void risposta(Response response) throws CloneNotSupportedException {
        //RetrofitService.RispostaRetrofit
        pb_get.setVisibility(ProgressBar.GONE);
        if(response.isSuccessful()) {
            JsonElement risp = (JsonElement) response.body();
            JsonObject json = risp != null ? risp.getAsJsonObject() : null;
            if(json != null){
                int id = json.get("id").getAsInt();
                String nome = json.get("nome").getAsString();
                String cognome = json.get("cognome").getAsString();
                byte eta = json.get("età").getAsByte();
                short altezza = json.get("altezza").getAsShort();
                String residenza = json.get("residenza").getAsString();
                this.persona = new Persona(id,nome,cognome,eta,altezza,residenza);
                /*Log.d("GetPersonaFragment", "ID => " + this.persona.getId());
                Log.d("GetPersonaFragment", "Nome => " + this.persona.getNome());
                Log.d("GetPersonaFragment", "Cognome => " + this.persona.getCognome());
                Log.d("GetPersonaFragment", "Età => " + this.persona.getEtà());
                Log.d("GetPersonaFragment", "Altezza => " + this.persona.getAltezza());
                Log.d("GetPersonaFragment", "Residenza => " + this.persona.getResidenza());*/
                ll_get_table.removeAllViews();
                //creo la tabella
                tl_get = new MyTable(getActivity());
                //creo la riga per la tabella
                //Riga ID
                tl_get.createRow();
                //creo la TextView
                TextView tv_Tabella = tl_get.createTv("ID",MyTable.DIM_TESTO, Typeface.DEFAULT_BOLD,MyTable.PADDING);
                //Aggiungo la TextView alla riga
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(String.valueOf(this.persona.getId()),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                //aggiungo la riga alla tabella
                tl_get.addRow();
                //Riga Nome
                tl_get.createRow();
                tv_Tabella = tl_get.createTv("NOME",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(this.persona.getNome(),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tl_get.addRow();
                //Riga cognome
                tl_get.createRow();
                tv_Tabella = tl_get.createTv("COGNOME",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(this.persona.getCognome(),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tl_get.addRow();
                //Riga età
                tl_get.createRow();
                tv_Tabella = tl_get.createTv("ETÀ",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(String.valueOf(this.persona.getEtà()+" anni"),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tl_get.addRow();
                //Riga altezza
                tl_get.createRow();
                tv_Tabella = tl_get.createTv("ALTEZZA",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(String.valueOf(this.persona.getAltezza()+" cm"),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tl_get.addRow();
                //Riga residenza
                tl_get.createRow();
                tv_Tabella = tl_get.createTv("RESIDENZA",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tv_Tabella = tl_get.createTv(this.persona.getResidenza(),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                tl_get.addToRow(tv_Tabella);
                tl_get.addRow();
                //aggiungo la tabella al layout
                ll_get_table.addView(tl_get);
            }//if(json != null){
            else {
                Log.d("GetPersonaFragment","json = null");
            }
        }//if(response.isSuccessful()){
        else{
            try {
                //Messaggio di errore dal server
                String msg = RetrofitService.messaggioErrore(response);
                Log.d("GetPersonaFragment","Errore => "+msg);
                dialogError = new MyDialog(getActivity(),"Errore",msg,R.drawable.ic_error);
                dialogError.setOkDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//public void risposta(Response response) throws CloneNotSupportedException {

    @Override
    public void fallito(Throwable t) {
        pb_get.setVisibility(ProgressBar.GONE);
        Log.d("GetPersonaFragment","Errore => "+t.getMessage());
        dialogError = new MyDialog(getActivity(),"Errore",t.getMessage(),R.drawable.ic_error);
        dialogError.setOkDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("GetPersonaFragment","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("GetPersonaFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GetPersonaFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GetPersonaFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("GetPersonaFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("GetPersonaFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("GetPersonaFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GetPersonaFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("SelectFragment","onDetach");
    }
}