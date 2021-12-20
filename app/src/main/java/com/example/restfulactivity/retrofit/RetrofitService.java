package com.example.restfulactivity.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//Classe che interroga le API RESTful di Laravel
public class RetrofitService implements Callback {

    public static final String ipLocale = "http://192.168.0.23:8000"; //Indirizzo IP locale

    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder httpClient;
    private Retrofit retrofit;
    private PersonaInterface pInt;
    public  RispostaRetrofit delega = null;
    private String baseUrl; //url di base del server che utilizza i servizi REST
    private Call<JsonElement> callRisposta;

    public interface RispostaRetrofit{
        void risposta(Response response) throws CloneNotSupportedException, IOException;
        void fallito(Throwable t);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            Log.d("RetrofitService","onResponse");
            delega.risposta(response);
        } catch (CloneNotSupportedException | IOException e) {
            if(e instanceof CloneNotSupportedException){
                Log.d("RetrofitService","Impossibile clonare l'oggetto");
            }
            else if(e instanceof IOException){
                Log.d("RetrofitService","Errore I/O");
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        delega.fallito(t);
    }

    public interface PersonaInterface{
        @GET("/api/personas")
        Call<JsonElement> getPersonas();

        @GET("/api/personas/{id}")
        Call<JsonElement> getPersona(@Path("id") int personaId);

        @FormUrlEncoded
        @POST("/api/personas")
        Call<JsonElement> createPersona(@Field("nome") String n,@Field("cognome") String cn,@Field("eta") String e,@Field("altezza") String a,@Field("residenza") String res);

        @FormUrlEncoded
        @PUT("/api/personas/{id}")
        Call<JsonElement> setPersona(@Path("id") int personaId,@Field("nome") String n,@Field("cognome") String cn,@Field("eta") String e,@Field("altezza") String a,@Field("residenza") String res);

        @DELETE("/api/personas/{id}")
        Call<JsonElement> deletePersona(@Path("id") int personaId);
    }



    public RetrofitService(String baseUrl){
        this.baseUrl = baseUrl;
        httpClient = new OkHttpClient.Builder();
        retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = retrofitBuilder.client(httpClient.build()).build();
        pInt = retrofit.create(PersonaInterface.class);
    }

    public void getPersonas(){
        this.callRisposta = pInt.getPersonas();
        this.callRisposta.enqueue(this);

    }//public void getPersonas(){

    public void getPersona(int id){
        this.callRisposta = pInt.getPersona(id);
        this.callRisposta.enqueue(this);
    }//public void getPersona(int id){

    public void createPersona(String nome, String cognome, String età, String altezza, String residenza){
        this.callRisposta = pInt.createPersona(nome,cognome,età,altezza,residenza);
        callRisposta.enqueue(this);
    }//public void createPersona(String n, String cn, byte e, short a, String res){

    public void editPersona(int id,String nome, String cognome, String età, String altezza, String residenza){
        this.callRisposta = pInt.setPersona(id, nome, cognome, età, altezza, residenza);
        this.callRisposta.enqueue(this);
    }//public void setPersona(int id){

    public void deletePersona(int id){
        this.callRisposta = pInt.deletePersona(id);
        this.callRisposta.enqueue(this);
    }//public void deletePersona(int id){

    //restituisce il messaggio di errore di una richiesta HTTP con Retrofit
    public static String messaggioErrore(Response response) throws IOException {
        String msg = "";
        String msgJson = ""; //messaggio di errore come stringa JSON
        InputStream is = response.errorBody().byteStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String riga = "";
        StringBuilder error = new StringBuilder();
        while((riga = br.readLine()) != null){
            error.append(riga);
        }
        br.close();
        isr.close();
        is.close();
        msgJson = error.toString();
        try{
            JsonObject jo = new JsonParser().parse(msgJson).getAsJsonObject();
            msg = jo.get("msg").getAsString();
        }catch(JsonSyntaxException jse){
            msg = "Errore sconosciuto";
        }
        return msg;
    }//public static String messaggioErrore(Response response) throws IOException {


}
