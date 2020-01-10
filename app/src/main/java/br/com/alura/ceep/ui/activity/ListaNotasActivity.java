package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        NotaDAO dao = new NotaDAO();
        List<Nota> todasNotas = dao.todos();


        configuraBotaoNovaNota();
        configuraAdapter(todasNotas);
        //configuraLinearLayout(listaNotas);

    }

    @Override
    protected void onResume() {
        NotaDAO dao = new NotaDAO();
        dao.todos();
        List<Nota> todos = dao.todos();
        configuraAdapter(todos);
        super.onResume();
    }

    private void configuraBotaoNovaNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);

        botaoInsereNota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
                startActivity(iniciaFormularioNota);
            }
        });
    }

    private void configuraAdapter(List<Nota> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        listaNotas.setAdapter(new ListaNotasAdapter(this, notas));
    }

}
