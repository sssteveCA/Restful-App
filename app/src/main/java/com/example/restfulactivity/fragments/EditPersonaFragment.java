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
import java.io.StringReader;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPersonaFragment extends Fragment implements View.OnClickListener, RetrofitService.RispostaRetrofit {

    private EditText et_edit_id, et_edit_nome, et_edit_cognome;
    private EditText et_edit_eta, et_edit_altezza, et_edit_residenza;
    private Button bt_edit;
    private MyDialog dialogSuccess, dialogError;
    private ProgressBar pb_edit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditPersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPersonaFragment newInstance(String param1, String param2) {
        EditPersonaFragment fragment = new EditPersonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("EditPersonaFragment","onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("EditPersonaFragment","onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_persona, container, false);
        et_edit_id = v.findViewById(R.id.et_edit_id);
        et_edit_nome = v.findViewById(R.id.et_edit_nome);
        et_edit_cognome = v.findViewById(R.id.et_edit_cognome);
        et_edit_eta = v.findViewById(R.id.et_edit_eta);
        et_edit_altezza = v.findViewById(R.id.et_edit_altezza);
        et_edit_residenza = v.findViewById(R.id.et_edit_residenza);
        bt_edit = v.findViewById(R.id.bt_edit);
        pb_edit = v.findViewById(R.id.pb_edit);
        bt_edit.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener
        Log.d("EditPersonaFragment","onCLick");
        switch(view.getId()){
            case R.id.bt_edit:
                if(Utils.connesso(getActivity())){
                    String editId = et_edit_id.getText().toString();
                    if(Utils.numero(editId)){
                        pb_edit.setVisibility(ProgressBar.VISIBLE);
                        int id = Integer.parseInt(editId);
                        String nome = et_edit_nome.getText().toString();
                        String cognome = et_edit_cognome.getText().toString();
                        String eta = et_edit_eta.getText().toString();
                        String altezza = et_edit_altezza.getText().toString();
                        String residenza = et_edit_residenza.getText().toString();
                        RetrofitService retrofitService = new RetrofitService(RetrofitService.ipLocale);
                        retrofitService.delega = this;
                        retrofitService.editPersona(id,nome,cognome,eta,altezza,residenza);
                    }//if(Utils.numero(editId)){
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
    public void risposta(Response response) throws CloneNotSupportedException, IOException {
        //RetrofitService.RispostaRetrofit
        pb_edit.setVisibility(ProgressBar.GONE);
        if(response.isSuccessful()) {
            JsonElement risp = (JsonElement) response.body();
            JsonObject json = risp.getAsJsonObject();
            if(json != null){
                String msg = json.get("msg").getAsString();
                Log.d("EditPersonaFragment", "Risposta => " + msg);
                et_edit_id.setText("");
                et_edit_nome.setText("");
                et_edit_cognome.setText("");
                et_edit_eta.setText("");
                et_edit_altezza.setText("");
                et_edit_residenza.setText("");
                dialogSuccess = new MyDialog(getActivity(),"Operazione eseguita",msg,R.drawable.ic_done);
                dialogSuccess.setOkDialog();
            }//if(json != null){
            else{
                Log.d("EditPersonaFragment","json = null");
            }
        }//if(response.isSuccessful()){
        else {
            //Messaggio di errore dal server
            String msg = RetrofitService.messaggioErrore(response);
            Log.d("EditPersonaFragment", "Errore => " + msg);
            dialogError = new MyDialog(getActivity(),"Errore",msg,R.drawable.ic_error);
            dialogError.setOkDialog();
        }
    }//public void risposta(Response response) throws CloneNotSupportedException, IOException {

    @Override
    public void fallito(Throwable t) {
        pb_edit.setVisibility(ProgressBar.GONE);
        Log.d("EditPersonaFragment","Errore => "+t.getMessage());
        dialogError = new MyDialog(getActivity(),"Errore",t.getMessage(),R.drawable.ic_error);
        dialogError.setOkDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //MainActivity main = (MainActivity)context;
        Log.d("EditPersonaFragment","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("EditPersonaFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EditPersonaFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("EditPersonaFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("EditPersonaFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("EditPersonaFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("EditPersonaFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("EditPersonaFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("EditPersonaFragment","onDetach");
    }
}