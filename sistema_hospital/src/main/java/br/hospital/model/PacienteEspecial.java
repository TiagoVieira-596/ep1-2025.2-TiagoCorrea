package br.hospital.model;

public class PacienteEspecial extends Paciente {
  private PlanoDeSaude plano_de_saude;
  public PacienteEspecial(String nome, String cpf, int idade, PlanoDeSaude plano_de_saude) {
    super(nome, cpf, idade);
    this.plano_de_saude = plano_de_saude;
  }

  private PlanoDeSaude getPlanoDeSaude() {
    return plano_de_saude;
  }

  @Override
  public String toString() {
    return "Paciente Especial: " + super.toString() + " Plano de Saúde: " + plano_de_saude;
  }
}