package br.hospital.utils;
import java.io.IOException;
import java.util.function.Predicate;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
public class Inputs {
  // lê o que o usuario está digitando no terminal e o altera, indicando erros nos inputs
  public static String lerInput(int col, int row, Predicate<String> verificador, String mensagem) throws IOException {
    StringBuilder input = new StringBuilder();
    KeyStroke tecla;

    while (true) {
        tecla = Tela.lerInput();

        if (tecla.getKeyType() == KeyType.Enter) {
          if (verificador == null || verificador.test(input.toString())) {
            break;
          } else {
            inputInvalido(5, row + 1, mensagem);
            continue;
          }
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() > 0) {
          input.deleteCharAt(input.length() - 1);
          Tela.exibirMensagem(col + input.length(), row, " ");
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() <= 0) {

        } else if (tecla.getCharacter() != null) {
          input.append(tecla.getCharacter());
        }

        Tela.exibirMensagem(col, row, input.toString() + " ");
    }

    return input.toString().trim();
  }

  // lê o input sem verificar a informação dada
  public static String lerInput(int col, int row) throws IOException {
    StringBuilder input = new StringBuilder();
    KeyStroke tecla;

    while (true) {
        tecla = Tela.lerInput();

        if (tecla.getKeyType() == KeyType.Enter) {
          break;
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() > 0) {
          input.deleteCharAt(input.length() - 1);
          Tela.exibirMensagem(col + input.length(), row, " ");
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() <= 0) {

        } else if (tecla.getCharacter() != null) {
          input.append(tecla.getCharacter());
        }

        Tela.exibirMensagem(col, row, input.toString() + " ");
    }

    return input.toString().trim();
  }

  // imprime uma mensagem se um input não é válido
  public static void inputInvalido(int col, int row, String mensagem) throws IOException{
    Tela.exibirMensagem(col, row, mensagem);
    Menu.pausa(1500);
    Tela.exibirMensagem(col, row, " ".repeat(120));
  }
}