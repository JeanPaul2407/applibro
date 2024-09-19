package com.progmoviles.appusersqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class bdUsers extends SQLiteOpenHelper {
    //Definir la (s) tabla (s) de la base de datos
    String tblUser = "Create Table user(nombre text, codigo text, coste integer, role text)";
    String tblProduct = "Create Table product(ref text, name text, unitprice integer)";
    public bdUsers(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblUser);
        db.execSQL(tblProduct);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table user");
        db.execSQL(tblUser);
        db.execSQL("drop table product");
        db.execSQL(tblProduct);
    }
}