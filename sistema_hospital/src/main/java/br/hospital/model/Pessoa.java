package br.hospital.model;

public class Pessoa {
  private String nome, cpf;
  private int idade;

  public Pessoa(String nome, String cpf, int idade) {
    this.nome = nome;
    this.cpf = cpf;
    this.idade = idade;
  }

  // getters e setters da classe Pessoa
  public void setNome(String nome) { this.nome = nome; }
  public String getNome() { return nome; }

  public void setCpf(String cpf) { this.cpf = cpf; }
  public String getCpf() { return cpf; }

  public void setIdade(int idade) { this.idade = idade; }
  public int getIdade() { return idade; }

  @Override
  public String toString() {
    return "Nome: " + nome + " CPF: " + cpf + " Idade: " + idade;
  }
}
