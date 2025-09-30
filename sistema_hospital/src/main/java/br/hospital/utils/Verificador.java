package br.hospital.utils;

public class Verificador {
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
  public static boolean numeroValido(String idade) {
    return idade.matches("^\\d+$");
  }
  public static boolean idadeValida(String idade) {
    return idade.matches("^\\d{1,3}$");
  }
  public static boolean palavraValida(String resposta) {
    return resposta.matches("^\\p{L}+$");
  }
}