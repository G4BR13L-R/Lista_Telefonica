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
    EditText campoNome;
    EditText campoDataNascimento;
    EditText campoTelefone;

    Button botaoSalvar;

    ListView listagemDados;
    List<Telefone> listaTelefonica;
    ArrayAdapter adapter;

    DBTelefone DBtelefone;
    Telefone telefone;

    Boolean editarTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(MainActivity.this);
        DBtelefone = new DBTelefone(db);

        campoNome = findViewById(R.id.campoNome);
        campoDataNascimento = findViewById(R.id.campoDataNascimento);
        campoTelefone = findViewById(R.id.campoTelefone);
        botaoSalvar = findViewById(R.id.botaoSalvar);
        listagemDados = findViewById(R.id.listagemDados);

        listaTelefonica = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaTelefonica);

        listagemDados.setAdapter(adapter);
        DBTelefone.listar(listaTelefonica);

        editarTelefone = false;
        campoNome.requestFocus();

        acaoComponentes();
    }

    @Override
    public void onBackPressed() {
        telefone = null;
        editarTelefone = false;
        campoNome.setText("");
        campoNome.requestFocus();
        campoDataNascimento.setText("");
        campoTelefone.setText("");

        Toast.makeText(MainActivity.this, "Cancelado com Sucesso!", Toast.LENGTH_LONG).show();
    }

    private void acaoComponentes() {
        listagemDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Selecione uma Opção:")
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                editarTelefone = true;

                                telefone = new Telefone();
                                telefone.setId(listaTelefonica.get(i).getId());

                                campoNome.setText(listaTelefonica.get(i).getNome());
                                campoDataNascimento.setText(listaTelefonica.get(i).getDataNascimento());
                                campoTelefone.setText(listaTelefonica.get(i).getTelefone());
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
                if (campoNome.getText().toString().isEmpty() || campoDataNascimento.getText().toString().isEmpty() || campoTelefone.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Dados Inválidos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (editarTelefone == false) {
                        telefone = new Telefone();
                    }

                    telefone.setNome(campoNome.getText().toString());
                    telefone.setDataNascimento(campoDataNascimento.getText().toString());
                    telefone.setTelefone(campoTelefone.getText().toString());

                    if (editarTelefone) {
                        DBtelefone.editar(telefone);

                        Toast.makeText(MainActivity.this, "Editado com Sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        DBtelefone.inserir(telefone);

                        Toast.makeText(MainActivity.this, "Salvo com Sucesso!", Toast.LENGTH_LONG).show();
                    }

                    DBtelefone.listar(listaTelefonica);
                    adapter.notifyDataSetChanged();

                    telefone = null;
                    editarTelefone = false;
                    campoNome.setText("");
                    campoNome.requestFocus();
                    campoDataNascimento.setText("");
                    campoTelefone.setText("");
                }
            }
        });
    }
}