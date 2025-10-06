package br.hospital.model;

import java.io.IOException;
import java.util.HashMap;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.Verificador;

public class PacienteEspecial extends Paciente {
  private static final HashMap<String, Integer> descontos = new HashMap<>();

static {
    descontos.put("CASSI", 100);
    descontos.put("UNIMED", 80);
    descontos.put("AMIL", 75);
    descontos.put("BRADESCO SAÚDE", 50);
    descontos.put("SULAMÉRICA", 40);
    descontos.put("NOTREDAME INTERMÉDICA", 25);
    descontos.put("OUTRO", 10);
}
  private String planoDeSaude;
  public PacienteEspecial(String nome, String cpf, int idade, String planoDeSaude) {
    super(nome, cpf, idade);
    this.planoDeSaude = planoDeSaude;
  }

  public String getPlanoDeSaude() {
    return planoDeSaude;
  }
  public static HashMap<String, Integer> getDescontos() {
    return descontos;
  }

  public static void virarPacienteEspecial() throws IOException {
    int linhaAtual = 0;
    for (String plano : Verificador.getPLANOS_VALIDOS()) {
      Tela.exibirMensagem(1, linhaAtual, plano);
      linhaAtual += 1;
    }
    Tela.exibirMensagem(1, linhaAtual + 2, "Que plano você vai cadastrar?");
    String plano = Inputs.lerInput(31, linhaAtual + 2, Verificador::planoDeSaudeValido, "Digite um dos planos possíveis.");
    Tela.exibirMensagem(1, linhaAtual + 3, "Plano cadastrado!");
    Menu.pausa();
  }

  @Override
  public String toString() {
    return "Paciente Especial: " + super.toString() + " Plano de Saúde: " + planoDeSaude;
  }
}