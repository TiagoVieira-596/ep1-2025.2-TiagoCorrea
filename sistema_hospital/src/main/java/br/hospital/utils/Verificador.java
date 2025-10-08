package br.hospital.utils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class Verificador {
  private static final Set<String> ESTADOS_VALIDOS = Set.of(
    "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
    "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
    "RS", "RO", "RR", "SC", "SP", "SE", "TO"
  );
  private static final Set<String> ESPECIALIDADES_VALIDAS = Set.of(
    "CLÍNICO GERAL", "PEDIATRIA", "CARDIOLOGIA", "DERMATOLOGIA", "GINECOLOGIA",
    "ORTOPEDIA", "NEUROLOGIA", "PSIQUIATRIA", "ENDOCRINOLOGIA", "OFTALMOLOGIA",
    "UROLOGIA", "OTORRINOLARINGOLOGIA", "REUMATOLOGIA", "GASTROENTEROLOGIA", "ONCOLOGIA",
    "INFECTOLOGIA", "NEFROLOGIA", "HEMATOLOGIA", "ANESTESIOLOGIA", "RADIOLOGIA"
  );
  private static final Set<String> PLANOS_VALIDOS = Set.of(
    "CASSI", "UNIMED", "NOTREDAME INTERMÉDICA", "AMIL", "SULAMÉRICA", "BRADESCO SAÚDE", "OUTRO"
  );

  // verifica se o cpf é válido
  public static boolean cpfValido(String cpf) {
    String numerosCpf = cpf.replace(".", "").replace("-", "");
    if (!numerosCpf.matches("\\d{11}") || numerosCpf.matches("(\\d)\\1{10}")) {
      return false;
    } else {
      for (int i = 0; i < 2; i++) {
        int soma = 0;
        int peso = 10 + i;
        for (int j = 0; j < 9 + i; j++) {
          soma += Character.getNumericValue(numerosCpf.charAt(j)) * (peso - j);
        }
        int resto = (10 * soma) % 11;
        if (resto == 10) resto = 0;
        if (resto != Character.getNumericValue(numerosCpf.charAt(9 + i))) {
          return false;
        }
        numerosCpf += resto;
      }
    }
    return true;
  }

  // lista os planos válidos
  public static Set<String> getPLANOS_VALIDOS() { return PLANOS_VALIDOS;}

  // verifica se um número é válido
  public static boolean numeroValido(String idade) { return idade.matches("^\\d+$"); }

  // permite idades positivas de no máximo 3 digitos
  public static boolean idadeValida(String idade) { return idade.matches("^\\d{1,3}$"); }

  // permite palavras sem números ou caractéres especiais
  public static boolean palavraValida(String resposta) { return resposta.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$"); }

  // verifica se o crm segue o padrão
  public static boolean crmValido(String crm) {

    if (!crm.matches("^\\d{4,7}/[A-Z]{2}$")) return false;

    String uf = crm.substring(crm.length() - 2);
    return ESTADOS_VALIDOS.contains(uf);
  }

  // verifica se a especialidade é válida
  public static boolean especialidadeValida(String especialidade) {
    if (!palavraValida(especialidade)) {
      return false;
    }
    return ESPECIALIDADES_VALIDAS.contains(especialidade.toUpperCase());
  }

  // testa se a data segue o padrão de data e não é passada
  public static boolean dataValida(String data) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      LocalDate dataInput = LocalDate.parse(data, formatador);
      return !dataInput.isBefore(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  // verifica se o input segue o padrão de horário
  public static boolean horarioValido(String horario) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("HH:mm");
    try {
      LocalTime.parse(horario.trim(), formatador);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  // verifica se o input é um dos estados válidos
  public static boolean localValido(String local) {
    return ESTADOS_VALIDOS.contains(local.toUpperCase());
  }

  // verifica se um plano está na lista válida
  public static boolean planoDeSaudeValido(String plano) {
    return PLANOS_VALIDOS.contains(plano.toUpperCase());
  }
  
  // vê se uma data vem antes de outra
  public static boolean dataAntesDaOutra(String data1, String data2) {
    DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      LocalDate dataInput1 = LocalDate.parse(data1, formatador);
      LocalDate dataInput2 = LocalDate.parse(data2, formatador);
      return dataInput1.isBefore(dataInput2);
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  // verfica a diferença entre datas
  public static long calcularDiferencaDeDias(String dataInicio, String dataFim) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate inicio = LocalDate.parse(dataInicio, formatter);
    LocalDate fim = LocalDate.parse(dataFim, formatter);
    return ChronoUnit.DAYS.between(inicio, fim);
  }
}