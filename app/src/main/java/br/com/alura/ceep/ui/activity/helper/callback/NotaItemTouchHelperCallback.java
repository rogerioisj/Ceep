package br.com.alura.ceep.ui.activity.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int movimentoDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int movimentoArraste = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        return makeMovementFlags(movimentoArraste,movimentoDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        new NotaDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        new NotaDAO().remove(viewHolder.getAdapterPosition());
        adapter.remove(viewHolder.getAdapterPosition());

    }
}
