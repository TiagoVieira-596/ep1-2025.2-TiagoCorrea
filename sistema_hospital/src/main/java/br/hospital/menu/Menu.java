package br.hospital.menu;
import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;

import br.hospital.model.Consulta;
import br.hospital.model.Internacao;
import br.hospital.model.Medico;
import br.hospital.model.Paciente;
import br.hospital.model.PacienteEspecial;

public class Menu {
  public static void criarMenu(List<String> opcoes, String mensagem) throws IOException {
      int selecionado = 0;

      boolean escolhendo = true;
      while (escolhendo) {
          Tela.limpar();
          Tela.exibirMensagem(mensagem);

          // desenhar o menu
          for (int i = 0; i < opcoes.size(); i++) {
              if (i == selecionado) {
                // deixar o selecionado destacado
                  Tela.getTextGraphics().setForegroundColor(TextColor.ANSI.WHITE);
                  Tela.getTextGraphics().setBackgroundColor(TextColor.ANSI.BLUE);
                  Tela.getTextGraphics().putString(2, 2 + i, opcoes.get(i), SGR.BOLD);
                  Tela.getTextGraphics().setBackgroundColor(TextColor.ANSI.DEFAULT);
              } else {
                  Tela.getTextGraphics().putString(2, 2 + i, opcoes.get(i));
              }
          }

          Tela.atualizar();

          // ler o input
          KeyStroke tecla = Tela.lerInput();

          switch (tecla.getKeyType()) {
              case ArrowUp -> selecionado = (selecionado - 1 + opcoes.size()) % opcoes.size();
              case ArrowDown -> selecionado = (selecionado + 1) % opcoes.size();
              case Enter -> escolhendo = lerEscolha(opcoes.get(selecionado));
              case Escape -> escolhendo = false;
          }
      }
  }

  private static boolean lerEscolha(String item) throws IOException {
      Tela.limpar();
      switch (item) {
        case "Paciente" -> criarMenu(List.of("Cadastrar paciente", "Agendar consulta", "Virar paciente especial", "Visualizar consultas", "Cancelar consulta", "Voltar"), "O que deseja fazer?");
        case "Médico" -> criarMenu(List.of("Cadastrar médico", "Concluir consulta", "Internar paciente", "Concluir internação", "Cancelar internação", "Voltar"), "O que deseja fazer?");
        case "Virar paciente especial" -> PacienteEspecial.virarPacienteEspecial();
        case "Internar paciente" -> Internacao.realizarInternacao();
        case "Concluir internação" -> Internacao.concluirInternacao();
        case "Cancelar internação" -> Internacao.cancelarInternacao();
        case "Agendar consulta" -> Consulta.agendarConsulta();
        case "Concluir consulta" -> Consulta.concluirConsulta();
        case "Cancelar consulta" -> Consulta.cancelarConsulta();
        case "Cadastrar paciente" -> Paciente.cadastroPaciente();
        case "Cadastrar médico" -> Medico.cadastroMedico();
        case "Voltar" -> {
          return false;
        }
        case "Sair" -> {
          Tela.encerrarTela();
          Tela.getTerminal().close();
          System.exit(0);
        }
      }
      return true;
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

  public static void pausa() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
