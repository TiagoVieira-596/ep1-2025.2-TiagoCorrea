package br.hospital.menu;
import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

public class Tela {
  private static Screen tela;
  private static TextGraphics tg;
  private static Terminal terminal;

  // inicializa uma nova interface de terminal para ser alterada
  public static void configurar(Screen novaTela, TextGraphics novoTg, Terminal novoTerminal) throws IOException {
    tela = novaTela;
    tg = novoTg;
    terminal = novoTerminal;
    tela.startScreen();
  }

  private Tela() {}

  // exibe uma mensagem na primeira linha do terminal
  public static void exibirMensagem(String mensagem) {
    try {
      if (tg != null && tela != null) {
        // limpa a linha e mostra a mensagem
        tg.putString(1, 0, " ".repeat(tela.getTerminalSize().getColumns() - 1));
        tg.putString(1, 0, mensagem);
        tela.refresh();
      } else {
        System.err.println("Tela ou TextGraphics não configurados.");
      }
    } catch (IOException e) {
      System.err.println("Erro ao exibir mensagem na tela: " + e.getMessage());
    }
  }

  // mostra a mensagem em um local determinado do terminal
  public static void exibirMensagem(int coluna, int linha, String mensagem) {
    try {
      if (tg != null && tela != null) {
        tg.putString(coluna, linha, " ".repeat(tela.getTerminalSize().getColumns() - 1));
        tg.putString(coluna, linha, mensagem);
        tela.refresh();
      } else {
        System.err.println("Tela ou TextGraphics não configurados.");
      }
    } catch (IOException e) {
      System.err.println("Erro ao exibir mensagem na tela: " + e.getMessage());
    }
  }

  // métodos estaticos para maior facilidade de uso e alteração da tela
  public static void limpar() { tela.clear(); }
  public static void atualizar() throws IOException { tela.refresh(); }
  public static KeyStroke lerInput() throws IOException { return tela.readInput(); }
  public static void encerrarTela() throws IOException { tela.stopScreen(); }

  // getters dos atributos da Tela
  public static TextGraphics getTextGraphics() { return tg; }
  public static Screen getTela() { return tela; }
  public static Terminal getTerminal() { return terminal; }
}
