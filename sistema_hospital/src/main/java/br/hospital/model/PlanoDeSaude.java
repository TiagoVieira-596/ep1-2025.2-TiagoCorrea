package br.hospital.model;

public class PlanoDeSaude {
  private final String nome_do_plano;
  private final double desconto;

  public PlanoDeSaude(String nome, double desconto) {
    this.nome_do_plano = nome;
    this.desconto = desconto;
  }

  public String getNomeDoPlano() {
    return nome_do_plano;
  }
  public double getDesconto() {
    return desconto;
  }

  @Override
  public String toString() {
    return " Plano de Saúde: " + nome_do_plano + " Desconto: " + desconto;
  }
}
