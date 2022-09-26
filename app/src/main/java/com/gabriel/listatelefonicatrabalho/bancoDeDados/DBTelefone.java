package com.gabriel.listatelefonicatrabalho.bancoDeDados;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DBTelefone {
    private DBHelper db;
    private SQLiteDatabase conexao;

    public DBTelefone(DBHelper db) {
        this.db = db;
    }

    public void inserir(Telefone telefone) {
        conexao = db.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", telefone.getNome());
        valores.put("dataNascimento", telefone.getDataNascimento());
        valores.put("telefone", telefone.getTelefone());

        conexao.insertOrThrow("listaTelefonica", null, valores);

        conexao.close();
    }

    public void editar(Telefone telefone) {
        conexao = db.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nome", telefone.getNome());
        valores.put("dataNascimento", telefone.getDataNascimento());
        valores.put("telefone", telefone.getTelefone());

        conexao.update("listaTelefonica", valores, "id=?", new String[]{telefone.getId().toString()});

        conexao.close();
    }

    public void remover(Integer id) {
        conexao = db.getWritableDatabase();

        conexao.delete("listaTelefonica", "id=?", new String[]{id + ""});

        conexao.close();
    }

    public void listar(List listaTelefonica) {
        listaTelefonica.clear();
        conexao = db.getReadableDatabase();

        String colunas[] = {"id", "nome", "dataNascimento", "telefone"};
        Cursor query = conexao.query("listaTelefonica", colunas, null, null, null, null, "nome");

        while (query.moveToNext()) {
            Telefone telefone = new Telefone();

            telefone.setId(Integer.parseInt(query.getString(0)));
            telefone.setNome(query.getString(1));
            telefone.setDataNascimento(query.getString(2));
            telefone.setTelefone(query.getString(3));

            listaTelefonica.add(telefone);
        }

        conexao.close();
    }
}
