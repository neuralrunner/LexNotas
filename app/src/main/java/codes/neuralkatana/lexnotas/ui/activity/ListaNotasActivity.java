package codes.neuralkatana.lexnotas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import codes.neuralkatana.R;
import codes.neuralkatana.lexnotas.dao.NotaDAO;
import codes.neuralkatana.lexnotas.model.Nota;
import codes.neuralkatana.lexnotas.ui.recyclerview.adapter.ListaNotasAdapter;

import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static codes.neuralkatana.lexnotas.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {


    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                vaiParaFormularioNota();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isResultadoComNota(requestCode, resultCode, data)) {
            Nota notaRecebida = adicionaNota(data);
            adapter.adiciona(notaRecebida);
        }
    }

    private Nota adicionaNota(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        return notaRecebida;
    }

    private boolean isResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodigoDeRequisicaoInsereNota(requestCode) && isCodigoResultadoNotaCriada(resultCode) && hasNotaNoDataExtra(data);
    }

    private boolean hasNotaNoDataExtra(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean isCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean isCodigoDeRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void vaiParaFormularioNota() {
        Intent intent = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void criaRecyclerView(NotaDAO dao) {
        RecyclerView listaDeNotas = findViewById(R.id.lista_notas_recyclerview);
        //NOTE:LayoutManager está configurado no XML
        List<Nota> todasNotas = dao.todos();
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaDeNotas.setAdapter(adapter);
    }

}