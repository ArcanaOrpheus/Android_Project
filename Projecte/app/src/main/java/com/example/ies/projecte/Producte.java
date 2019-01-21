package com.example.ies.projecte;

public class Producte {
    int id;
    String nom;
    int stock;


    //Constructores

    public Producte(){}

    public Producte(String nom, int stock){
        this.nom = nom;
        this.stock=stock;
    }

    public Producte(int id, String nom, int stock){
        this.id=id;
        this.nom =nom;
        this.stock=stock;
    }

    //Setters and getters

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return this.id;
    }

    public void setNom(String nom){
        this.nom=nom;
    }

    public String getNom(){
        return this.nom;
    }
    public void setStock(int stock){
        this.stock=stock;
    }

    public int getStock(){
        return this.stock;
    }
}
