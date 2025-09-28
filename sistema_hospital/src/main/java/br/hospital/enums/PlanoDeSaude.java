package br.hospital.enums;

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
  public double getId() {
      return id;
  }
  public static PlanoDeSaude getPorId(int id) {
      for (PlanoDeSaude cdd : PlanoDeSaude.values()) {
          if (cdd.getId() == id) {
              return cdd;
          }
      }
      throw new IllegalArgumentException("Código de plano inválido: " + id);
  }
}
