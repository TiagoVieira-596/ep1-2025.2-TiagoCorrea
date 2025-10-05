package br.hospital.app;
import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import br.hospital.menu.Menu;
import br.hospital.menu.Tela;

public class Main {
    public static void main(String[] args) throws IOException {
        Screen tela = new DefaultTerminalFactory().createScreen();
        TextGraphics tg = tela.newTextGraphics();
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Tela.configurar(tela, tg, terminal);
        Menu.criarMenu(List.of("Paciente", "Médico", "Sair"), "Olá! Que serviços quer utilizar?");
    }
}
