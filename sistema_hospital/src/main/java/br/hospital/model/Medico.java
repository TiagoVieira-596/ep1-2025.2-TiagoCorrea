package br.hospital.model;
import java.io.IOException;
import java.util.ArrayList;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Medico extends Pessoa{
  private String crm, especialidade;
  private double custo;
  private ArrayList<String> agenda;

  public Medico(String nome, String cpf, int idade, String crm, double custo) {
    super(nome, cpf, idade);
    this.crm = crm;
    this.custo = custo;
    this.agenda = new ArrayList<>();
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
    Medico novoMedico = new Medico(nome.toUpperCase(), cpf, Integer.parseInt(idade), crm, Double.parseDouble(custo));
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

  public void setAgenda(ArrayList<String> agenda) {
    this.agenda = agenda;
  }
  public ArrayList<String> getAgenda() {
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