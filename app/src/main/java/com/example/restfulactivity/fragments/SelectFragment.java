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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.restfulactivity.MainActivity;
import com.example.restfulactivity.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //passa la posizione dell'elemento Spinner selezionato
    public interface passaPosizione{
        void cambia(int pos);
    }

    public passaPosizione passa;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private TextView tv_select;
    private Spinner sp_select;

    public SelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFragment newInstance(String param1, String param2) {
        SelectFragment fragment = new SelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SelectFragment","onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SelectFragment","onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select, container, false);
        //tv_select = v.findViewById(R.id.tv_select);
        //Creazione dello Spinner con le options dichiarate in string.xml
        sp_select = v.findViewById(R.id.sp_select);
        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(getActivity(),R.array.sp_azioni,android.R.layout.simple_spinner_item);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_select.setAdapter(sp_adapter);
        sp_select.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity main = (MainActivity)context;
        passa = main;
        Log.d("SelectFragment","onAttach");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //AdapterView.OnItemSelectedListener
        Log.d("SelectFragment","onItemSelected");
        Log.d("onItemSelected","i = "+i+" l "+l);
        //passa la posizione alla funzione astratta "cambia"
        passa.cambia(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //AdapterView.OnItemSelectedListener
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("SelectFragment","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SelectFragment","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SelectFragment","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("SelectFragment","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("SelectFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("SelectFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SelectFragment","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("SelectFragment","onDetach");
    }
}