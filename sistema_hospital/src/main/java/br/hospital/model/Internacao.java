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
  private double custoTotal, custoComPlano;
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

  // realiza a internação do paciente
  public static void realizarInternacao() throws IOException {
    HashMap<String, String> dadosInternacao = pegarInputInternacao();
    Tela.exibirMensagem(2, 5, "Custo:");
    String custo = Inputs.lerInput(9, 5, Verificador::numeroValido, "Custo de internação inválido.");

    if (tentarRealizarInternacaoPossivel(dadosInternacao.get("cpf"), dadosInternacao.get("nome"), dadosInternacao.get("data_entrada"), Integer.parseInt(dadosInternacao.get("quarto")), Double.parseDouble(custo))) {
      Tela.exibirMensagem(2, 7, ("Internação agendada!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível agendar a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  // conclui a internação do paciente
  public static void concluirInternacao() throws IOException {
    HashMap<String, String> dadosInternacao = pegarInputInternacao();

    if (tentarConcluirInternacaoPossivel(dadosInternacao.get("cpf"), dadosInternacao.get("nome"), dadosInternacao.get("data_entrada"), Integer.parseInt(dadosInternacao.get("quarto")))) {
      Tela.exibirMensagem(2, 7, ("Internação concluida!"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível concluir a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  // cancela a internação do paciente
  public static void cancelarInternacao() throws IOException {
    HashMap<String, String> dadosInternacao = pegarInputInternacao();

    if (tentarCancelarInternacaoPossivel(dadosInternacao.get("cpf"), dadosInternacao.get("nome"), dadosInternacao.get("data_entrada"), Integer.parseInt(dadosInternacao.get("quarto")))) {
      Tela.exibirMensagem(2, 7, ("Internação cancelada"));
    } else {
      Tela.exibirMensagem(2, 7, ("Não foi possível cancelar a internação."));
      Tela.exibirMensagem(2, 8, ("Verifique os dados do paciente/médico."));
    }
    Menu.pausa(2000);
  }

  // pede os dados relacionados a internação de um paciente
  public static HashMap<String, String> pegarInputInternacao() throws IOException{
    Tela.exibirMensagem(2, 1, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 1, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 2, "Nome do médico:");
    String nome = Inputs.lerInput(18, 2, Verificador::palavraValida, "Nome inválido: siga o padrão 'código/estado'.");

    Tela.exibirMensagem(2, 3, "Data de internação:");
    String dataDeEntrada = Inputs.lerInput(22, 3, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    Tela.exibirMensagem(2, 4, "Quarto:");
    String quarto = Inputs.lerInput(10, 4, Verificador::numeroValido, "Quarto inexistente.");
    HashMap<String, String> dados = new HashMap<>();
    dados.put("cpf", cpf.replace(".", "").replace("-", ""));
    dados.put("nome", nome.toUpperCase());
    dados.put("data_entrada", dataDeEntrada);
    dados.put("quarto", quarto);
    return dados;
  }

  // cria a internação e à salva, se possível
  public static boolean tentarRealizarInternacaoPossivel(String cpfPaciente, String nome, String dataDeEntrada, int quarto, double custo) {
    // pega as internações salvas
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    // verifica se o médico e paciente existem
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nome);
    if (medico == null || paciente == null) return false;

    // se já existe alguém internado naquele quarto, cancelar
    if (repoInternacao.buscar(p -> p.getQuarto() == quarto) != null) return false;

    // pega o custo e aplica os descontos de plano
    double custoInternacao = custo;
    if (paciente instanceof PacienteEspecial pacienteEspecial) {
      HashMap<String, Integer> descontos = PacienteEspecial.getDescontos();
      int descontoPlano = descontos.get(pacienteEspecial.getPlanoDeSaude());
      if (paciente.getIdade() >= 60) descontoPlano += 5;
      custoInternacao *= (1 - (descontoPlano / 100.0));
    }

    // salva a nova internação
    Internacao internacao = new Internacao(cpfPaciente, nome, dataDeEntrada, quarto, custo, Math.round(custoInternacao));
    paciente.novaInternacao(internacao);
    paciente.atualizarPorCpf(cpfPaciente);
    repoInternacao.adicionar(internacao);
    return true;
  }

  // tenta concluir a internação
  public static boolean tentarConcluirInternacaoPossivel(String cpfPaciente, String nomeMedico, String dataDeEntrada, int quarto) throws IOException {
    Tela.exibirMensagem(2, 5, "Data de alta:");
    String dataDeSaida = Inputs.lerInput(16, 5, Verificador::dataValida, "Data inválida: não usar datas passadas, dia/mês/ano");

    // pega as internações salvas
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    // verifica se o médico e paciente existem
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null || Verificador.dataAntesDaOutra(dataDeEntrada, dataDeSaida)) return false;

    // cria uma internação para a comparação
    Internacao dadosDaInternacao = new Internacao(cpfPaciente, nomeMedico, dataDeEntrada, quarto, 0, 0);
    List<Internacao> internacoes = repoInternacao.filtrar(p -> p.getCpf().equals(cpfPaciente));

    for (Internacao internacao : internacoes) {
      if (internacao.equals(dadosDaInternacao)) {
        for (Internacao internacaoPaciente : paciente.getInternacoes()) {
          if (internacaoPaciente.equals(internacao)) {
            internacaoPaciente.setDataDeSaida(dataDeSaida);
            // se o paciente tem plano
            if (paciente instanceof PacienteEspecial) {
              long diasInternado = Verificador.calcularDiferencaDeDias(internacaoPaciente.getDataDeEntrada(), dataDeSaida);
              // Zera os custos se o a internação durou menos de uma semana
              if (diasInternado < 7) {
                internacaoPaciente.setCustoTotal(0.0);
                internacaoPaciente.setCustoComPlano(0.0);
              }
            }
            paciente.atualizarPorCpf(cpfPaciente);
          }
        }
        // remove a internação da base de dados
        repoInternacao.remover(internacao);
        return true;
      }
    }
    return false;
  }

  // tenta cancelar uma internação
  public static boolean tentarCancelarInternacaoPossivel(String cpfPaciente, String nomeMedico, String dataDeEntrada, int quarto) throws IOException {
    // acessa as internações salvas
    RepositorioJson<Internacao> repoInternacao = new RepositorioJson(Internacao[].class, "dados_internacoes.json");

    // verifica se o paciente e médico
    Paciente paciente = Paciente.procurarCpfPaciente(cpfPaciente);
    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico == null || paciente == null) return false;

    // cria a internação para comparar
    Internacao dadosDaInternacao = new Internacao(cpfPaciente, nomeMedico, dataDeEntrada, quarto, 0, 0);
    List<Internacao> internacoes = repoInternacao.filtrar(p -> p.getCpf().equals(cpfPaciente));

    for (Internacao internacao : internacoes) {
      if (internacao.equals(dadosDaInternacao)) {
        for (Internacao internacaoPaciente : paciente.getInternacoes()) {
          if (internacaoPaciente.equals(internacao)) {
            // cancela a internação específica
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
 // getters dos dados de internação
  public int getQuarto() { return quarto; }
  public String getCpf() { return cpfPaciente; }
  public double getCustoTotal() { return custoTotal; }
  public double getCustoComPlano() { return custoComPlano; }
  public String getDataDeEntrada() { return dataDeEntrada;}
  public String getDataDeSaida() { return dataDeSaida; }

  // setters de dados alteráveis
  public void setDataDeSaida(String dataDeSaida) { this.dataDeSaida = dataDeSaida; }
  public void setCustoTotal(double custoTotal) { this.custoTotal = custoTotal; }
  public void setCustoComPlano(double custoComPlano) { this.custoComPlano = custoComPlano; }

  // sobrescritas para comparar internações por valor
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