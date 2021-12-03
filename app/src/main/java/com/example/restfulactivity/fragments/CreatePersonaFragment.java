package com.example.restfulactivity.fragments;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.restfulactivity.MainActivity;
import com.example.restfulactivity.R;
import com.example.restfulactivity.dialog.MyDialog;
import com.example.restfulactivity.retrofit.RetrofitService;
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
 * Use the {@link CreatePersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePersonaFragment extends Fragment implements View.OnClickListener, RetrofitService.RispostaRetrofit {

    private EditText et_create_nome, et_create_cognome;
    private EditText et_create_eta, et_create_altezza, et_create_residenza;
    private Button bt_create;
    private MyDialog dialogSuccess,dialogError;
    private ProgressBar pb_create;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePersonaFragment newInstance(String param1, String param2) {
        CreatePersonaFragment fragment = new CreatePersonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("CreatePersonaFragment","onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CreatePersonaFragment","onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_persona, container, false);
        et_create_nome = v.findViewById(R.id.et_create_nome);
        et_create_cognome = v.findViewById(R.id.et_create_cognome);
        et_create_eta = v.findViewById(R.id.et_create_eta);
        et_create_altezza = v.findViewById(R.id.et_create_altezza);
        et_create_residenza = v.findViewById(R.id.et_create_residenza);
        bt_create = v.findViewById(R.id.bt_create);
        pb_create = v.findViewById(R.id.pb_create);
        bt_create.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener
        Log.d("CreatePersonaFragment","onClick");
        switch(view.getId()){
            case R.id.bt_create:
                if(Utils.connesso(getActivity())) {
                    pb_create.setVisibility(ProgressBar.VISIBLE);
                    String nome = et_create_nome.getText().toString();
                    String cognome = et_create_cognome.getText().toString();
                    String eta = et_create_eta.getText().toString();
                    String altezza = et_create_altezza.getText().toString();
                    String residenza = et_create_residenza.getText().toString();
                    RetrofitService retrofitService = new RetrofitService(RetrofitService.ipLocale);
                    retrofitService.delega = this;
                    retrofitService.createPersona(nome,cognome,eta,altezza,residenza);
                }//if(Utils.connesso(getActivity()))
                else{
                    Toast.makeText(getActivity(),"Collegati ad internet per continuare",Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void risposta(Response response) throws CloneNotSupportedException, IOException {
        //RetrofitService.RispostaRetrofit
        pb_create.setVisibility(ProgressBar.GONE);
        if (response.isSuccessful()) {
            JsonElement risp = (JsonElement) response.body();
            JsonObject json = risp != null ? risp.getAsJsonObject() : null;
            if(json != null) {
                String msg = json.get("msg").getAsString();
                Log.d("CreatePersonaFragment", "Risposta => " + msg);
                et_create_nome.setText("");
                et_create_cognome.setText("");
                et_create_eta.setText("");
                et_create_altezza.setText("");
                et_create_residenza.setText("");
                dialogSuccess = new MyDialog(getActivity(),"Operazione eseguita",msg,R.drawable.ic_done);
                dialogSuccess.setOkDialog();
            }//if(json != null){
            else {
                Log.d("CreatePersonaFragment", "json = null");
            }
        }//if (response.isSuccessful()) {
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
    }//public void risposta(Response response) throws CloneNotSupportedException, IOException {

    @Override
    public void fallito(Throwable t) {
        pb_create.setVisibility(ProgressBar.GONE);
        Log.d("CreatePersonaFragment","Errore => "+t.getMessage());
        dialogError = new MyDialog(getActivity(),"Errore",t.getMessage(),R.drawable.ic_error);
        dialogError.setOkDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //MainActivity main = (MainActivity)context;
        Log.d("CreatePersonaFragment","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("CreatePersonaFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CreatePersonaFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CreatePersonaFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CreatePersonaFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CreatePersonaFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("CreatePersonaFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CreatePersonaFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("CreatePersonaFragment","onDetach");
    }
}