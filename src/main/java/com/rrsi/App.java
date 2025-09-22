package com.rrsi;

import com.rrsi.ui.LoginFrame;
import javax.swing.*;

/**
 * Sistema Imobiliário RRSI
 * Classe principal de inicialização
 */
public class App {
    public static void main(String[] args) {
        // Iniciar aplicação com tela de login
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
