package com.rrsi.ui;

import com.rrsi.firebase.FirebaseManager;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de Login do Sistema RRSI Imobiliária
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private FirebaseManager firebaseManager;
    
    public LoginFrame() {
        firebaseManager = FirebaseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        setTitle("RRSI Imobiliária - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Campos de entrada
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        // Botões
        loginButton = new JButton("Entrar");
        cancelButton = new JButton("Cancelar");
        
        // Estilização dos botões
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        cancelButton.setBackground(new Color(180, 70, 70));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal com fundo
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setLayout(new BorderLayout());
        
        // Painel do cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(400, 80));
        
        JLabel titleLabel = new JLabel("RRSI IMOBILIÁRIA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        // Painel central com formulário
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Label e campo de email
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        
        // Label e campo de senha
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Painel dos botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(buttonPanel, gbc);
        
        // Adicionar painéis ao frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Permitir login com Enter
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }
    
    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validação básica
        if (email.isEmpty()) {
            showError("Por favor, digite o email.");
            emailField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Por favor, digite a senha.");
            passwordField.requestFocus();
            return;
        }
        
        if (!firebaseManager.isInitialized()) {
            showError("Erro de conexão com o Firebase. Tente novamente.");
            return;
        }
        
        // Mostrar indicador de carregamento
        loginButton.setEnabled(false);
        loginButton.setText("Conectando...");
        
        // Executar autenticação em thread separada
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String errorMessage = "";
            private UserRecord userRecord = null;
            
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Verificar se o usuário existe no Firebase
                    userRecord = firebaseManager.getUserByEmail(email);
                    
                    // Para simplificar, vamos considerar que se o usuário existe no Firebase,
                    // a autenticação é válida. Em um cenário real, você usaria Firebase Auth
                    // com tokens personalizados ou outro método de validação de senha.
                    
                    return true;
                } catch (FirebaseAuthException e) {
                    if (e.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                        errorMessage = "Usuário não encontrado.";
                    } else {
                        errorMessage = "Erro de autenticação: " + e.getMessage();
                    }
                    return false;
                } catch (Exception e) {
                    errorMessage = "Erro de conexão: " + e.getMessage();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                // Restaurar botão
                loginButton.setEnabled(true);
                loginButton.setText("Entrar");
                
                try {
                    Boolean success = get();
                    if (success) {
                        // Login bem-sucedido
                        showSuccess("Login realizado com sucesso!");
                        
                        // Determinar o tipo de usuário (admin ou comum)
                        String userType = determineUserType(userRecord);
                        
                        // Fechar tela de login e abrir tela principal
                        dispose();
                        openMainScreen(userRecord, userType);
                    } else {
                        // Login falhou
                        showError(errorMessage);
                        passwordField.setText("");
                        emailField.requestFocus();
                    }
                } catch (Exception e) {
                    showError("Erro inesperado: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private String determineUserType(UserRecord userRecord) {
        // Por enquanto, vamos considerar que emails com "admin" são administradores
        // Em um sistema real, isso seria determinado por claims customizados ou 
        // informações armazenadas no Firestore
        String email = userRecord.getEmail();
        if (email != null && email.toLowerCase().contains("admin")) {
            return "admin";
        }
        return "user";
    }
    
    private void openMainScreen(UserRecord userRecord, String userType) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(userRecord, userType);
            mainFrame.setVisible(true);
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro de Login", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}