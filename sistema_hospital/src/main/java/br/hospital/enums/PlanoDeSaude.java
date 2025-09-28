package br.hospital.enums;
import java.util.ArrayList;
import java.util.List;

public enum PlanoDeSaude {
  UNIMED("Unimed", 1, 0.15),
  AMIL("Amil", 2, 0.10),
  BRADESCO_SAUDE("Bradesco Saúde", 3, 0.12),
  SULAMERICA("SulAmérica", 4, 0.25),
  HAPVIDA("Hapvida", 5, 0.30),
  CASSI("Cassi", 6, 0.35),
  NOTREDAME("NotreDame Intermédica", 7, 0.05),
  OUTROS("Outros", 8, 0.0);

  private final String nome;
  private final int id;
  private final double desconto;

  PlanoDeSaude(String nome, int id, double desconto) {
    this.nome = nome;
    this.id = id;
    this.desconto = desconto;
  }

  public String getNome() {
    return nome;
  }
  public double getDesconto() {
    return desconto;
  }
  public int getId() {
      return id;
  }
  public static List<String> listarPlanos() {
    List<String> listaDePlanos = new ArrayList<>();
    for (PlanoDeSaude plano : PlanoDeSaude.values()) {
        listaDePlanos.add(plano.getNome());
    }
    return listaDePlanos;
  }
  @Override
    public String toString() {
        return "Nome: " + name().charAt(0) + name().substring(1).toLowerCase() + " Id: " + id + " Desconto: " + desconto;
  }
}
