package br.hospital.utils;
import java.io.IOException;
import java.util.function.Predicate;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.menu.Menu;
public class Inputs {
  // lê o que o usuario está digitando no terminal e o altera, indicando erros nos inputs
  public static String lerInput(Screen screen, int col, int row, Predicate<String> verificador, String mensagem) throws IOException {
    StringBuilder input = new StringBuilder();
    TextGraphics tg = screen.newTextGraphics();
    KeyStroke tecla;

    while (true) {
        tecla = screen.readInput();

        if (tecla.getKeyType() == KeyType.Enter) {
          if (verificador == null || verificador.test(input.toString())) {
            break;
          } else {
            inputInvalido(tg, screen, col, row + 1, mensagem);
            continue;
          }
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() > 0) {
          input.deleteCharAt(input.length() - 1);
          tg.putString(col + input.length(), row, " ");
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() <= 0) {

        } else if (tecla.getCharacter() != null) {
          input.append(tecla.getCharacter());
        }

        tg.putString(col, row, input.toString() + " ");
        screen.refresh();
    }

    return input.toString().trim();
  }

  // lê o input sem verificar a informação dada
  public static String lerInput(Screen screen, int col, int row) throws IOException {
    StringBuilder input = new StringBuilder();
    TextGraphics tg = screen.newTextGraphics();
    KeyStroke tecla;

    while (true) {
        tecla = screen.readInput();

        if (tecla.getKeyType() == KeyType.Enter) {
          break;
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() > 0) {
          input.deleteCharAt(input.length() - 1);
          tg.putString(col + input.length(), row, " ");
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() <= 0) {

        } else if (tecla.getCharacter() != null) {
          input.append(tecla.getCharacter());
        }

        tg.putString(col, row, input.toString() + " ");
        screen.refresh();
    }

    return input.toString().trim();
  }

  public static void inputInvalido(TextGraphics tg, Screen screen, int col, int row, String mensagem) throws IOException{
    tg.putString(col, row, mensagem);
    screen.refresh();
    Menu.pausa(1500);
    tg.putString(col, row, " ".repeat(40));
    screen.refresh();
  }
}