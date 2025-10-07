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

public class Internacao {
  private final String cpfPaciente, nomeMedico, dataDeEntrada;
  private final int quarto;
  private final double custoTotal, custoComPlano;
  private String dataDeSaida;

  public Internacao(String cpfPaciente, String nomeMedico, String dataDeEntrada, int quarto, double custoTotal, double custoComPlano) {
    this.cpfPaciente = cpfPaciente;
    this.nomeMedico = nomeMedico;
    this.dataDeEntrada = dataDeEntrada;
    this.dataDeSaida = "Ainda internado";
    this.quarto = quarto;
    this.custoTotal = custoTotal;
    this.custoComPlano = custoComPlano;
  }

  public static void realizarInternacao() throws IOException {
    String[] dados = pegarInputInternacao();
    Tela.exibirMensagem(2, 5, "Custo:");
    String custo = Inputs.lerInput(9, 5, Verificador::numeroValido, "Custo de internação inválido.");

    if (tentarRealizarInternacaoPossivel(dados[0], dados[1], dados[2], Integer.parseInt(dados[3]), Double.parseDouble(custo))) {
      Tela.exibirMensagem(2, 7, ("Internação agendada!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível agendar a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  public static void concluirInternacao() throws IOException {
    String[] dados = pegarInputInternacao();

    if (tentarConcluirInternacaoPossivel(dados[0], dados[1], dados[2], Integer.parseInt(dados[3]))) {
      Tela.exibirMensagem(2, 7, ("Internação concluida!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível concluir a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }
  public static void cancelarInternacao() throws IOException {
    String[] dados = pegarInputInternacao();

    if (tentarCancelarInternacaoPossivel(dados[0], dados[1], dados[2], Integer.parseInt(dados[3]))) {
      Tela.exibirMensagem(2, 7, ("Internação cancelada"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível cancelar a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  public static String[] pegarInputInternacao() throws IOException{
    Tela.exibirMensagem(2, 1, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 1, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 2, "Nome do médico:");
    String nome = Inputs.lerInput(18, 2, Verificador::palavraValida, "Nome inválido: siga o padrão 'código/estado'.");

    Tela.exibirMensagem(2, 3, "Data de internação:");
    String dataDeEntrada = Inputs.lerInput(22, 3, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    Tela.exibirMensagem(2, 4, "Quarto:");
    String quarto = Inputs.lerInput(10, 4, Verificador::numeroValido, "Quarto inexistente.");
    String[] dados = {cpf.replace(".", "").replace("-", ""), nome.toUpperCase(), dataDeEntrada, quarto};
    return dados;
  }

  public static boolean tentarRealizarInternacaoPossivel(String cpfPaciente, String nome, String dataDeEntrada, int quarto, double custo) {
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nome);
    if (medico == null || paciente == null) return false;

    if (repoInternacao.buscar(p -> p.getQuarto() == quarto) != null) return false;

    double custoInternacao = custo;
    if (paciente instanceof PacienteEspecial pacienteEspecial) {
      HashMap<String, Integer> descontos = PacienteEspecial.getDescontos();
      int descontoPlano = descontos.get(pacienteEspecial.getPlanoDeSaude());
      if (paciente.getIdade() >= 60) descontoPlano += 5;
      custoInternacao *= (1 - (descontoPlano / 100.0));
    }

    Internacao internacao = new Internacao(cpfPaciente, nome, dataDeEntrada, quarto, custo, Math.round(custoInternacao));
    paciente.novaInternacao(internacao);
    paciente.atualizarPorCpf(cpfPaciente);
    repoInternacao.adicionar(internacao);
    return true;
  }

  public static boolean tentarConcluirInternacaoPossivel(String cpfPaciente, String nomeMedico, String dataDeEntrada, int quarto) throws IOException {
    Tela.exibirMensagem(2, 5, "Data de alta:");
    String dataDeSaida = Inputs.lerInput(16, 5, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null || Verificador.dataAntesDaOutra(dataDeEntrada, dataDeSaida)) return false;

    Internacao dadosDaInternacao = new Internacao(cpfPaciente, nomeMedico, dataDeEntrada, quarto, 0, 0);
    List<Internacao> internacoes = repoInternacao.filtrar(p -> p.getCpf().equals(cpfPaciente));

    for (Internacao internacao : internacoes) {
      if (internacao.equals(dadosDaInternacao)) {
        for (Internacao internacaoPaciente : paciente.getInternacoes()) {
          if (internacaoPaciente.equals(internacao)) {
            internacaoPaciente.setDataDeSaida(dataDeSaida);
            paciente.atualizarPorCpf(cpfPaciente);
          }
        }
        repoInternacao.remover(internacao);
        return true;
      }
    }
    return false;
  }

  public static boolean tentarCancelarInternacaoPossivel(String cpfPaciente, String nomeMedico, String dataDeEntrada, int quarto) throws IOException {
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null) return false;

    Internacao dadosDaInternacao = new Internacao(cpfPaciente, nomeMedico, dataDeEntrada, quarto, 0, 0);
    List<Internacao> internacoes = repoInternacao.filtrar(p -> p.getCpf().equals(cpfPaciente));

    for (Internacao internacao : internacoes) {
      if (internacao.equals(dadosDaInternacao)) {
        for (Internacao internacaoPaciente : paciente.getInternacoes()) {
          if (internacaoPaciente.equals(internacao)) {
            internacaoPaciente.setDataDeSaida("Internação cancelada.");
            paciente.atualizarPorCpf(cpfPaciente);
          }
        }
        repoInternacao.remover(internacao);
        return true;
      }
    }
    return false;
  }

  public int getQuarto() {
    return quarto;
  }
  public String getCpf() {
    return cpfPaciente;
  }
  public double getCustoTotal() {
    return custoTotal;
  }
  public double getCustoComPlano() {
    return custoComPlano;
  }
  public String getDataDeEntrada() {
    return dataDeEntrada;
  }
  public String getDataDeSaida() {
    return dataDeSaida;
  }

  public void setDataDeSaida(String dataDeSaida) {
    this.dataDeSaida = dataDeSaida;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Internacao internacao = (Internacao) o;

    return cpfPaciente.equals(internacao.cpfPaciente)
            && nomeMedico.equals(internacao.nomeMedico)
            && dataDeEntrada.equals(internacao.dataDeEntrada)
            && quarto == internacao.quarto;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpfPaciente, nomeMedico, dataDeEntrada, quarto);
  }
}