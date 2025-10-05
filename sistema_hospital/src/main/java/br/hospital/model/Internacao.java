package br.hospital.model;
import java.io.IOException;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.Verificador;

public class Internacao {
  private final Paciente paciente;
  private final Medico medicoResponsavel;
  private final String dataDeEntrada, dataDeSaida, quarto;
  private final double custo;

  public Internacao(Paciente paciente, Medico medicoResponsavel, String dataDeEntrada, String dataDeSaida, String quarto, double custo) {
    this.paciente = paciente;
    this.medicoResponsavel = medicoResponsavel;
    this.dataDeEntrada = dataDeEntrada;
    this.dataDeSaida = dataDeSaida;
    this.quarto = quarto;
    this.custo = custo;
  }

  public static String[] realizarInternacao() throws IOException {
    Tela.exibirMensagem(2, 1, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 1, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 2, "CRM do médico:");
    String crm = Inputs.lerInput(17, 2, Verificador::crmValido, "CRM inválido: siga o padrão 'código/estado'.");

    Tela.exibirMensagem(2, 3, "Data de internação:");
    String dataDeEntrada = Inputs.lerInput(22, 3, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    Tela.exibirMensagem(2, 4, "Quarto:");
    String quarto = Inputs.lerInput(10, 4, Verificador::numeroValido, "Quarto inexistente.");

    Tela.exibirMensagem(2, 5, "Custo:");
    String custo = Inputs.lerInput(9, 5, Verificador::numeroValido, "Custo de internação inválido.");

    Tela.exibirMensagem(2, 7, ("Internação realizada!"));
    String[] dados = {cpf, crm, dataDeEntrada, quarto, custo};
    Menu.pausa();
    return dados;
  }
}