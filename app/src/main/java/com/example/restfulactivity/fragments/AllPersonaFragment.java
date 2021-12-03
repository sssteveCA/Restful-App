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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restfulactivity.MainActivity;
import com.example.restfulactivity.R;
import com.example.restfulactivity.dialog.MyDialog;
import com.example.restfulactivity.retrofit.Persona;
import com.example.restfulactivity.retrofit.RetrofitService;
import com.example.restfulactivity.tabelle.MyTable;
import com.example.restfulactivity.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPersonaFragment extends Fragment implements RetrofitService.RispostaRetrofit {

    private LinearLayout ll_all_table; //layout che contiene la tabella con le informazioni richieste
    private List<Persona> persone;
    private MyTable tl_all;
    private ProgressBar pb_all;
    private MyDialog dialogError;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllPersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllPersonaFragment newInstance(String param1, String param2) {
        AllPersonaFragment fragment = new AllPersonaFragment();
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
        View v = inflater.inflate(R.layout.fragment_all_persona, container, false);
        ll_all_table = v.findViewById(R.id.ll_all_table);
        pb_all = v.findViewById(R.id.pb_all);
        persone = new LinkedList<>();
        if(Utils.connesso(getActivity())){
            pb_all.setVisibility(ProgressBar.VISIBLE);
            RetrofitService retrofitService = new RetrofitService(RetrofitService.ipLocale);
            retrofitService.delega = this;
            retrofitService.getPersonas();
        }//if(Utils.connesso(getActivity())){
        else{
            Toast.makeText(getActivity(),"Collegati ad internet per continuare",Toast.LENGTH_LONG).show();
        }
        return v;
    }

    @Override
    public void risposta(Response response) {
        //RetrofitService.RispostaRetrofit
        pb_all.setVisibility(ProgressBar.GONE);
        if(response.isSuccessful()){
            JsonElement risp = (JsonElement) response.body();
            //mostra la lista delle persone trovate attraverso un array JSON
            JsonArray jsonArray = risp != null ? risp.getAsJsonArray() : null;
            if(jsonArray != null){
                persone.clear();
                int size = jsonArray.size();
                if(size > 0){
                    ll_all_table.removeAllViews();
                    //creo la tabella
                    tl_all = new MyTable(getActivity());
                    //Riga di intestazione
                    //creo la riga
                    tl_all.createRow();
                    //creo la TextView
                    TextView tv_tabella = tl_all.createTv("ID",MyTable.DIM_TESTO, Typeface.DEFAULT_BOLD,MyTable.PADDING);
                    //aggiungo la TextView alla riga
                    tl_all.addToRow(tv_tabella);
                    tv_tabella = tl_all.createTv("NOME",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                    tl_all.addToRow(tv_tabella);
                    tv_tabella = tl_all.createTv("COGNOME",MyTable.DIM_TESTO,Typeface.DEFAULT_BOLD,MyTable.PADDING);
                    tl_all.addToRow(tv_tabella);
                    //aggiungo la riga alla tabella
                    tl_all.addRow();
                    //Viene creata una lista di oggetti Persona dall'array JSON
                    for(int n = 0; n < jsonArray.size(); n++){
                        JsonObject jsonPersona = jsonArray.get(n).getAsJsonObject();
                        int id = jsonPersona.get("id").getAsInt();
                        String nome = jsonPersona.get("nome").getAsString();
                        String cognome = jsonPersona.get("cognome").getAsString();
                        byte eta = jsonPersona.get("età").getAsByte();
                        short altezza = jsonPersona.get("altezza").getAsShort();
                        String residenza = jsonPersona.get("residenza").getAsString();
                        Persona p = new Persona(id,nome,cognome,eta,altezza,residenza);
                        /*Log.d("AllPersonaFragment", "ID => " + p.getId());
                        Log.d("AllPersonaFragment", "Nome => " + p.getNome());
                        Log.d("AllPersonaFragment", "Cognome => " + p.getCognome());
                        Log.d("AllPersonaFragment", "Età => " + p.getEtà());
                        Log.d("AllPersonaFragment", "Altezza => " + p.getAltezza());
                        Log.d("AllPersonaFragment", "Residenza => " + p.getResidenza());*/
                        persone.add(p);
                        //riga tabella con le informazioni di una singola persona
                        tl_all.createRow();
                        tv_tabella = tl_all.createTv(String.valueOf(p.getId()),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                        tl_all.addToRow(tv_tabella);
                        tv_tabella = tl_all.createTv(p.getNome(),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                        tl_all.addToRow(tv_tabella);
                        tv_tabella = tl_all.createTv(p.getCognome(),MyTable.DIM_TESTO,Typeface.DEFAULT,MyTable.PADDING);
                        tl_all.addToRow(tv_tabella);
                        tl_all.addRow();
                    }//for(int n = 0; n < jsonArray.size(); n++){
                    //aggiungo la tabella al layout
                    ll_all_table.addView(tl_all);
                }//if(size > 0){
                else{
                    Toast.makeText(getActivity(),"Non ci sono persone registrate",Toast.LENGTH_LONG).show();
                }
            }//if(jsonArray != null){
            else {
                Log.d("AllPersonaFragment","json = null");
            }
        }//if(response.isSuccessful()){
        else {
            try {
                //Messaggio di errore dal server
                String msg = RetrofitService.messaggioErrore(response);
                Log.d("CreatePersonaFragment","Errore => "+msg);
                dialogError = new MyDialog(getActivity(),"Errore",msg,R.drawable.ic_error);
                dialogError.setOkDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void fallito(Throwable t) {
        pb_all.setVisibility(ProgressBar.GONE);
        Log.d("AllPersonaFragment","Errore => "+t.getMessage());
        dialogError = new MyDialog(getActivity(),"Errore",t.getMessage(),R.drawable.ic_error);
        dialogError.setOkDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //MainActivity main = (MainActivity)context;
        Log.d("AllPersonaFragment","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("AllPersonaFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("AllPersonaFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AllPersonaFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("AllPersonaFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("AllPersonaFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("AllPersonaFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AllPersonaFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("AllPersonaFragment","onDetach");
    }
}