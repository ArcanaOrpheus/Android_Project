package com.example.ies.projecte;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends Activity {

    //Database Helper do not delete

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        //Creating tag

        Tag tag = new Tag("Producte");

        //Insert tag into db

        long tag_id = db.createTag(tag);

        //Creating Products

        Producte prod1 = new Producte(1,"Dani", 3);
        Producte prod2 = new Producte(2,"Ruben", 4);
        Producte prod3 = new Producte(3,"Sal", 1);
        Producte prod4 = new Producte(4,"Azucar", 2);

        //Inserting prods in db

        long prod1_id = db.createProd(prod1, new long[] {tag_id} );
        long prod2_id = db.createProd(prod2, new long[] {tag_id});
        long prod3_id = db.createProd(prod3, new long[] {tag_id} );
        long prod4_id = db.createProd(prod4, new long[] {tag_id});

        // Getting all tag names
        Log.d("Get Tags", "Getting All Tags");

        List<Tag> allTags = db.getAllTags();
        for (Tag tag2 : allTags) {
            Log.d("Tag Name", tag2.getTagName());
        }

        // Getting all Todos
        Log.d("Get Produtes", "Getting All Productes");

        List<Producte> allProds = db.getAllProductes();
        for (Producte prod : allProds) {
            Log.d("ToDo", prod.getNom());
        }

    }
}
