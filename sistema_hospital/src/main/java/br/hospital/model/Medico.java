package br.hospital.model;
import java.io.IOException;
import java.util.HashMap;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Medico extends Pessoa{
  private String crm, especialidade;
  private double custo;

  private final HashMap<String, Boolean> agenda = new HashMap<>();

  {
    for (int i = 8; i < 23; i++) {
      agenda.put(String.format("%02d:00", i), true);
      agenda.put(String.format("%02d:30", i), true);
    }
  }

  public Medico(String nome, String cpf, int idade, String crm, String especialidade, double custo) {
    super(nome, cpf, idade);
    this.crm = crm;
    this.especialidade = especialidade;
    this.custo = custo;
  }

  public static void cadastroMedico() throws IOException {
    Tela.exibirMensagem(2, 1, "Nome completo do médico:");
    String nome = Inputs.lerInput(27, 1, Verificador::palavraValida, "Nome inválido: use apenas letras.");

    Tela.exibirMensagem(2, 2, "CPF do médico:");
    String cpf = Inputs.lerInput(17, 2, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 3, "Idade do médico:");
    String idade = Inputs.lerInput(19, 3, Verificador::idadeValida, "Idade inválida: use apenas números positivos.");

    Tela.exibirMensagem(2, 4, "CRM do médico:");
    String crm = Inputs.lerInput(17, 4, Verificador::crmValido, "CRM inválido.");

    Tela.exibirMensagem(2, 5, "Especialidade do médico:");
    String especialidade = Inputs.lerInput(27, 5, Verificador::especialidadeValida, "Especialidade inválida.");

    Tela.exibirMensagem(2, 6, "Preço da consulta:");
    String custo = Inputs.lerInput(21, 6, Verificador::numeroValido, "Preço da consulta inválido: use apenas números.");

    Tela.exibirMensagem(2, 8, ("Médico cadastrado cadastrado!"));
    Tela.exibirMensagem(2, 9, ("Nome: " + nome + " CRM: " + crm + " Especialidade: " + especialidade + " Custo:" + custo));
    Medico novoMedico = new Medico(nome.toUpperCase(), cpf.replace(".", "").replace("-", ""), Integer.parseInt(idade), crm, especialidade.toUpperCase(), Double.parseDouble(custo));
    RepositorioJson<Medico> repo = new RepositorioJson(Medico[].class, "dados_medicos.json");
    repo.adicionar(novoMedico);
    Menu.pausa(1500);
  }

  public void setCrm(String crm) {
    this.crm = crm;
  }
  public String getCrm() {
    return crm;
  }

  public void setEspecialidade(String especialidade) {
    this.especialidade = especialidade;
  }
  public String getEspecialidade() {
    return especialidade;
  }

  public void setCusto(double custo) {
    this.custo = custo;
  }
  public double getCusto() {
    return custo;
  }

  public static void mudarDisponibilidade() throws IOException {
    int linha = 1;
    Tela.exibirMensagem(1, linha, "Nome do médico:");
    String nomeMedico = Inputs.lerInput(17, linha, Verificador::palavraValida, "Nome inválido, use apenas letras.");

    Medico medico = Medico.procurarNomeMedico(nomeMedico);
    if (medico != null && medico.mudarDisponibilidadePossivel()) {
      Tela.exibirMensagem(1, linha++, "Disponibilidade Alterada");
    } else {
      Tela.exibirMensagem(1, linha++, "Escolha um médico e um horarío validos.");
    }
    Menu.pausa();
  }

  public boolean mudarDisponibilidadePossivel() throws IOException {
    int linha = 1;
    Tela.exibirMensagem(1, linha, "De qual horário você quer mudar a disponibilade?");
    String horario = Inputs.lerInput(50, linha, Verificador::horarioValido, "Formato de horario inválido");
    Tela.exibirMensagem(1, linha++, "Disponibilade?");
    String disponibilidade = Inputs.lerInput(16, linha, Verificador::palavraValida, "Digite Disponível ou Indisponível").equalsIgnoreCase("DISPONÍVEL") ? "DISPONÍVEL" : "INDISPONÍVEL";
    if (this.getAgenda().containsKey(horario)) {
    this.getAgenda().put(horario, disponibilidade.equalsIgnoreCase("DISPONÍVEL"));
    } else {
      return false;
    }
    return true;
  }
  public HashMap<String, Boolean> getAgenda() {
    return agenda;
  }

  public static Medico procurarNomeMedico(String nomeMedico) {
    RepositorioJson<Medico> repoMedico = new RepositorioJson(Medico[].class, "dados_medicos.json");
    return repoMedico.buscar(p -> p.getNome().equals(nomeMedico));
  }
  public static Medico procurarCrmMedico(String crmMedico) {
    RepositorioJson<Medico> repoMedico = new RepositorioJson(Medico[].class, "dados_medicos.json");
    return repoMedico.buscar(p -> p.getCrm().equals(crmMedico));
  }

  @Override
  public String toString() {
    return "Médico: " + super.toString() + " CRM: " + crm + " Custo de consulta: " + custo;
  }
}