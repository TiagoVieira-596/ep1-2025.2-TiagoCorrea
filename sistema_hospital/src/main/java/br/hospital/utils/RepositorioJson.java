package br.hospital.utils;
import br.hospital.menu.Tela;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class RepositorioJson<T> {

  private final String arquivo;
  private final Class<T[]> tipoArray;
  private final Gson gson;

  public RepositorioJson(Class<T[]> tipoArray, String arquivo) {
    this.arquivo = "data/" + arquivo;
    this.tipoArray = tipoArray;
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }

  public List<T> listar() {
    try {
        Path caminho = Path.of(arquivo);
        if (!Files.exists(caminho)) return new ArrayList<T>();

        String conteudo = Files.readString(caminho);
        T[] array = gson.fromJson(conteudo, tipoArray);
        return array != null ? new ArrayList<>(Arrays.asList(array)) : new ArrayList<>();
    } catch (IOException | JsonSyntaxException e) {
      Tela.exibirMensagem("Não foi possível ler do arquivo");
      return new ArrayList<>();
    }
  }
  public void salvarTodos(List<T> lista) {
    try {
      Path caminho = Path.of(arquivo);
      Files.createDirectories(caminho.getParent());

      String json = gson.toJson(lista);
      Files.writeString(caminho, json);
    } catch (IOException e) {
      Tela.exibirMensagem("Erro ao salvar arquivo: " + arquivo);
    }
  }

  public void adicionar(T objeto) {
    List<T> lista = listar();
    lista.add(objeto);
    salvarTodos(lista);
  }

  public boolean remover(T objeto) {
    List<T> lista = listar();
    boolean removido = lista.remove(objeto);
    salvarTodos(lista);
    return removido;
  }

  public T buscar(Predicate<T> filtro) {
    return listar().stream().filter(filtro).findFirst().orElse(null);
  }

  public List<T> filtrar(Predicate<T> filtro) {
    return listar().stream().filter(filtro).toList();
  }
}
