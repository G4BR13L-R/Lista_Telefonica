package com.gabriel.listatelefonicatrabalho;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gabriel.listatelefonicatrabalho.bancoDeDados.DBHelper;
import com.gabriel.listatelefonicatrabalho.bancoDeDados.DBTelefone;
import com.gabriel.listatelefonicatrabalho.entidades.Telefone;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextNome;
    EditText editTextTelefone;
    EditText editTextDataNascimento;
    Button botaoSalvar;
    ListView listViewTelefones;
    List<Telefone> listaTelefonica;
    ArrayAdapter adapter;
    DBTelefone DBtelefone;
    Telefone telefone;
    Boolean validarEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(MainActivity.this);
        DBtelefone = new DBTelefone(db);

        editTextNome = findViewById(R.id.editTextNome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextDataNascimento = findViewById(R.id.editTextDataDeNascimento);
        botaoSalvar = findViewById(R.id.buttonSalvar);
        listViewTelefones = findViewById(R.id.listViewTelefones);

        listaTelefonica = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaTelefonica);

        listViewTelefones.setAdapter(adapter);
        DBtelefone.listar(listaTelefonica);

        validarEdicao = false;

        comandosComponentes();
    }

    @Override
    public void onBackPressed() {
        telefone = null;
        validarEdicao = false;
        editTextNome.setText("");
        editTextTelefone.setText("");
        editTextDataNascimento.setText("");
        Toast.makeText(MainActivity.this, "Cancelado com Sucesso!", Toast.LENGTH_LONG).show();
    }

    private void comandosComponentes() {
        listViewTelefones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Selecione uma Opção:")
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                validarEdicao = true;
                                telefone = new Telefone();
                                telefone.setId(listaTelefonica.get(i).getId());
                                editTextNome.setText(listaTelefonica.get(i).getNome());
                                editTextTelefone.setText(listaTelefonica.get(i).getTelefone());
                                editTextDataNascimento.setText(listaTelefonica.get(i).getDataNascimento());
                            }
                        })
                        .setNegativeButton("Remover", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Deseja realmente remover?")
                                        .setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int k) {
                                                DBtelefone.remover(listaTelefonica.get(i).getId());
                                                DBtelefone.listar(listaTelefonica);
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(MainActivity.this, "Removido com Sucesso!", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create().show();
                            }
                        })
                        .create().show();
                return (false);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextNome.getText().toString().isEmpty() || editTextTelefone.getText().toString().isEmpty()|| editTextDataNascimento.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Dados Inválidos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (validarEdicao == false) {
                        telefone = new Telefone();
                    }
                    telefone.setNome(editTextNome.getText().toString());
                    telefone.setTelefone(editTextTelefone.getText().toString());
                    telefone.setDataNascimento(editTextDataNascimento.getText().toString());
                    if (validarEdicao) {
                        DBtelefone.editar(telefone);
                        Toast.makeText(MainActivity.this, "Editado com Sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        DBtelefone.inserir(telefone);
                        Toast.makeText(MainActivity.this, "Salvo com Sucesso!", Toast.LENGTH_LONG).show();
                    }

                    DBtelefone.listar(listaTelefonica);
                    adapter.notifyDataSetChanged();
                    telefone = null;
                    validarEdicao = false;
                    editTextNome.setText("");
                    editTextTelefone.setText("");
                    editTextDataNascimento.setText("");
                }
            }
        });
    }
}