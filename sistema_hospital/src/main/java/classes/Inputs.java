package classes;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;

public class Inputs {
  public static String lerInput(Screen screen, int col, int row) throws IOException {
    StringBuilder input = new StringBuilder();
    KeyStroke tecla;

    while (true) {
        tecla = screen.readInput();

        if (tecla.getKeyType() == KeyType.Enter) {
            break;
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            screen.newTextGraphics().putString(col + input.length(), row, " ");
        } else if (tecla.getKeyType() == KeyType.Backspace && input.length() <= 0) {

        } else if (tecla.getCharacter() != null) {
            input.append(tecla.getCharacter());
        }

        screen.newTextGraphics().putString(col, row, input.toString() + " ");
        screen.refresh();
    }

    return input.toString().trim();
  }
}