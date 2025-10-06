package br.hospital.model;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Consulta {
  private final String cpfPaciente, nomeMedico, data, horario, local;
  private final double custo;
  private String status, diagnostico, prescricao;

  public Consulta(String cpfPaciente, String nomeMedico, String data, String horario, String local, String status, double custo) {
    this.cpfPaciente = cpfPaciente;
    this.nomeMedico = nomeMedico;
    this.data = data;
    this.horario = horario;
    this.local = local;
    this.status = status;
    this.custo = custo;
    this.diagnostico = "Sem diagnóstico";
    this.prescricao = "Sem prescrição";
  }

  public static void agendarConsulta() throws IOException {
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

    if (tentarAgendarConsulta(cpf, nomeMedico.toUpperCase(), data, horario, local.toUpperCase())) {
      Tela.exibirMensagem(2, 7, ("Consulta agendada!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível agendar a consulta."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico e a sua disponibilidade."));
    }
    Menu.pausa(2000);
  }

  public static void concluirConsulta() throws IOException {
    Tela.exibirMensagem(2, 1, "CPF do paciente:");
    String cpfPaciente = Inputs.lerInput(19, 1, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 2, "Nome do médico:");
    String nomeMedico = Inputs.lerInput(18, 2, Verificador::palavraValida, "Nome Inválido: o nome deve conter apenas letras.");

    Tela.exibirMensagem(2, 3, "Data:");
    String data = Inputs.lerInput(8, 3, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    Tela.exibirMensagem(2, 4, "Horário:");
    String horario = Inputs.lerInput(11, 4, Verificador::horarioValido, "Horário inválido. siga o padrão hh:mm");

    Tela.exibirMensagem(2, 5, "Local:");
    String local = Inputs.lerInput(9, 5, Verificador::localValido, "Local inválido: escolha um estado válido, exemplo 'DF'.");
    
    if (tentarConcluirConsultaPossivel(cpfPaciente, nomeMedico.toUpperCase(), data, horario, local.toUpperCase())) {
      Tela.exibirMensagem(2, 9, ("Consulta Concluída!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível cancelar a consulta."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados da consulta"));
    }
    Menu.pausa(2000);
  }

  public String getNomeMedico() {
    return nomeMedico;
  }
  public String getCpfPaciente() {
    return cpfPaciente;
  }
  public String getData() {
    return data;
  }
  public String getHorario() {
    return horario;
  }
  public String getLocal() {
    return local;
  }

  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
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

  public static boolean tentarAgendarConsulta(String cpfPaciente, String nomeMedico, String data, String horario, String local) {
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    List<Consulta> consultasMedico = repoConsulta.filtrar(p -> p.getNomeMedico().equals(nomeMedico));

    if (medico == null || paciente == null) return false;
    for (Consulta consulta : consultasMedico) {
      if (consulta.getData().equals(data) && consulta.getHorario().equals(horario) && consulta.getNomeMedico().equals(nomeMedico)) return false;
      if (consulta.getData().equals(data) && consulta.getHorario().equals(horario) && consulta.getLocal().equals(local)) return false;
    }
    double custoConsulta = medico.getCusto();
    if (paciente instanceof PacienteEspecial) {
      HashMap<String, Integer> descontos = PacienteEspecial.getDescontos();
      int descontoPlano = descontos.get(((PacienteEspecial) paciente).getPlanoDeSaude());
      custoConsulta *= (1 - (descontoPlano / 100.0));
    }
    Consulta consultaAgendada = new Consulta(cpfPaciente, nomeMedico, data, horario, local, "AGENDADA", custoConsulta);
    paciente.novaConsulta(consultaAgendada);
    repoPaciente.atualizar(p -> p.getCpf().equals(cpfPaciente), paciente);
    repoConsulta.adicionar(consultaAgendada);
    return true;
  }

  public static boolean tentarConcluirConsultaPossivel(String cpfPaciente, String nomeMedico, String data, String horario, String local) throws IOException {
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    Consulta dadosDaConsulta = new Consulta(cpfPaciente, nomeMedico, data, horario, local, "AGENDADA", 0);
    List<Consulta> consultasMedico = repoConsulta.filtrar(p -> p.getNomeMedico().equals(nomeMedico));
    if (medico == null || paciente == null) return false;
    for (Consulta consulta : consultasMedico) {
      if (consulta.equals(dadosDaConsulta)) {
        for (Consulta consultaPaciente : paciente.getConsultas()) {
          if (consultaPaciente.equals(consulta)) {
            Tela.exibirMensagem(2, 6, "Diagnóstico:");
            String diagnostico = Inputs.lerInput(15, 6);
            Tela.exibirMensagem(2, 7, "Prescrição:");
            String prescrição = Inputs.lerInput(14, 7);

            consultaPaciente.setDiagnostico(diagnostico);
            consultaPaciente.setPrescricao(prescrição);
            consultaPaciente.setStatus("CONCLUÍDA");
            repoPaciente.atualizar(p -> p.getCpf().equals(cpfPaciente), paciente);
          }
        }
        repoConsulta.remover(consulta);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Consulta consulta = (Consulta) o;

    return data.equals(consulta.data)
            && horario.equals(consulta.horario)
            && local.equals(consulta.local)
            && nomeMedico.equals(consulta.nomeMedico)
            && cpfPaciente.equals(consulta.cpfPaciente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, horario, local, nomeMedico, cpfPaciente);
  }
}