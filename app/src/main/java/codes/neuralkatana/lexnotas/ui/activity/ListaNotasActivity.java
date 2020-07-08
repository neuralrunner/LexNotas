package codes.neuralkatana.lexnotas.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import codes.neuralkatana.R;
import codes.neuralkatana.lexnotas.dao.NotaDAO;
import codes.neuralkatana.lexnotas.model.Nota;
import codes.neuralkatana.lexnotas.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {
    private NotaDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        criaRecyclerView();
    }

    private void criaRecyclerView() {
        RecyclerView listaDeNotas = findViewById(R.id.lista_notas_recyclerview);
        dao = criaNotasDeExemplo();
        List<Nota> todasNotas = dao.todos();
        //NOTE:LayoutManager está configurado no XML
        listaDeNotas.setAdapter(new ListaNotasAdapter(this,todasNotas));
    }

    private NotaDAO criaNotasDeExemplo() {
        NotaDAO notaDAO = new NotaDAO();
        notaDAO.insere(new Nota("Stoicism.1 - M.A.", "You have power over your mind - not outside events. Realize this, and you will find strength."));
        notaDAO.insere(new Nota("Stoicism.2 - M.A.", "Dwell on the beauty of life. Watch the stars, and see yourself running with them."));
        return notaDAO;
    }
}