package codes.neuralkatana.lexnotas.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import codes.neuralkatana.lexnotas.model.Nota;

public class NotaDAO {

    private final static ArrayList<Nota> notas = new ArrayList<>();

    public List<Nota> todos() {
        return (List<Nota>) notas.clone();
    }

    public void insere(Nota... notas) {
        NotaDAO.notas.addAll(Arrays.asList(notas));
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
    }

    public void remove(int posicao) {
        notas.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(notas, posicaoInicio, posicaoFim);
    }

    public boolean isEmpty(){
        if(todos().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void removeTodos() {
        notas.clear();
    }
}
