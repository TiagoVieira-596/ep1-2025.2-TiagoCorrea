package br.hospital.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import br.hospital.menu.Tela;

public class Serializador {
  public static <T> void serializar(T objeto, String arquivo, Class<T[]> tipoArray) throws IOException{
    Gson gson = new Gson();
    Path caminho = Path.of("data/", arquivo);

    List<T> listaDeDados = new ArrayList<>();

    try {
      if (Files.exists(caminho)) {
        String dadosNoArquivo = Files.readString(caminho);
        T[] arrayExistente = gson.fromJson(dadosNoArquivo, tipoArray);
        if (arrayExistente != null) {
            listaDeDados.addAll(Arrays.asList(arrayExistente));
        }
      }
    } catch (IOException e) {
        Tela.exibirMensagem("Não foi possível ler o arquivo");
    } catch (JsonSyntaxException e) {
        Tela.exibirMensagem("JSON inválido no arquivo: " + arquivo);
    }

    listaDeDados.add(objeto);

    String json = gson.toJson(listaDeDados);
    Files.writeString(caminho, json);
  }

  public static <T> T[] deserializar(String arquivo, Class<T[]> tipoArray) throws IOException {
    Gson gson = new Gson();
    Path caminho = Path.of("data", arquivo);

    try {
        String conteudo = Files.readString(caminho);
        return gson.fromJson(conteudo, tipoArray);
    } catch (IOException e) {
        Tela.exibirMensagem(1, 0, "Não foi possível ler o arquivo");
    } catch (JsonSyntaxException e) {
        Tela.exibirMensagem(1, 0, "JSON inválido no arquivo: " + arquivo);
    }

    return null;
  }

}
