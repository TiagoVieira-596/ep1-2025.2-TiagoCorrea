package br.hospital.model;
import java.io.IOException;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.Verificador;

public class Consulta {
  private final Paciente paciente;
  private final Medico medico;
  private final String data, horario, local, status;
  private final double custo;
  private String diagnostico, prescricao;

  public Consulta(Paciente paciente, Medico medico, String data, String horario, String local, String status, double custo) {
    this.paciente = paciente;
    this.medico = medico;
    this.data = data;
    this.horario = horario;
    this.local = local;
    this.status = status;
    this.custo = custo;
  }

  public static String[] agendarConsulta() throws IOException {
    Tela.exibirMensagem(2, 1, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 1, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 2, "Nome do médico:");
    String nomeMedico = Inputs.lerInput(18, 2, Verificador::palavraValida, "Nome Inválido: o nome deve conter apenas letras.");

    Tela.exibirMensagem(2, 3, "Data:");
    String data = Inputs.lerInput(8, 3, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    Tela.exibirMensagem(2, 4, "Horário:");
    String horario = Inputs.lerInput(11, 4, Verificador::horarioValido, "Horário inválido. siga o padrão hh:mm");

    Tela.exibirMensagem(2, 5, "Local:");
    String local = Inputs.lerInput(9, 5, Verificador::localValido, "Local inválido: escolha um estado válido, exemplo 'DF'.");

    Tela.exibirMensagem(2, 7, ("Consulta agendada!"));
    String[] dados = {cpf, nomeMedico, data, horario, local};
    Menu.pausa();
    return dados;
  }

  public String getDiagnostico() {
    return diagnostico;
  }
  public void setDiagnostico(String diagnostico) {
    this.diagnostico = diagnostico;
  }

  public String getPrescricao() {
    return prescricao;
  }
  public void setPrescricao(String prescricao) {
    this.prescricao = prescricao;
  }
}