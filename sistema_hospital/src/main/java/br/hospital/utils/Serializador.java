package br.hospital.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Serializador {
  public static void serializar(Object objeto, String arquivo, Screen tela, TextGraphics tg) throws IOException{
    Gson gson = new Gson();
    String json = gson.toJson(objeto);
    try {
      Files.createDirectories(Path.of("data"));
      Files.writeString(Path.of("data/" + arquivo), json);
    } catch (IOException e) {
      tg.putString(1, 0, "Não foi possível salvar o arquivo.");
      tela.refresh();
    }
  }
  public static <T> T deserializar(Class<T> classe, String arquivo, Screen tela, TextGraphics tg) throws IOException{
    Gson gson = new Gson();
    try {
      String dados = Files.readString(Path.of("data/" + arquivo));
      return gson.fromJson(dados, classe);
    } catch (IOException | JsonSyntaxException e) {
      tg.putString(1, 0, "Não foi possível lêr o arquivo.");
      tela.refresh();
    }
    return null;
  }
}
