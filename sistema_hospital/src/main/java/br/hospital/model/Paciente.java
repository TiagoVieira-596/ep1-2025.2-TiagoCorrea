package br.hospital.model;
import java.io.IOException;
import java.util.ArrayList;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.Serializador;
import br.hospital.utils.Verificador;

public class Paciente extends Pessoa{
  private ArrayList<Consulta> consultas;
  private ArrayList<String> internacoes;

  public Paciente(String nome, String cpf, int idade) {
    super(nome, cpf, idade);
    this.consultas = new ArrayList<>();
    this.internacoes = new ArrayList<>();
  }

  public static void cadastroPaciente() throws IOException {
    Tela.exibirMensagem(2, 1, "Nome do paciente:");
    String nome = Inputs.lerInput(20, 1, Verificador::palavraValida, "Nome inválido: use apenas letras.");

    Tela.exibirMensagem(2, 2, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 2, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 3, "Idade do paciente:");
    String idade = Inputs.lerInput(21, 3, Verificador::idadeValida, "Idade inválida: a idade deve ser um número positivo");

    Tela.exibirMensagem(2, 5, ("Paciente cadastrado!"));
    Tela.exibirMensagem(2, 6, ("Nome: " + nome + " CPF: " + cpf + " Idade: " + idade));
    Paciente novoPaciente = new Paciente(nome, cpf, Integer.parseInt(idade));
    Serializador.serializar(novoPaciente, "dados_pacientes.json", Paciente[].class);
    Menu.pausa();
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