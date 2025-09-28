package br.hospital.model;
import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import br.hospital.menu.Menu;
import br.hospital.utils.Inputs;

public class Internacao {
  private final Paciente paciente;
  private final Medico medicoResponsavel;
  private final String dataDeEntrada;
  private final String dataDeSaida; 
  private final String quarto;
  private final double custo;

  public Internacao(Paciente paciente, Medico medicoResponsavel, String dataDeEntrada, String dataDeSaida, String quarto, double custo) {
    this.paciente = paciente;
    this.medicoResponsavel = medicoResponsavel;
    this.dataDeEntrada = dataDeEntrada;
    this.dataDeSaida = dataDeSaida;
    this.quarto = quarto;
    this.custo = custo;
  }

  public static String[] realizarInternacao(TextGraphics tg, Screen tela) throws IOException {
    tg.putString(2, 1, "CPF do paciente:");
    tela.refresh();
    String cpf = Inputs.lerInput(tela, 19, 1);

    tg.putString(2, 2, "CRM do médico:");
    tela.refresh();
    String crm = Inputs.lerInput(tela, 17, 2);

    tg.putString(2, 3, "Data de internação:");
    tela.refresh();
    String dataDeEntrada = Inputs.lerInput(tela, 22, 3);

    tg.putString(2, 4, "Quarto:");
    tela.refresh();
    String quarto = Inputs.lerInput(tela, 10, 4);

    tg.putString(2, 5, "Custo:");
    tela.refresh();
    String custo = Inputs.lerInput(tela, 9, 5);

    tg.putString(2, 7, ("Internação realizada!"));
    tela.refresh();
    String[] dados = {cpf, crm, dataDeEntrada, quarto, custo};
    Menu.pausa(1000);
    return dados;
  }
}