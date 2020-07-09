package codes.neuralkatana.lexnotas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import codes.neuralkatana.R;
import codes.neuralkatana.lexnotas.dao.NotaDAO;
import codes.neuralkatana.lexnotas.model.Nota;
import codes.neuralkatana.lexnotas.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private List<Nota> todasNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        NotaDAO dao = criaNotasDeExemplo();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==2 && data.hasExtra("nota")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }
    }

    private void vaiParaFormularioNota() {
        Intent intent = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        startActivityForResult(intent,1);
    }

    private void criaRecyclerView(NotaDAO dao) {
        RecyclerView listaDeNotas = findViewById(R.id.lista_notas_recyclerview);
        //NOTE:LayoutManager está configurado no XML
        todasNotas = dao.todos();
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaDeNotas.setAdapter(adapter);
    }

    private NotaDAO criaNotasDeExemplo() {
        NotaDAO notaDAO = new NotaDAO();
        if(notaDAO.todos().isEmpty()) {
            notaDAO.insere(new Nota("Stoicism.1 - M.A.", "You have power over your mind - not outside events. Realize this, and you will find strength."));
            notaDAO.insere(new Nota("Stoicism.2 - M.A.", "Dwell on the beauty of life. Watch the stars, and see yourself running with them."));
        }
        return notaDAO;
    }

}