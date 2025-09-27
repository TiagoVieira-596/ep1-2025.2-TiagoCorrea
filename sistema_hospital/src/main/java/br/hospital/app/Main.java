package br.hospital.app;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import br.hospital.menu.Menu;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> opcoes = Arrays.asList("Paciente", "Médico", "Sair");
        Menu.criarMenu(opcoes, "Que serviços utilizar?");
    }
}
