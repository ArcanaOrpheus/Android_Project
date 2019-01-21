package com.example.ies.projecte;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_PRODUCT = "Productes";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_PROD_TAG = "prod_tags";

    // Common column names
    private static final String KEY_ID = "id";

    // NOTES Table - column nmaes
    private static final String KEY_PROD = "name";
    private static final String KEY_STOCK = "stock";

    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    private static final String KEY_PROD_ID = "prod_id";
    private static final String KEY_TAG_ID = "tag_id";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_PROD = "CREATE TABLE "
            + TABLE_PRODUCT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROD
            + " TEXT," + KEY_STOCK + " INTEGER"  + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_PROD_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PROD_ID + " INTEGER," + KEY_TAG_ID + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PROD);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROD_TAG);

        // create new tables
        onCreate(db);
    }

    // ------------------------ Product table methods ----------------//

    /**
     * Creating a prod
     */
    public long createProd(Producte prod, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, prod.getId());
        values.put(KEY_PROD, prod.getNom());
        values.put(KEY_STOCK, prod.getStock());

        // insert row
        long todo_id = db.insert(TABLE_PRODUCT, null, values);

        // insert tag_ids
        for (long tag_id : tag_ids) {
            createProdTag(todo_id, tag_id);
        }

        return todo_id;
    }

    /**
     * get single todo
     */
    public Producte getProducte(long prod_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "
                + KEY_ID + " = " + prod_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Producte p = new Producte();
        p.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        p.setNom((c.getString(c.getColumnIndex(KEY_PROD))));
        p.setStock(c.getInt(c.getColumnIndex(KEY_STOCK)));

        return p;
    }

    /**
     * getting all todos
     * */
    public List<Producte> getAllProductes() {
        List<Producte> prods = new ArrayList<Producte>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Producte p = new Producte();
                p.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                p.setNom((c.getString(c.getColumnIndex(KEY_PROD))));
                p.setStock(c.getInt(c.getColumnIndex(KEY_STOCK)));

                // adding to todo list
                prods.add(p);
            } while (c.moveToNext());
        }

        return prods;
    }

    /**
     * getting all todos under single tag
     * */
    public List<Producte> getAllToDosByTag(String tag_name) {
        List<Producte> prods = new ArrayList<Producte>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " td, "
                + TABLE_TAG + " tg, " + TABLE_PROD_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_PROD_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Producte p = new Producte();
                p.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                p.setNom((c.getString(c.getColumnIndex(KEY_PROD))));
                p.setStock(c.getInt(c.getColumnIndex(KEY_STOCK)));

                // adding to prods list
                prods.add(p);
            } while (c.moveToNext());
        }

        return prods;
    }

    /**
     * getting products count
     */
    public int getProdsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a product
     */
    public int updateProduct(Producte prod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROD, prod.getNom());
        values.put(KEY_STOCK, prod.getStock());

        // updating row
        return db.update(TABLE_PRODUCT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(prod.getId()) });
    }

    /**
     * Deleting a prod
     */
    public void deleteProd(long prod_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, KEY_ID + " = ?",
                new String[] { String.valueOf(prod_id) });
    }

    // ------------------------ "tags" table methods ----------------//

    /**
     * Creating tag
     */
    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // insert row
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id;
    }

    /**
     * getting all tags
     * */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));

                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    /**
     * Updating a tag
     */
    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // updating row
        return db.update(TABLE_TAG, values, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }

    /**
     * Deleting a tag
     */
    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting tag
        // check if todos under this tag should also be deleted
        if (should_delete_all_tag_todos) {
            // get all todos under this tag
            List<Producte> allTagToDos = getAllToDosByTag(tag.getTagName());

            // delete all products
            for (Producte prod : allTagToDos) {
                // delete product
                deleteProd(prod.getId());
            }
        }

        // now delete the tag
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }

    // ------------------------ "todo_tags" table methods ----------------//

    /**
     * Creating todo_tag
     */
    public long createProdTag(long todo_id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROD_ID, todo_id);
        values.put(KEY_TAG_ID, tag_id);

        long id = db.insert(TABLE_PROD_TAG, null, values);

        return id;
    }

    /**
     * Updating a prod tag
     */
    public int updateProdTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);

        // updating row
        return db.update(TABLE_PRODUCT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     * Deleting a prod tag
     */
    public void deleteProdTag(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
