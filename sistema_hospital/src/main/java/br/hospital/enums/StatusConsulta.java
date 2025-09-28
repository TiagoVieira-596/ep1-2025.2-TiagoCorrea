package br.hospital.enums;

public enum StatusConsulta {
  AGENDADA("Agendada"),
  REALIZADA("Realizada"),
  CANCELADA("Cancelada");

  private final String status;

  StatusConsulta(String status) {
      this.status = status;
  }
  public String getStatus() {
      return status;
  }
}
