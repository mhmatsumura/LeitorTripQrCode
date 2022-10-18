package com.copel.mhmatsu.leitortripqrcode.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;


import com.copel.mhmatsu.leitortripqrcode.modelo.Tripsaver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alura on 12/08/15.
 */
public class TripsaverDAO extends SQLiteOpenHelper {

    public TripsaverDAO(Context context) {

        super(context, "leitortripqrcode", null, 2);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE Tripsaver (id INTEGER PRIMARY KEY, " +
                "tcmr TEXT , " +
                "codigoId TEXT);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Colaboradors ADD COLUMN caminhoFoto TEXT";
                db.execSQL(sql); // indo para versao 2
        }*/

    }


    public void insere(Tripsaver tripsaver) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoColaborador(tripsaver);

        db.insert("Tripsaver", null, dados);

    }





    private ContentValues pegaDadosDoColaborador(Tripsaver tripsaver) {

        ContentValues dados = new ContentValues();

        dados.put("tcmr", tripsaver.getTcmr());
        dados.put("id", tripsaver.getId());
        dados.put("codigoId", tripsaver.getCodigoId());


        return dados;
    }

    public List<Tripsaver> buscaColaboradores() {

        Log.d("log de depuração","buscando colaboradores");

        String sql = "SELECT * FROM Tripsaver;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Tripsaver> tripsavers = new ArrayList<Tripsaver>();

        while (c.moveToNext()) {
            Tripsaver tripsaver = new Tripsaver();

            tripsaver.setId(c.getLong(c.getColumnIndex("id")));
            tripsaver.setTcmr(c.getString(c.getColumnIndex("tcmr")));
            tripsaver.setCodigoId(c.getString(c.getColumnIndex("codigoId")));

            tripsavers.add(tripsaver);
        }
        c.close();

        return tripsavers;
    }

    public void deleta(Tripsaver tripsaver) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {tripsaver.getId().toString()};
        db.delete("Tripsaver", "id = ?", params);
    }

    public void altera(Tripsaver tripsaver) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoColaborador(tripsaver);

        String[] params = {tripsaver.getId().toString()};
        db.update("Tripsaver", dados, "id = ?", params);
    }
}
