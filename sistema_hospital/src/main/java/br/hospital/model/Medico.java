package br.hospital.model;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.menu.Menu;
import br.hospital.utils.Inputs;
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

  public static String[] cadastroMedico(TextGraphics tg, Screen tela) throws IOException {
    tg.putString(2, 1, "Nome do médico:");
    tela.refresh();
    String nome = Inputs.lerInput(tela, 18, 1, Verificador::palavraValida, "Nome inválido: use apenas letras.");

    tg.putString(2, 2, "CRM do médico:");
    tela.refresh();
    String crm = Inputs.lerInput(tela, 17, 2, Verificador::crmValido, "CRM inválido.");

    tg.putString(2, 3, "Especialidade do médico:");
    tela.refresh();
    String especialidade = Inputs.lerInput(tela, 27, 3, Verificador::especialidadeValida, "Especialidade inválida.");

    tg.putString(2, 4, "Preço da consulta:");
    tela.refresh();
    String custo = Inputs.lerInput(tela, 21, 4, Verificador::numeroValido, "Preço da consulta inválido: use apenas números.");

    tg.putString(2, 6, ("Médico cadastrado cadastrado!"));
    tela.refresh();
    tg.putString(2, 7, ("Nome: " + nome + " CRM: " + crm + " Especialidade: " + especialidade + " Custo:" + custo));
    tela.refresh();
    String[] dados = {nome, crm, especialidade, custo};
    Menu.pausa();
    return dados;
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

  @Override
  public String toString() {
    return "Médico: " + super.toString() + " CRM: " + crm + " Custo de consulta: " + custo;
  }
}