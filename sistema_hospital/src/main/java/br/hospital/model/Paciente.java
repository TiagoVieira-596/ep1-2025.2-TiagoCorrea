package br.hospital.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;
import br.hospital.utils.Inputs;
import br.hospital.utils.RepositorioJson;
import br.hospital.utils.Verificador;

public class Paciente extends Pessoa{
  private ArrayList<Consulta> consultas;
  private ArrayList<Internacao> internacoes;

  public Paciente(String nome, String cpf, int idade) {
    super(nome, cpf, idade);
    this.consultas = new ArrayList<>();
    this.internacoes = new ArrayList<>();
  }
  public Paciente(String nome, String cpf, int idade, List<Consulta> consultas, List<Internacao> internacoes) {
    super(nome, cpf, idade);
    this.consultas = new ArrayList<>(consultas);
    this.internacoes = new ArrayList<>(internacoes);
  }

  public static void cadastroPaciente() throws IOException {
    Tela.exibirMensagem(2, 1, "Nome completo do paciente:");
    String nome = Inputs.lerInput(29, 1, Verificador::palavraValida, "Nome inválido: use apenas letras.");

    Tela.exibirMensagem(2, 2, "CPF do paciente:");
    String cpf = Inputs.lerInput(19, 2, Verificador::cpfValido, "CPF inválido.");

    Tela.exibirMensagem(2, 3, "Idade do paciente:");
    String idade = Inputs.lerInput(21, 3, Verificador::idadeValida, "Idade inválida: a idade deve ser um número positivo");

    Tela.exibirMensagem(2, 5, ("Paciente cadastrado!"));
    Tela.exibirMensagem(2, 6, ("Nome: " + nome + " CPF: " + cpf + " Idade: " + idade));
    Paciente novoPaciente = new Paciente(nome.toUpperCase(), cpf.replace(".", "").replace("-", ""), Integer.parseInt(idade));
    RepositorioJson<Paciente> repo = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    repo.adicionar(novoPaciente);
    Menu.pausa(1500);
  }

  public void novaConsulta(Consulta consulta) {
    if (consultas == null) {
        consultas = new ArrayList<>();
    }
    consultas.add(consulta);
  }
  public List<Consulta> getConsultas() {
    return new ArrayList<>(consultas);
  }

  public void novaInternacao(Internacao internacao) {
    if (internacoes == null) {
        internacoes = new ArrayList<>();
    }
    internacoes.add(internacao);
  }
  public List<Internacao> getInternacoes() {
    return new ArrayList<>(internacoes);
  }

  public static Paciente procurarCpfPaciente(String cpf) {
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");
    Paciente paciente = repoPacienteEspecial.buscar(p -> p.getCpf().equals(cpf));
    if (paciente == null) {
      paciente = repoPaciente.buscar(p -> p.getCpf().equals(cpf));
    }
    return paciente;
  }

  public void atualizarPorCpf(String cpf) {
    RepositorioJson<Paciente> repoPaciente = new RepositorioJson(Paciente[].class, "dados_pacientes.json");
    RepositorioJson<PacienteEspecial> repoPacienteEspecial = new RepositorioJson(PacienteEspecial[].class, "dados_pacientes_especiais.json");
    if (this instanceof PacienteEspecial pacienteEspecial) {
      repoPacienteEspecial.atualizar(p -> p.getCpf().equals(cpf), pacienteEspecial);
    } else {
      repoPaciente.atualizar(p -> p.getCpf().equals(cpf), this);
    } 
  }

  @Override
  public String toString() {
    return "Paciente: " + super.toString();
  }
}