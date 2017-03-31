package com.scl.neutrino.simplecontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Nutrino on 3/31/2017.
 */

public class Database extends SQLiteOpenHelper{

    public static int DATABASE_VERSION       = 1;
    private static final String DATABASE_NAME = "contact_db";

    private static final String NAME          = "person_name";
    private static final String CONTACT       = "phone_no";

    private static final String CONTACT_TABLE = "contact";

    private static final String CREATE_CT = "CREATE TABLE "+CONTACT_TABLE+" ( "+NAME+" TEXT , "+CONTACT+" TEXT , PRIMARY KEY ( "
            +CONTACT+"));";
    private static final String UPGRADE_CT = "DROP TABLE IF EXISTS "+CONTACT_TABLE;

    private SQLiteDatabase helper;

    public Database(Context context,  int version) {
        super(context, DATABASE_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UPGRADE_CT);
        onCreate(db);
    }

    public void insertContact(ArrayList<Person> persons){

        ContentValues values = new ContentValues();
        helper = getWritableDatabase();

        for(Person p : persons){

            values.put(NAME , p.getPersonName());
            values.put(CONTACT , p.getContact());

            try{
                helper.insert(CONTACT_TABLE , null , values);
            }catch(SQLiteConstraintException ex){
                Log.d("Exception : " , ex.toString());
            }
        }

        helper.close();
    }

    public ArrayList<Person> getAllContact(){

        String sql = "SELECT * FROM "+CONTACT_TABLE;
        helper = getReadableDatabase();
        Cursor cursor = helper.rawQuery(sql , null);

        cursor.moveToFirst();

        ArrayList<Person> list = new ArrayList<>();
        Person prsn;

        while (!cursor.isAfterLast()){

            prsn = new Person(cursor.getString(cursor.getColumnIndex(NAME)) , cursor.getString(cursor.getColumnIndex(CONTACT)));
            list.add(prsn);
            cursor.moveToNext();
        }

        cursor.close();
        helper.close();

        return list;
    }
}
