package classes;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> opcoes = Arrays.asList("Cadastrar paciente", "Cadastrar médico", "Salvar", "Sair");
        Menu.criarMenu(opcoes);
    }
}
