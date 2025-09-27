package classes;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.List;

public class Menu {

    public static void criarMenu(List<String> opcoes) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen tela = new DefaultTerminalFactory().createScreen();
        tela.startScreen();

        int selecionado = 0;

        boolean escolhendo = true;
        while (escolhendo) {
            tela.clear();
            TextGraphics tg = tela.newTextGraphics();

            // Desenhar o menu
            for (int i = 0; i < opcoes.size(); i++) {
                if (i == selecionado) {
                    tg.setForegroundColor(TextColor.ANSI.WHITE);
                    tg.setBackgroundColor(TextColor.ANSI.BLUE);
                    tg.putString(2, 2 + i, opcoes.get(i), SGR.BOLD);
                    tg.setBackgroundColor(TextColor.ANSI.DEFAULT);  // reset for next line
                } else {
                    tg.putString(2, 2 + i, opcoes.get(i));
                }
            }

            tela.refresh();

            // Read key input
            KeyStroke tecla = tela.readInput();

            switch (tecla.getKeyType()) {
                case ArrowUp -> selecionado = (selecionado - 1 + opcoes.size()) % opcoes.size();
                case ArrowDown -> selecionado = (selecionado + 1) % opcoes.size();
                case Enter -> // Perform action based on selecionado
                    escolhendo = lerEscolha(opcoes.get(selecionado), tela);
                case Escape -> escolhendo = false;
            }
        }

        tela.stopScreen();
        terminal.close();
    }

    private static boolean lerEscolha(String item, Screen tela) throws IOException {
        tela.clear();
        TextGraphics tg = tela.newTextGraphics();
        switch (item) {
          case "Cadastrar paciente" -> cadastroPaciente(tg, tela);
          case "Cadastrar médico" -> cadastroMedico(tg, tela);
        }
        pausa(3000);

        return !item.equals("Sair");
    }

    public static void pausa(int tempo) {
      try {
          Thread.sleep(tempo);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (IllegalArgumentException e) {
        pausa(1000);
      }
    }

    public static String[] cadastroPaciente(TextGraphics tg, Screen tela) throws IOException {
      tg.putString(2, 2, "Nome do paciente:");
      tela.refresh();
      String nome = Inputs.lerInput(tela, 20, 2);

      tg.putString(2, 4, "CPF do paciente:");
      tela.refresh();
      String cpf = Inputs.lerInput(tela, 19, 4);

      tg.putString(2, 6, "Idade do paciente:");
      tela.refresh();
      String idade = Inputs.lerInput(tela, 21, 6);

      tg.putString(2, 8, ("Paciente cadastrado!"));
      tela.refresh();
      tg.putString(2, 9, ("Nome: " + nome + " CPF: " + cpf + " Idade: " + idade));
      tela.refresh();
      String[] dados = {nome, cpf, idade};
      return dados;
    }

    public static String[] cadastroMedico(TextGraphics tg, Screen tela) throws IOException {
      tg.putString(2, 2, "Nome do médico:");
      tela.refresh();
      String nome = Inputs.lerInput(tela, 18, 2);

      tg.putString(2, 4, "CRM do médico:");
      tela.refresh();
      String crm = Inputs.lerInput(tela, 17, 4);

      tg.putString(2, 6, "Especialidade do médico:");
      tela.refresh();
      String especialidade = Inputs.lerInput(tela, 27, 6);

      tg.putString(2, 8, "Preço da consulta:");
      tela.refresh();
      String custo = Inputs.lerInput(tela, 21, 8);

      tg.putString(2, 10, ("Médico cadastrado cadastrado!"));
      tela.refresh();
      tg.putString(2, 11, ("Nome: " + nome + " CRM: " + crm + " Especialidade: " + especialidade + " Custo:" + custo));
      tela.refresh();
      String[] dados = {nome, crm, especialidade, custo};
      return dados;
    }

}
