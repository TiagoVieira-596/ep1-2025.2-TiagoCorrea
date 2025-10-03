package br.hospital.model;

import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.menu.Menu;
import br.hospital.utils.Inputs;
import br.hospital.utils.Verificador;

public class PacienteEspecial extends Paciente {
  private String planoDeSaude;
  public PacienteEspecial(String nome, String cpf, int idade, String planoDeSaude) {
    super(nome, cpf, idade);
    this.planoDeSaude = planoDeSaude;
  }

  public String getPlanoDeSaude() {
    return planoDeSaude;
  }

  public static void virarPacienteEspecial(TextGraphics tg, Screen tela) throws IOException {
    int linhaAtual = 0;
    for (String plano : Verificador.getPLANOS_VALIDOS()) {
      tg.putString(1, linhaAtual, plano);
      linhaAtual += 1;
    }
    tg.putString(1, linhaAtual + 2, "Que plano você vai cadastrar?");
    tela.refresh();
    String plano = Inputs.lerInput(tela, 31, linhaAtual + 2, Verificador::planoDeSaudeValido, "Digite um dos planos possíveis.");
    tg.putString(1, linhaAtual + 3, "Plano cadastrado!");
    Menu.pausa();
    tela.refresh();
  }

  @Override
  public String toString() {
    return "Paciente Especial: " + super.toString() + " Plano de Saúde: " + planoDeSaude;
  }
}