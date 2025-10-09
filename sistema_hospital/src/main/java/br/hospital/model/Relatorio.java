package br.hospital.model;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Relatorio {
  public static void relatorioPacientes() throws IOException {
    int linha = 0;
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");

    List<Paciente> pacientes = repoPaciente.listar();
    List<PacienteEspecial> pacientesEspeciais = repoPacienteEspecial.listar();
    List<Paciente> todosPacientes = new ArrayList<>();

    if (!todosPacientes.addAll(pacientes) && !todosPacientes.addAll(pacientesEspeciais)) {
      Tela.exibirMensagem("Não foram encontrados pacientes.");
      Menu.pausa();
      return;
    }
      for (Paciente p : todosPacientes) {
          Tela.exibirMensagem(1, linha, "Paciente: " + p.getNome() + " - CPF: " + p.getCpf());
          linha += 1;
          
          Tela.exibirMensagem(1, linha, "Consultas:");
          linha += 1;
          for (Consulta c : p.getConsultas()) {
              Tela.exibirMensagem(1, linha, "- " + c.getData() + ", " + c.getEspecialidade() + ", " + "Doutor: " + c.getNomeMedico());
              linha += 1;
          }

          Tela.exibirMensagem(1, linha, "Internações:");
          linha += 1;
          for (Internacao i : p.getInternacoes()) {
              Tela.exibirMensagem(1, linha, "- Entrada: " + i.getDataDeEntrada() + ", Saída: " + i.getDataDeSaida());
              linha += 1;
          }
      }
    // esperar 2,5 segundos por linha printada
    Menu.pausa(linha * 1000);
  }
  
  public static void relatorioMedicos() throws IOException {
    int linha = 0;
    RepositorioJson<Medico> repoMedico = new RepositorioJson(Medico[].class, "dados_medicos.json");
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");

    List<Medico> medicos = repoMedico.listar();
    List<Consulta> consultas = repoConsulta.listar();
    
    if (medicos.isEmpty()) {
      Tela.exibirMensagem("Não foram encontrados médicos.");
      Menu.pausa();
      return;
    }
    for (Medico m : medicos) {
      Tela.exibirMensagem(1, linha++, "Médico: " + m.getNome() + " - CRM: " + m.getCrm());
      Tela.exibirMensagem(1, linha++, "Especialidade: " + m.getEspecialidade());

      long realizadas = consultas.stream()
          .filter(c -> c.getNomeMedico().equals(m.getNome()) && c.isPassada())
          .count();
      long futuras = consultas.stream()
          .filter(c -> c.getNomeMedico().equals(m.getNome()) && !c.isPassada())
          .count();

      Tela.exibirMensagem(1, linha++, "Consultas realizadas: " + realizadas);
      Tela.exibirMensagem(1, linha++, "Consultas futuras: " + futuras);
      linha++;
    }
    Menu.pausa(linha * 1000);
  }

  public static void relatorioConsultasFiltradas() throws IOException {
    int linha = 0;

    Tela.exibirMensagem(1, linha + 1, "CPF do paciente:");
    String cpf = Inputs.lerInput(18, linha + 1, Verificador::cpfValido, "CPF inválido.");
    Tela.exibirMensagem(1, linha + 2, "Nome do médico:");
    String nomeMedico = Inputs.lerInput(17, linha + 2, Verificador::palavraValida, "Nome inválido, use apenas letras.");
    Tela.exibirMensagem(1, linha + 3, "Especialidade do médico:");
    String especialidade = Inputs.lerInput(26, linha + 3, Verificador::especialidadeValida, "Especialidade inválida.");

    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");

    List<Consulta> consultas = repoConsulta.listar().stream()
        .filter(c -> (cpf == null || c.getCpfPaciente().equals(cpf)))
        .filter(c -> (nomeMedico == null || c.getNomeMedico().equals(nomeMedico)))
        .filter(c -> (especialidade == null || c.getEspecialidade().equalsIgnoreCase(especialidade)))
        .toList();

    if (consultas.isEmpty()) {
      Tela.exibirMensagem("Não foram encontradas consultas com esses parâmetros de busca.");
      Menu.pausa();
      return;
    }
    for (Consulta c : consultas) {
      Tela.exibirMensagem(1, linha++, "- " + c.getData() + ", " + c.getEspecialidade() + ", Paciente: " + c.getCpfPaciente() + ", Médico: " + c.getNomeMedico());
    }
    Menu.pausa(linha * 1000);
  }

  public static void relatorioPacientesInternados() throws IOException {
    int linha = 0;
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");

    List<Paciente> todos = new ArrayList<>();
    todos.addAll(repoPaciente.listar());
    todos.addAll(repoPacienteEspecial.listar());

    LocalDate hoje = LocalDate.now();

    boolean internacaoEncontrada = false;
    for (Paciente p : todos) {
      for (Internacao i : p.getInternacoes()) {
        if (i.getDataDeSaida().equals("Ainda internado")) {
          LocalDate entrada = LocalDate.parse(i.getDataDeEntrada(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
          long dias = ChronoUnit.DAYS.between(entrada, hoje);
          Tela.exibirMensagem(1, linha++, p.getNome() + " - Internado há " + dias + " dias - Quarto: " + i.getQuarto());
          internacaoEncontrada = true;
        }
      }
    }
  if (!internacaoEncontrada) {
    Tela.exibirMensagem(1, linha++, "Não foram encontradas internações.");
    Menu.pausa();
    return;
  }
  Menu.pausa(linha * 1000);
}


  public static void relatorioEstatisticasGerais() throws IOException {
    int linha = 0;
    RepositorioJson<Consulta> repoConsulta = new RepositorioJson(Consulta[].class, "dados_consultas.json");
    List<Consulta> consultas = repoConsulta.listar();

    // Médico que mais atendeu
    Map<String, Long> atendimentos = consultas.stream()
        .collect(Collectors.groupingBy(Consulta::getNomeMedico, Collectors.counting()));

    var maisAtendeu = atendimentos.entrySet().stream()
        .max(Map.Entry.comparingByValue()).orElse(null);

    boolean dadosEncontrados = false;
    if (maisAtendeu != null) {
      dadosEncontrados = true;
        Tela.exibirMensagem(1, linha++, "Médico que mais atendeu: " + maisAtendeu.getKey() + " - " + maisAtendeu.getValue() + " consultas");
    }

    // Especialidade mais procurada
    Map<String, Long> porEspecialidade = consultas.stream()
        .collect(Collectors.groupingBy(Consulta::getEspecialidade, Collectors.counting()));

    var maisProcurada = porEspecialidade.entrySet().stream()
        .max(Map.Entry.comparingByValue()).orElse(null);

    if (maisProcurada != null) {
      dadosEncontrados = true;
      Tela.exibirMensagem(1, linha++, "Especialidade mais procurada: " + maisProcurada.getKey() + " - " + maisProcurada.getValue() + " consultas");
    }
    if (!dadosEncontrados) {
      Tela.exibirMensagem(1, linha++, "Não há dados suficientes para as estatísticas.");
      Menu.pausa();
      return;
    }
    Menu.pausa(linha * 1000);
  }

  public static void relatorioPlanosDeSaude() throws IOException {
    int linha = 0;
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");

    List<PacienteEspecial> pacientes = repoPacienteEspecial.listar();

    if (pacientes.isEmpty()) {
      Tela.exibirMensagem("Não foram encontrados pacientes com plano de saúde");
      Menu.pausa();
    }

    Map<String, List<PacienteEspecial>> porPlano = pacientes.stream()
        .collect(Collectors.groupingBy(PacienteEspecial::getPlanoDeSaude));

    for (String plano : porPlano.keySet()) {
      double total = 0, comPlano = 0;
      int totalPacientes = porPlano.get(plano).size();

      for (Paciente p : porPlano.get(plano)) {
        for (Internacao i : p.getInternacoes()) {
          total += i.getCustoTotal();
          comPlano += i.getCustoComPlano(); // precisa existir esse método
        }
      }

      Tela.exibirMensagem(1, linha++, "Plano: " + plano);
      Tela.exibirMensagem(1, linha++, "Pacientes: " + totalPacientes);
      Tela.exibirMensagem(1, linha++, String.format("Economia total: R$ %.2f", (total - comPlano)));
      linha++;
    }
    Menu.pausa(linha * 1000);
  }
}
