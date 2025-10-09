package br.hospital.model;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Consulta {
  private final String cpfPaciente, nomeMedico, especialidade, data, horario, local;
  private final double custo;
  private String status, diagnostico, prescricao;

  public Consulta(String cpfPaciente, String nomeMedico, String especialidade, String data, String horario, String local, String status, double custo) {
    this.cpfPaciente = cpfPaciente;
    this.nomeMedico = nomeMedico;
    this.especialidade = especialidade;
    this.data = data;
    this.horario = horario;
    this.local = local;
    this.status = status;
    this.custo = custo;
    this.diagnostico = "Sem diagnóstico";
    this.prescricao = "Sem prescrição";
  }

  // agenda (se possível) a consulta com os dados providos e à salva nos arquivos
  public static void agendarConsulta() throws IOException {
    HashMap<String, String> dadosDaConsulta = Consulta.pegarInfoConsulta();
    if (tentarAgendarConsulta(dadosDaConsulta.get("cpf"), dadosDaConsulta.get("nome_medico"), dadosDaConsulta.get("data"), dadosDaConsulta.get("horario"), dadosDaConsulta.get("local"))) {
      Tela.exibirMensagem(2, 7, ("Consulta agendada!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível agendar a consulta."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico e a sua disponibilidade."));
    }
    Menu.pausa(2000);
  }

  // conclui (se possível) a consulta com os dados providos, atualiza o histórico do passiente e a remove dos arquivos de consulta
  public static void concluirConsulta() throws IOException {
    HashMap<String, String> dadosDaConsulta = Consulta.pegarInfoConsulta();
    if (tentarConcluirConsultaPossivel(dadosDaConsulta.get("cpf"), dadosDaConsulta.get("nome_medico"), dadosDaConsulta.get("data"), dadosDaConsulta.get("horario"), dadosDaConsulta.get("local"))) {
      Tela.exibirMensagem(2, 9, ("Consulta Concluída!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível cancelar a consulta."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados da consulta"));
    }
    Menu.pausa(2000);
  }

  // cancela (se possível) a consulta com os dados providos, atualiza o histórico do passiente e a remove dos arquivos de consulta
  public static void cancelarConsulta() throws IOException {
    HashMap<String, String> dadosDaConsulta = Consulta.pegarInfoConsulta();
    if (tentarCancelarConsultaPossivel(dadosDaConsulta.get("cpf"), dadosDaConsulta.get("nome_medico"), dadosDaConsulta.get("data"), dadosDaConsulta.get("horario"), dadosDaConsulta.get("local"))) {
      Tela.exibirMensagem(2, 9, ("Consulta Cancelada."));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível cancelar a consulta."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados da consulta"));
    }
    Menu.pausa(2000);
  }

  // método para pedir as informações necessárias ao usuário sobre uma consulta
  public static HashMap<String, String> pegarInfoConsulta() throws IOException {
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
    
    // retorna um hashmap das informações fornecidas
    HashMap<String, String> dados = new HashMap<>();
    dados.put("cpf", cpf.replace(".", "").replace("-", ""));
    dados.put("nome_medico", nomeMedico.toUpperCase());
    dados.put("data", data);
    dados.put("horario", horario);
    dados.put("local", local.toUpperCase());
    return dados;
  }

  // se for possível, salva a consulta na base de dados e deixa agendada
  public static boolean tentarAgendarConsulta(String cpfPaciente, String nomeMedico, String data, String horario, String local) {
    // acessa as consultas salvas
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");

    // procura o médico e paciente salvos na base de dados com os dados fornecidos
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);

    // filtra as consultas daquele médico específico
    List<Consulta> consultasMedico = repoConsulta.filtrar(p -> p.getNomeMedico().equals(nomeMedico));
    // retorna se o médico está ocupado ou não foram encontrados o médico ou paciente
    if (medico == null || paciente == null) return false;
    Boolean disponivel = medico.getAgenda().get(horario);
    if (disponivel == null || !disponivel) return false;
    for (Consulta consulta : consultasMedico) {
      // duas consultas na mesma hora com o mesmo médico
      if (consulta.getData().equals(data) && consulta.getHorario().equals(horario) && consulta.getNomeMedico().equals(nomeMedico)) return false;
      // duas consultas no mesmo lugar e hora
      if (consulta.getData().equals(data) && consulta.getHorario().equals(horario) && consulta.getLocal().equals(local)) return false;
    }
    
    // pega o custo da consulta e aplica o desconto do plano, se o paciente tem
    double custoConsulta = medico.getCusto();
    if (paciente instanceof PacienteEspecial pacienteEspecial) {
      HashMap<String, Integer> descontos = PacienteEspecial.getDescontos();
      int descontoPlano = descontos.get(pacienteEspecial.getPlanoDeSaude());
      custoConsulta *= (1 - (descontoPlano / 100.0));
    }

    // atualiza as bases de dados
    Consulta consultaAgendada = new Consulta(cpfPaciente, nomeMedico, medico.getEspecialidade(), data, horario, local, "AGENDADA", custoConsulta);
    paciente.novaConsulta(consultaAgendada);
    paciente.atualizarPorCpf(cpfPaciente);
    repoConsulta.adicionar(consultaAgendada);
    return true;
  }

  // se for possível, conclui a consulta na base de dados e deixa agendada
  public static boolean tentarConcluirConsultaPossivel(String cpfPaciente, String nomeMedico, String data, String horario, String local) throws IOException {
    // acessa as consultas salvas
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");

    // procura o médico e paciente salvos na base de dados e retorna se não achar
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null) return false;

    // cria uma consulta temporária para comparação
    Consulta dadosDaConsulta = new Consulta(cpfPaciente, nomeMedico, medico.getEspecialidade(), data, horario, local, "AGENDADA", 0);
    List<Consulta> consultasMedico = repoConsulta.filtrar(p -> p.getNomeMedico().equals(nomeMedico));

    for (Consulta consulta : consultasMedico) {
      if (consulta.equals(dadosDaConsulta)) {
        for (Consulta consultaPaciente : paciente.getConsultas()) {
          // ao encontrar a consulta para concluir, pede o diagnóstico e prescrição
          if (consultaPaciente.equals(consulta)) {
            Tela.exibirMensagem(2, 6, "Diagnóstico:");
            String diagnostico = Inputs.lerInput(15, 6);
            Tela.exibirMensagem(2, 7, "Prescrição:");
            String prescrição = Inputs.lerInput(14, 7);

            // salva as alterações
            consultaPaciente.setDiagnostico(diagnostico);
            consultaPaciente.setPrescricao(prescrição);
            consultaPaciente.setStatus("CONCLUÍDA");
            paciente.atualizarPorCpf(cpfPaciente);
          }
        }
        // tira a consulta dos dados salvos
        repoConsulta.remover(consulta);
        return true;
      }
    }
    return false;
  }

  // se for possível, conclui a consulta na base de dados e deixa agendada
  public static boolean tentarCancelarConsultaPossivel(String cpfPaciente, String nomeMedico, String data, String horario, String local) throws IOException {
    // acessa as consultas salvas
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");

    // procura o médico e paciente salvos na base de dados e retorna se não achar
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null) return false;

    // cria uma consulta temporária para comparação
    Consulta dadosDaConsulta = new Consulta(cpfPaciente, nomeMedico, medico.getEspecialidade(), data, horario, local, "AGENDADA", 0);
    List<Consulta> consultasMedico = repoConsulta.filtrar(p -> p.getNomeMedico().equals(nomeMedico));

    for (Consulta consulta : consultasMedico) {
      if (consulta.equals(dadosDaConsulta)) {
        for (Consulta consultaPaciente : paciente.getConsultas()) {
          // ao encontrar a consulta para cancelar, altera os dados salvos
          if (consultaPaciente.equals(consulta)) {
            consultaPaciente.setStatus("CANCELADA");
            paciente.atualizarPorCpf(cpfPaciente);
          }
        }
        // remove a consulta dos dados salvos
        repoConsulta.remover(consulta);
        return true;
      }
    }
    return false;
  }

  // verifica se a consulta já passou
  public boolean isPassada() {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      LocalDate dataConsulta = LocalDate.parse(getData(), formatador);
      return dataConsulta.isBefore(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  // getters dos dados das consultas
  public String getNomeMedico() { return nomeMedico; }
  public String getEspecialidade() { return especialidade; }
  public String getCpfPaciente() { return cpfPaciente; }
  public String getData() { return data; }
  public String getHorario() { return horario; }
  public String getLocal() { return local; }
  public String getStatus() { return status; }
  public String getDiagnostico() { return diagnostico; }
  public String getPrescricao() { return prescricao; }

  // setters do status, diagnóstico e prescrição
  public void setStatus(String status) { this.status = status; }
  public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
  public void setPrescricao(String prescricao) { this.prescricao = prescricao; }

  // sobrescritas para que a comparação entre consultas não seja feita por referência, mas por valores
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