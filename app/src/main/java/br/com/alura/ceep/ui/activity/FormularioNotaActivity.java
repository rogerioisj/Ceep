package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity implements ConstantesActivity {

    public static final String APP_BAR_INSERE_NOTA = "Insere Nota";
    public static final String APP_BAR_EDITA_NOTA = "Edita Nota";
    private int posicaoRecebida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(APP_BAR_INSERE_NOTA);

        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA) && dadosRecebidos.hasExtra(CHAVE_POSICAO)){

            setTitle(APP_BAR_EDITA_NOTA);

            Nota notaRecebida = (Nota)dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, VALOR_POSICAO_INVALIDA);

            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());
            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (ehBotaoSalva(item)) {
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Nota criaNota() {
        TextView titulo = findViewById(R.id.formulario_nota_titulo);
        TextView descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean ehBotaoSalva(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_salva_botao;
    }
}
