package br.hospital.model;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.menu.Menu;
import br.hospital.utils.Inputs;
import br.hospital.utils.Verificador;

public class Paciente extends Pessoa{
  private ArrayList<Consulta> consultas;
  private ArrayList<String> internacoes;

  public Paciente(String nome, String cpf, int idade) {
    super(nome, cpf, idade);
    this.consultas = new ArrayList<>();
    this.internacoes = new ArrayList<>();
  }

  public static String[] cadastroPaciente(TextGraphics tg, Screen tela) throws IOException {
    tg.putString(2, 1, "Nome do paciente:");
    tela.refresh();
    String nome = Inputs.lerInput(tela, 20, 1, Verificador::palavraValida, "Nome inválido: use apenas letras.");

    tg.putString(2, 2, "CPF do paciente:");
    tela.refresh();
    String cpf = Inputs.lerInput(tela, 19, 2, Verificador::numeroValido, "Nome inválido: use apenas letras.");

    tg.putString(2, 3, "Idade do paciente:");
    tela.refresh();
    String idade = Inputs.lerInput(tela, 21, 3);

    tg.putString(2, 5, ("Paciente cadastrado!"));
    tela.refresh();
    tg.putString(2, 6, ("Nome: " + nome + " CPF: " + cpf + " Idade: " + idade));
    tela.refresh();
    String[] dados = {nome, cpf, idade};
    Menu.pausa(1000);
    return dados;
  }

  public void setConconsultas(ArrayList<Consulta> consultas) {
    this.consultas = consultas;
  }
  public ArrayList<Consulta> getConconsultas() {
    return consultas;
  }

  public void setInternacoes(ArrayList<String> internacoes) {
    this.internacoes = internacoes;
  }
  public ArrayList<String> getInternacoes() {
    return internacoes;
  }

  @Override
  public String toString() {
    return "Paciente: " + super.toString();
  }

}