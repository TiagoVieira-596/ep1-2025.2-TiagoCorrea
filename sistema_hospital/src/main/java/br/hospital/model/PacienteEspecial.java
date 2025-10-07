package br.hospital.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class PacienteEspecial extends Paciente {
  private static final HashMap<String, Integer> descontos = new HashMap<>();

static {
    descontos.put("CASSI", 90);
    descontos.put("UNIMED", 65);
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
  public PacienteEspecial(String nome, String cpf, int idade, String planoDeSaude, List<Consulta> consultas, List<Internacao> internacoes) {
    super(nome, cpf, idade, consultas, internacoes);
    this.planoDeSaude = planoDeSaude;
  }

  public String getPlanoDeSaude() {
    return planoDeSaude;
  }
  public void setPlanoDeSaude(String planoDeSaude) {
    this.planoDeSaude = planoDeSaude;
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
    Tela.exibirMensagem(1, linhaAtual + 2, "CPF do paciente:");
    String cpfPaciente = Inputs.lerInput(19, linhaAtual + 2, Verificador::cpfValido, "CPF inválido.");
    Tela.exibirMensagem(1, linhaAtual + 3, "Que plano você vai cadastrar?");
    String plano = Inputs.lerInput(31, linhaAtual + 3, Verificador::planoDeSaudeValido, "Digite um dos planos possíveis.");

    if (tentarCriarPacienteEspecial(cpfPaciente.replace(".", "").replace("-", ""), plano.toUpperCase())) {
      Tela.exibirMensagem(1, linhaAtual + 4, "Plano cadastrado!");
    } else {
      Tela.exibirMensagem(1, linhaAtual + 4, "Paciente não encontrado / Plano atual já é esse.");
    }
    Menu.pausa(2000);
  }

  public static boolean tentarCriarPacienteEspecial(String cpfPaciente, String plano) {
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    if (paciente == null) return false;
    if (paciente instanceof PacienteEspecial pacienteEspecial) {
      if (plano.equals(((PacienteEspecial) paciente).getPlanoDeSaude())) return false;
      pacienteEspecial.setPlanoDeSaude(plano);
      repoPacienteEspecial.atualizar(p -> p.getCpf().equals(cpfPaciente), (PacienteEspecial) paciente);
    } else {
      repoPaciente.remover(p -> p.getCpf().equals(cpfPaciente));
      PacienteEspecial novoPacienteEspecial = new PacienteEspecial(paciente.getNome(), paciente.getCpf(), paciente.getIdade(), plano, paciente.getConsultas(), paciente.getInternacoes());
      repoPacienteEspecial.adicionar(novoPacienteEspecial);
    }
    return true;
  }

  @Override
  public String toString() {
    return "Paciente Especial: " + super.toString() + " Plano de Saúde: " + planoDeSaude;
  }
}