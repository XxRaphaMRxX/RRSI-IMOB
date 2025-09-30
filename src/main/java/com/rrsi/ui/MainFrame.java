package com.rrsi.ui;

import com.google.firebase.auth.UserRecord;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela Principal do Sistema RRSI Imobiliária
 * Mostra diferentes opções baseadas no tipo de usuário
 */
public class MainFrame extends JFrame {
    private UserRecord currentUser;
    private String userType;
    private JLabel welcomeLabel;
    private JPanel menuPanel;
    
    public MainFrame(UserRecord user, String userType) {
        this.currentUser = user;
        this.userType = userType;
        
        initializeComponents();
        setupLayout();
        setupMenuOptions();
    }
    
    private void initializeComponents() {
        setTitle("RRSI Imobiliária - Sistema de Gestão");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Label de boas-vindas
        String userName = currentUser.getDisplayName() != null ? 
                         currentUser.getDisplayName() : 
                         currentUser.getEmail().split("@")[0];
        
        welcomeLabel = new JLabel("Bem-vindo, " + userName + 
                                 (userType.equals("admin") ? " (Administrador)" : ""));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Painel do cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(900, 100));
        headerPanel.setLayout(new BorderLayout());
        
        // Título principal
        JLabel titleLabel = new JLabel("RRSI IMOBILIÁRIA - SISTEMA DE GESTÃO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Painel de informações do usuário
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setBackground(new Color(70, 130, 180));
        userInfoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton logoutButton = new JButton("Sair");
        logoutButton.setBackground(new Color(180, 70, 70));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.addActionListener(e -> logout());
        
        userInfoPanel.add(welcomeLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userInfoPanel, BorderLayout.SOUTH);
        
        // Painel do menu principal
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(240, 248, 255));
        menuPanel.setLayout(new GridBagLayout());
        
        // Painel do rodapé
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(70, 130, 180));
        footerPanel.setPreferredSize(new Dimension(900, 50));
        
        JLabel footerLabel = new JLabel("© 2025 RRSI Imobiliária - Todos os direitos reservados");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel);
        
        // Adicionar componentes ao painel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupMenuOptions() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Opções comuns para todos os usuários
        JButton clientesButton = createMenuButton("Gerenciar Clientes", "Cadastrar e gerenciar informações dos clientes");
        JButton imoveisButton = createMenuButton("Gerenciar Imóveis", "Cadastrar e gerenciar imóveis disponíveis");
        JButton relatoriosButton = createMenuButton("Relatórios", "Visualizar relatórios de vendas e comissões");

        gbc.gridx = 0; gbc.gridy = 0;
        menuPanel.add(clientesButton, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        menuPanel.add(imoveisButton, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        menuPanel.add(relatoriosButton, gbc);

        // Opções específicas para administradores
        if (userType.equals("admin")) {
            JButton usuariosButton = createMenuButton("Gerenciar Usuários", "Cadastrar e gerenciar usuários do sistema");
            JButton configButton = createMenuButton("Configurações", "Configurações gerais do sistema");

            gbc.gridx = 0; gbc.gridy = 1;
            menuPanel.add(usuariosButton, gbc);

            gbc.gridx = 1; gbc.gridy = 1;
            menuPanel.add(configButton, gbc);

            // Event listener para gerenciar usuários
            usuariosButton.addActionListener(e -> openUserManagement());
        }
        
        // Event listeners para botões comuns
        clientesButton.addActionListener(e -> showNotImplemented("Gerenciamento de Clientes"));
        imoveisButton.addActionListener(e -> showNotImplemented("Gerenciamento de Imóveis"));
        relatoriosButton.addActionListener(e -> showNotImplemented("Relatórios"));
    }
    
    private JButton createMenuButton(String title, String description) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 248, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private void openUserManagement() {
        UserManagementFrame userManagementFrame = new UserManagementFrame(this);
        userManagementFrame.setVisible(true);
    }
    
    private void showNotImplemented(String feature) {
        JOptionPane.showMessageDialog(this,
            "A funcionalidade '" + feature + "' ainda não foi implementada.\n" +
            "Esta funcionalidade será adicionada em futuras versões.",
            "Funcionalidade em Desenvolvimento",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja sair do sistema?",
            "Confirmar Logout",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
}

