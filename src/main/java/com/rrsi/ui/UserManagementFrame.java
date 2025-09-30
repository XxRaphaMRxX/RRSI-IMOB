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
        gbc.insets = new Insets(8, 8, 8, 8);

        // Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);
        
        // Nome de exibição
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JLabel displayNameLabel = new JLabel("Nome:");
        displayNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(displayNameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(displayNameField, gbc);

        // Tipo de usuário
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JLabel roleLabel = new JLabel("Tipo:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(roleComboBox, gbc);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(createUserButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Painel de log
        JPanel logPanel = new JPanel();
        logPanel.setBackground(new Color(240, 248, 255));
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Log de Operações",
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Adicionar componentes ao painel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
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

        // Permitir criar usuário com Enter
        displayNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewUser();
            }
        });
    }
    
    private void createNewUser() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String displayName = displayNameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        // Validações
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
        
        if (password.length() < 6) {
            showError("A senha deve ter pelo menos 6 caracteres.");
            passwordField.requestFocus();
            return;
        }

        if (displayName.isEmpty()) {
            showError("Por favor, digite o nome de exibição.");
            displayNameField.requestFocus();
            return;
        }

        if (!firebaseManager.isInitialized()) {
            showError("Firebase não está inicializado. Verifique a conexão.");
            return;
        }
        
        // Verificar se o usuário já existe
        if (firebaseManager.userExists(email)) {
            showError("Usuário com este email já existe.");
            emailField.requestFocus();
            return;
        }
        
        // Desabilitar botão durante criação
        createUserButton.setEnabled(false);
        createUserButton.setText("Criando...");
        
        // Executar criação em thread separada
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            private String errorMessage = "";
            private UserRecord createdUser = null;

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    createdUser = firebaseManager.createUser(email, password, displayName);
                    return true;
                } catch (FirebaseAuthException e) {
                    errorMessage = "Erro ao criar usuário: " + e.getMessage();
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
                    if (success) {
                        // Sucesso
                        logMessage("✓ Usuário criado com sucesso:");
                        logMessage("  Email: " + createdUser.getEmail());
                        logMessage("  Nome: " + createdUser.getDisplayName());
                        logMessage("  Tipo: " + role);
                        logMessage("  UID: " + createdUser.getUid());
                        logMessage("  Data/Hora: " + new java.util.Date());
                        logMessage("─────────────────────────────────────");

                        showSuccess("Usuário criado com sucesso!");
                        clearForm();
                    } else {
                        // Erro
                        logMessage("✗ Erro ao criar usuário:");
                        logMessage("  " + errorMessage);
                        logMessage("  Data/Hora: " + new java.util.Date());
                        logMessage("─────────────────────────────────────");

                        showError(errorMessage);
                    }
                } catch (Exception e) {
                    showError("Erro inesperado: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    private void clearForm() {
        emailField.setText("");
        passwordField.setText("");
        displayNameField.setText("");
        roleComboBox.setSelectedIndex(0);
        emailField.requestFocus();
    }
    
    private void logMessage(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}