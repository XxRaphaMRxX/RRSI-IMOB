package com.rrsi.ui;

import com.rrsi.firebase.FirebaseManager;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de Gerenciamento de Usuários - Apenas para Administradores
 */
public class UserManagementFrame extends JFrame {
    private MainFrame parentFrame;
    private FirebaseManager firebaseManager;
    
    // Componentes do formulário
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField displayNameField;
    private JComboBox<String> roleComboBox;
    private JButton createUserButton;
    private JButton cancelButton;
    private JTextArea logArea;
    
    public UserManagementFrame(MainFrame parent) {
        this.parentFrame = parent;
        this.firebaseManager = FirebaseManager.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        setTitle("RRSI Imobiliária - Gerenciamento de Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        // Campos do formulário
        emailField = new JTextField(25);
        passwordField = new JPasswordField(25);
        displayNameField = new JTextField(25);
        
        // ComboBox para tipos de usuário
        String[] roles = {"Corretor", "Administrador", "Gerente"};
        roleComboBox = new JComboBox<>(roles);
        
        // Botões
        createUserButton = new JButton("Criar Usuário");
        cancelButton = new JButton("Fechar");
        
        // Área de log
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setBackground(new Color(248, 248, 248));
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        // Estilização dos botões
        createUserButton.setBackground(new Color(70, 130, 180));
        createUserButton.setForeground(Color.WHITE);
        createUserButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        cancelButton.setBackground(new Color(180, 70, 70));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setLayout(new BorderLayout());
        
        // Painel do cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(600, 60));
        
        JLabel titleLabel = new JLabel("GERENCIAMENTO DE USUÁRIOS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        // Painel do formulário
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Criar Novo Usuário", 
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        
        // Nome de exibição
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Nome Completo:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(displayNameField, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Cargo/Função
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        JLabel roleLabel = new JLabel("Cargo:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleComboBox, gbc);
        
        // Painel dos botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(createUserButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Painel de log
        JPanel logPanel = new JPanel();
        logPanel.setBackground(new Color(240, 248, 255));
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Log de Atividades", 
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Adicionando componentes ao painel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Mensagem inicial no log
        appendLog("Sistema de gerenciamento de usuários iniciado.");
        appendLog("Preencha os campos e clique em 'Criar Usuário'.");
    }
    
    private void setupEventListeners() {
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewUser();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void createNewUser() {
        // Validar campos
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String displayName = displayNameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        if (email.isEmpty()) {
            showError("Por favor, digite o email.");
            emailField.requestFocus();
            return;
        }
        
        if (!isValidEmail(email)) {
            showError("Por favor, digite um email válido.");
            emailField.requestFocus();
            return;
        }
        
        if (displayName.isEmpty()) {
            showError("Por favor, digite o nome completo.");
            displayNameField.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            showError("A senha deve ter pelo menos 6 caracteres.");
            passwordField.requestFocus();
            return;
        }
        
        if (!firebaseManager.isInitialized()) {
            showError("Erro de conexão com o Firebase. Tente novamente.");
            return;
        }
        
        // Verificar se o usuário já existe
        if (firebaseManager.userExists(email)) {
            showError("Já existe um usuário com este email.");
            appendLog("ERRO: Tentativa de criar usuário duplicado: " + email);
            return;
        }
        
        // Desabilitar botão durante criação
        createUserButton.setEnabled(false);
        createUserButton.setText("Criando...");
        
        appendLog("Iniciando criação de usuário: " + email);
        
        // Executar criação em thread separada
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String errorMessage = "";
            private UserRecord newUser = null;
            
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    // Criar usuário no Firebase
                    newUser = firebaseManager.createUser(email, password, displayName);
                    
                    // TODO: Em um sistema real, você salvaria informações adicionais
                    // como o cargo no Firestore ou como custom claims
                    
                    return true;
                } catch (FirebaseAuthException e) {
                    errorMessage = "Erro do Firebase: " + e.getMessage();
                    return false;
                } catch (Exception e) {
                    errorMessage = "Erro inesperado: " + e.getMessage();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                // Restaurar botão
                createUserButton.setEnabled(true);
                createUserButton.setText("Criar Usuário");
                
                try {
                    Boolean success = get();
                    if (success && newUser != null) {
                        // Sucesso
                        String successMsg = "Usuário criado com sucesso!\n" +
                                          "UID: " + newUser.getUid() + "\n" +
                                          "Email: " + newUser.getEmail() + "\n" +
                                          "Nome: " + newUser.getDisplayName() + "\n" +
                                          "Cargo: " + role;
                        
                        showSuccess(successMsg);
                        appendLog("SUCESSO: Usuário criado - UID: " + newUser.getUid());
                        appendLog("  Email: " + newUser.getEmail());
                        appendLog("  Nome: " + newUser.getDisplayName());
                        appendLog("  Cargo: " + role);
                        
                        // Limpar formulário
                        clearForm();
                        
                    } else {
                        // Erro
                        showError(errorMessage);
                        appendLog("ERRO: " + errorMessage);
                    }
                } catch (Exception e) {
                    showError("Erro inesperado: " + e.getMessage());
                    appendLog("ERRO CRÍTICO: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
    }
    
    private void clearForm() {
        emailField.setText("");
        passwordField.setText("");
        displayNameField.setText("");
        roleComboBox.setSelectedIndex(0);
        emailField.requestFocus();
    }
    
    private void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
            logArea.append("[" + timestamp + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}