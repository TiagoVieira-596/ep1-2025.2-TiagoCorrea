package br.hospital.model;

import br.hospital.enums.PlanoDeSaude;

public class PacienteEspecial extends Paciente {
  private PlanoDeSaude planoDeSaude;
  public PacienteEspecial(String nome, String cpf, int idade, PlanoDeSaude planoDeSaude) {
    super(nome, cpf, idade);
    this.planoDeSaude = planoDeSaude;
  }

  private PlanoDeSaude getPlanoDeSaude() {
    return planoDeSaude;
  }

  @Override
  public String toString() {
    return "Paciente Especial: " + super.toString() + " Plano de Saúde: " + planoDeSaude;
  }
}