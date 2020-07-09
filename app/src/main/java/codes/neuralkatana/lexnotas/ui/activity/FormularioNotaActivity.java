package codes.neuralkatana.lexnotas.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import codes.neuralkatana.R;
import codes.neuralkatana.lexnotas.dao.NotaDAO;
import codes.neuralkatana.lexnotas.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_formulario_nota_ic_salva){
            EditText titulo = findViewById(R.id.formulario_nota_titulo);
            EditText descricao = findViewById(R.id.formulario_nota_descricao);
            Nota nota = new Nota(titulo.getText().toString(), descricao.getText().toString());
            Intent resultadoInsercao = new Intent();
            resultadoInsercao.putExtra("nota",nota);
            setResult(2,resultadoInsercao);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}