package codes.neuralkatana.lexnotas.ui.activity;

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

import codes.neuralkatana.R;
import codes.neuralkatana.lexnotas.dao.NotaDAO;
import codes.neuralkatana.lexnotas.model.Nota;
import codes.neuralkatana.lexnotas.ui.recyclerview.adapter.ListaNotasAdapter;
import codes.neuralkatana.lexnotas.ui.recyclerview.adapter.listener.OnItemClickListener;
import codes.neuralkatana.lexnotas.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "NOTAS";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITULO_APPBAR);
        setContentView(R.layout.activity_lista_notas);
        NotaDAO dao = new NotaDAO();
        criaRecyclerView(dao);

        mostraInsereNota();
    }

    private void mostraInsereNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaInsere();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isResultadoInsereNota(requestCode, data)) {
            inserirNota(resultCode, data);
        }

        if (isResultadoAlteraNota(requestCode, data)) {
            alterarNota(resultCode, data);
        }
    }

    private void inserirNota(int resultCode, @Nullable Intent data) {
        if(isCodigoResultadoOK(resultCode)){
            Nota notaRecebida = adicionaNota(data);
            adapter.adiciona(notaRecebida);
        }
    }

    private void alterarNota(int resultCode, @Nullable Intent data) {
        if(isCodigoResultadoOK(resultCode)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            if (isPosicaoValida(posicaoRecebida)) {
                new NotaDAO().altera(posicaoRecebida, notaRecebida);
                adapter.altera(posicaoRecebida, notaRecebida);
            } else {
                Toast.makeText(this,
                        "Ocorreu um problema na alteração da Nota.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Nota adicionaNota(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        return notaRecebida;
    }

    private void criaRecyclerView(NotaDAO dao) {
        RecyclerView listaDeNotas = findViewById(R.id.lista_notas_recyclerview);
        //NOTE:LayoutManager está configurado no XML
        /*if (dao.isEmpty()) {
            dao = mockDAO(dao);
        }*/
        List<Nota> todasNotas = dao.todos();
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaDeNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaAltera(nota, posicao);
            }
        });
        configuraItemTouchHelper(listaDeNotas);

    }

    private void configuraItemTouchHelper(RecyclerView listaDeNotas) {
        //ItemTouchHelper e NotaItemTouchHelperCallback, responsáveis pela animação do RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaDeNotas);
    }

    private void vaiParaFormularioNotaInsere() {
        Intent intent = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void vaiParaFormularioNotaAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    private NotaDAO mockDAO(NotaDAO dao) {
        for (int i = 0; i < 10; i++) {
            dao.insere(new Nota("Stoicism " + (i + 1), "“Everything we hear is an opinion, not a fact." +
                    " Everything we see is a perspective, not the truth.”"));
        }
        return dao;
    }

    private boolean isPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean isResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicaoAlteraNota(requestCode)
                && hasNotaDataExtra(data)
                && hasChavePosicao(data);
    }

    private boolean hasChavePosicao(@Nullable Intent data) {
        return data.hasExtra(CHAVE_POSICAO);
    }

    private boolean isCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean isResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return isCodigoDeRequisicaoInsereNota(requestCode) && hasNotaDataExtra(data);
    }

    private boolean hasNotaDataExtra(Intent data) {
        return data!= null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodigoResultadoOK(int resultCode) {
        return resultCode == RESULT_OK;
    }

    private boolean isCodigoDeRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }


}