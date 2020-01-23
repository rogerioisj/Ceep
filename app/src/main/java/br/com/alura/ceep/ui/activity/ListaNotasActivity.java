package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.activity.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.ConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ConstantesActivity.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.ConstantesActivity.CODIGO_REQUISICAO_EDITA_NOTA;
import static br.com.alura.ceep.ui.activity.ConstantesActivity.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.ConstantesActivity.VALOR_POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String APP_BAR_NOTAS = "Notas";
    public static final String APP_BAR_INSERE_NOTA = "Insere nota";
    public static final String APP_BAR_ALTERA_NOTA = "Altera Nota";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        //List<Nota> todasNotas = retornaTodasNotas();

        List<Nota> todasNotas = new NotaDAO().todos();

        setTitle(APP_BAR_NOTAS);

        configuraRecyclerView(todasNotas);
        configuraBotaoNovaNota();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        insereNota(requestCode, resultCode, data);

        editaNota(requestCode, resultCode, data);
    }

    private void editaNota(int requestCode, int resultCode, @Nullable Intent data) {
        Nota notaRecebida;
        int posicaoRecebida;
        if (validacaoNotaEdicao(requestCode, data)) {
            if (resultadoOK(resultCode)) {
                notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, VALOR_POSICAO_INVALIDA);
                if (posicaoRecebida > VALOR_POSICAO_INVALIDA) {
                    altera(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um erro ao tentar editar a nota", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private boolean validacaoNotaEdicao(int requestCode, @Nullable Intent data) {
        return validaCodigoEditaNota(requestCode)
                && validaSerializableInsereNota(data)
                && data.hasExtra(CHAVE_POSICAO);
    }

    private void altera(Nota notaRecebida, int posicaoRecebida) {
        new NotaDAO().altera(posicaoRecebida, notaRecebida);
        adapter.altera(posicaoRecebida, notaRecebida);
    }

    private boolean validaCodigoEditaNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_EDITA_NOTA;
    }

    private void insereNota(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehCodigoRequisicaoInsereNota(requestCode) && validaSerializableInsereNota(data)) {
            if (resultadoOK(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }
        }
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean resultadoOK(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean validaSerializableInsereNota(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
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
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
                abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
                startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_EDITA_NOTA);
            }
        });
    }

    private List<Nota> retornaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i < 10; i++) {
            dao.insere(new Nota("Titulo " + (i + 1), "Descrição " + (i + 1)));
        }
        return dao.todos();

    }
}
