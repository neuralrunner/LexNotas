package codes.neuralkatana.lexnotas.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        mostraListaDeNotas();

    }

    private void mostraListaDeNotas() {
        RecyclerView listaDeNotas = findViewById(R.id.lista_notas_recyclerview);
        criaEPopulaDAO();
        List<Nota> todasNotas = dao.todos();
        listaDeNotas.setAdapter(new ListaNotasAdapter(this,todasNotas));
        //LayoutManager criado via xml, criação via Java abaixo:
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //listaDeNotas.setLayoutManager(layoutManager);
    }

    private void criaEPopulaDAO() {
        dao = new NotaDAO();
        for(int i = 1; i<=10000; i++){
            dao.insere(new Nota("Stoicism."+i+" - M.A.", "You have power over your mind - not outside events. Realize this, and you will find strength."));
        }

        //dao.insere(new Nota("Stoicism.2 - M.A.", "Dwell on the beauty of life. Watch the stars, and see yourself running with them."));
    }
}