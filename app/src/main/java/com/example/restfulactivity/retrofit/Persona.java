package com.example.restfulactivity.retrofit;

import androidx.annotation.NonNull;

//In questa classe vengono memorizzate le informazioni di una singola Persona quando viene effettuata una richiesta GET con Retrofit
public class Persona implements Cloneable {

    private int id;
    private String nome;
    private String cognome;
    private byte età;
    private short altezza; //altezza in cm
    private String residenza;

    public Persona(int id, String nome, String cognome, byte età, short altezza, String residenza){
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.età = età;
        this.altezza = altezza;
        this.residenza = residenza;
    }

    @NonNull
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public int getId(){return this.id;}
    public String getNome(){return this.nome;}
    public String getCognome(){return this.cognome;}
    public byte getEtà(){return this.età;}
    public short getAltezza(){return this.altezza;}
    public String getResidenza(){return this.residenza;}

    public void setNome(String nome){this.nome = nome;}
    public void setCognome(String cognome){this.cognome = cognome;}
    public void setEtà(byte eta){this.età = età;}
    public void setAltezza(short altezza){this.altezza = altezza;}
    public void setResidenza(String residenza){this.residenza = residenza;}

}
