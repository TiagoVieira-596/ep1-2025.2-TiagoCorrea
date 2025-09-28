package br.hospital.menu;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import br.hospital.enums.PlanoDeSaude;
import br.hospital.model.Consulta;
import br.hospital.model.Internacao;
import br.hospital.model.Medico;
import br.hospital.model.Paciente;

public class Menu {

    public static void criarMenu(List<String> opcoes, String mensagem) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen tela = new DefaultTerminalFactory().createScreen();
        tela.startScreen();

        int selecionado = 0;

        boolean escolhendo = true;
        while (escolhendo) {
            tela.clear();
            TextGraphics tg = tela.newTextGraphics();
            tg.putString(1, 0, mensagem);
            tela.refresh();

            // desenhar o menu
            for (int i = 0; i < opcoes.size(); i++) {
                if (i == selecionado) {
                  // deixar o selecionado destacado
                    tg.setForegroundColor(TextColor.ANSI.WHITE);
                    tg.setBackgroundColor(TextColor.ANSI.BLUE);
                    tg.putString(2, 2 + i, opcoes.get(i), SGR.BOLD);
                    tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    tg.putString(2, 2 + i, opcoes.get(i));
                }
            }

            tela.refresh();

            // ler o input
            KeyStroke tecla = tela.readInput();

            switch (tecla.getKeyType()) {
                case ArrowUp -> selecionado = (selecionado - 1 + opcoes.size()) % opcoes.size();
                case ArrowDown -> selecionado = (selecionado + 1) % opcoes.size();
                case Enter -> //  ação depende da opção selecionada
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
          case "Paciente" -> criarMenu(Arrays.asList("Cadastrar paciente", "Agendar consulta", "Virar paciente especial", "Voltar"), "O que deseja fazer?");
          case "Médico" -> criarMenu(Arrays.asList("Cadastrar médico", "Internar paciente", "Voltar"), "O que deseja fazer?");
          case "Virar paciente especial" -> criarMenu(PlanoDeSaude.listarPlanos(), "O que deseja fazer?");
          case "Internar paciente" -> Internacao.realizarInternacao(tg, tela);
          case "Agendar consulta" -> Consulta.agendarConsulta(tg, tela);
          case "Cadastrar paciente" -> Paciente.cadastroPaciente(tg, tela);
          case "Cadastrar médico" -> Medico.cadastroMedico(tg, tela);
          case "Voltar" -> item = "Sair";
        }

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
}
