package br.hospital.model;
import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.enums.StatusConsulta;
import br.hospital.menu.Menu;
import br.hospital.utils.Inputs;

public class Consulta {
  private final Paciente paciente;
  private final Medico medico;
  private final String data;
  private final String horario; 
  private final String local;
  private final StatusConsulta status;
  private final double custo;
  private String diagnostico;
  private String prescricao;

  public Consulta(Paciente paciente, Medico medico, String data, String horario, String local, StatusConsulta status, double custo) {
    this.paciente = paciente;
    this.medico = medico;
    this.data = data;
    this.horario = horario;
    this.local = local;
    this.status = status;
    this.custo = custo;
  }

  public static String[] agendarConsulta(TextGraphics tg, Screen tela) throws IOException {
    tg.putString(2, 1, "CPF do paciente:");
    tela.refresh();
    String cpf = Inputs.lerInput(tela, 19, 1);

    tg.putString(2, 2, "CRM do médico:");
    tela.refresh();
    String crm = Inputs.lerInput(tela, 17, 2);

    tg.putString(2, 3, "Data:");
    tela.refresh();
    String data = Inputs.lerInput(tela, 8, 3);

    tg.putString(2, 4, "Horário:");
    tela.refresh();
    String horario = Inputs.lerInput(tela, 11, 4);

    tg.putString(2, 5, "Local:");
    tela.refresh();
    String local = Inputs.lerInput(tela, 9, 5);

    tg.putString(2, 7, ("Consulta agendada!"));
    tela.refresh();
    String[] dados = {cpf, crm, data, horario, local};
    Menu.pausa(1000);
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