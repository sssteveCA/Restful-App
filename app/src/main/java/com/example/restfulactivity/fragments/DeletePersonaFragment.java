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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeletePersonaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeletePersonaFragment extends Fragment implements View.OnClickListener, RetrofitService.RispostaRetrofit {

    private EditText et_delete;
    private Button bt_delete;
    private MyDialog dialogSuccess,dialogError;
    private ProgressBar pb_delete;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeletePersonaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeletePersonaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeletePersonaFragment newInstance(String param1, String param2) {
        DeletePersonaFragment fragment = new DeletePersonaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DeletePersonaFragment","onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DeletePersonaFragment","onCreateView");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_delete_persona, container, false);
        et_delete = v.findViewById(R.id.et_delete);
        bt_delete = v.findViewById(R.id.bt_delete);
        pb_delete = v.findViewById(R.id.pb_delete);
        bt_delete.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener
        Log.d("DeletePersonaFragment","onClick");
        switch(view.getId()){
            case R.id.bt_delete:
                    if(Utils.connesso(getActivity())){
                        String deleteId = et_delete.getText().toString();
                        if(Utils.numero(deleteId)){
                            pb_delete.setVisibility(ProgressBar.VISIBLE);
                            RetrofitService retrofitService = new RetrofitService(RetrofitService.ipLocale);
                            retrofitService.delega = this;
                            retrofitService.deletePersona(Integer.parseInt(deleteId));
                        }//if(Utils.numero(deleteId)){
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
        pb_delete.setVisibility(ProgressBar.GONE);
        if (response.isSuccessful()) {
            JsonElement risp = (JsonElement) response.body();
            JsonObject json = risp.getAsJsonObject();
            if(json != null) {
                String msg = json.get("msg").getAsString();
                Log.d("DeletePersonaFragment", "Risposta => " + msg);
                dialogSuccess = new MyDialog(getActivity(),"Operazione eseguita",msg,R.drawable.ic_done);
                dialogSuccess.setOkDialog();
                et_delete.setText("");
            }//if(json != null) {
            else{
                Log.d("DeletePersonaFragment","json = null");
            }
        }//if (response.isSuccessful()) {
        else {
            //Messaggio di errore dal server
            String msg = RetrofitService.messaggioErrore(response);
            Log.d("DeletePersonaFragment", "Errore => " + msg);
            dialogError = new MyDialog(getActivity(),"Errore",msg,R.drawable.ic_error);
            dialogError.setOkDialog();
        }
    }//public void risposta(Response response) throws CloneNotSupportedException, IOException {

    @Override
    public void fallito(Throwable t) {
        pb_delete.setVisibility(ProgressBar.GONE);
        Log.d("DeletePersonaFragment","Errore => "+t.getMessage());
        dialogError = new MyDialog(getActivity(),"Errore",t.getMessage(),R.drawable.ic_error);
        dialogError.setOkDialog();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("DeletePersonaFragment","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("DeletePersonaFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DeletePersonaFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DeletePersonaFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DeletePersonaFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DeletePersonaFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("DeletePersonaFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DeletePersonaFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("DeletePersonaFragment","onDetach");
    }
}