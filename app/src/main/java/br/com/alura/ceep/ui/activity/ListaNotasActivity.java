package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.recyclerview.adapter.ListaNotasAdapter;

import static br.com.alura.ceep.ui.activity.ConstantesActivity.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.ConstantesActivity.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = retornaTodasNotas();


        configuraRecyclerView(todasNotas);
        configuraBotaoNovaNota();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        verificaNotaDeFormulario(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void verificaNotaDeFormulario(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehCodigoRequisicaoInsereNota(requestCode) && ehCodigoResultadoInsereNota(resultCode) && validaSerializableInsereNota(data)) {
            Nota nota = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(nota);
            adapter.adiciona(nota);
        }
    }

    private boolean validaSerializableInsereNota(@Nullable Intent data) {
        return data.hasExtra("nota");
    }

    private boolean ehCodigoResultadoInsereNota(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraBotaoNovaNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);

        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioActivity();
            }
        });
    }

    private void vaiParaFormularioActivity() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }

    private List<Nota> retornaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();

    }
}
