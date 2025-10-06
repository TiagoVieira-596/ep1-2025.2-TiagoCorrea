package br.hospital.model;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Internacao {
  private final String cpfPaciente, crmMedico, dataDeEntrada;
  private final int quarto;
  private final double custo;
  private String dataDeSaida;

  public Internacao(String cpfPaciente, String crmMedico, String dataDeEntrada, int quarto, double custo) {
    this.cpfPaciente = cpfPaciente;
    this.crmMedico = crmMedico;
    this.dataDeEntrada = dataDeEntrada;
    this.dataDeSaida = "Ainda internado";
    this.quarto = quarto;
    this.custo = custo;
  }

  public static void realizarInternacao() throws IOException {
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

    if (tentarRealizarInternacao(cpf.replace(".", "").replace("-", ""), crm, dataDeEntrada, Integer.parseInt(quarto), Double.parseDouble(custo))) {
      Tela.exibirMensagem(2, 7, ("Internação agendada!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível agendar a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  public static boolean tentarRealizarInternacao(String cpfPaciente, String crm, String dataDeEntrada, int quarto, double custo) {
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarCrmMedico(crm);
    List<Internacao> internacoesMedico = repoInternacao.filtrar(p -> p.getCrm().equals(crm));

    if (medico == null || paciente == null) return false;
    for (Internacao internacao : internacoesMedico) {
      if (internacao.getQuarto() == quarto) return false;
    }
    double custoInternacao = medico.getCusto();
    if (paciente instanceof PacienteEspecial) {
      HashMap<String, Integer> descontos = PacienteEspecial.getDescontos();
      int descontoPlano = descontos.get(((PacienteEspecial) paciente).getPlanoDeSaude());
      if (paciente.getIdade() >= 60) descontoPlano += 5;
      custoInternacao *= (1 - (descontoPlano / 100.0));
    }
    Internacao internacao = new Internacao(cpfPaciente, crm, dataDeEntrada, quarto, custoInternacao);
    paciente.novaInternacao(internacao);
    repoPaciente.atualizar(p -> p.getCpf().equals(cpfPaciente), paciente);
    repoInternacao.adicionar(internacao);
    return true;
  }

  public int getQuarto() {
    return quarto;
  }
  public String getCrm() {
    return crmMedico;
  }
}