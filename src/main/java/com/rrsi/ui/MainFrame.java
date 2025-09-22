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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Opções comuns para todos os usuários
        addMenuButton("Consultar Imóveis", "Visualizar catálogo de imóveis", gbc, 0, row++, 
                     e -> openPropertySearch());
        
        addMenuButton("Meus Dados", "Visualizar e editar informações pessoais", gbc, 0, row++, 
                     e -> openUserProfile());
        
        // Opções específicas para administradores
        if (userType.equals("admin")) {
            addMenuSeparator(gbc, 0, row++);
            
            JLabel adminLabel = new JLabel("OPÇÕES DE ADMINISTRADOR");
            adminLabel.setFont(new Font("Arial", Font.BOLD, 14));
            adminLabel.setForeground(new Color(70, 130, 180));
            adminLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0; gbc.gridy = row++;
            menuPanel.add(adminLabel, gbc);
            
            addMenuButton("Gerenciar Usuários", "Criar e gerenciar contas de usuário", gbc, 0, row++, 
                         e -> openUserManagement());
            
            addMenuButton("Gerenciar Imóveis", "Adicionar e editar imóveis", gbc, 0, row++, 
                         e -> openPropertyManagement());
            
            addMenuButton("Relatórios", "Visualizar relatórios do sistema", gbc, 0, row++, 
                         e -> openReports());
        }
        
        // Opções específicas para corretores
        if (userType.equals("user")) {
            addMenuSeparator(gbc, 0, row++);
            
            addMenuButton("Meus Clientes", "Gerenciar clientes atribuídos", gbc, 0, row++, 
                         e -> openClientManagement());
            
            addMenuButton("Minhas Vendas", "Acompanhar vendas e comissões", gbc, 0, row++, 
                         e -> openSalesTracking());
        }
    }
    
    private void addMenuButton(String title, String description, GridBagConstraints gbc, 
                              int x, int y, ActionListener action) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        buttonPanel.setPreferredSize(new Dimension(400, 80));
        
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(Color.WHITE);
        button.setBorder(null);
        button.addActionListener(action);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);
        
        buttonPanel.add(button);
        
        gbc.gridx = x; gbc.gridy = y;
        menuPanel.add(buttonPanel, gbc);
    }
    
    private void addMenuSeparator(GridBagConstraints gbc, int x, int y) {
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(400, 1));
        gbc.gridx = x; gbc.gridy = y;
        menuPanel.add(separator, gbc);
    }
    
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja sair do sistema?", 
                "Confirmar Saída", 
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
    
    // Métodos para abrir diferentes telas (implementações futuras)
    private void openPropertySearch() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Consultar Imóveis");
    }
    
    private void openUserProfile() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Meus Dados");
    }
    
    private void openUserManagement() {
        SwingUtilities.invokeLater(() -> {
            UserManagementFrame userMgmtFrame = new UserManagementFrame(this);
            userMgmtFrame.setVisible(true);
        });
    }
    
    private void openPropertyManagement() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Gerenciar Imóveis");
    }
    
    private void openReports() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Relatórios");
    }
    
    private void openClientManagement() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Meus Clientes");
    }
    
    private void openSalesTracking() {
        JOptionPane.showMessageDialog(this, "Funcionalidade em desenvolvimento: Minhas Vendas");
    }
    
    // Getters para acesso aos dados do usuário
    public UserRecord getCurrentUser() {
        return currentUser;
    }
    
    public String getUserType() {
        return userType;
    }
}