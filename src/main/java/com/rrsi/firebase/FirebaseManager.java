package com.rrsi.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe responsável por gerenciar a inicialização e operações do Firebase
 */
public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseAuth firebaseAuth;
    private boolean initialized = false;
    
    private FirebaseManager() {
        initializeFirebase();
    }
    
    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }
    
    /**
     * Inicializa o Firebase usando o arquivo de credenciais
     */
    private void initializeFirebase() {
        try {
            // Carrega o arquivo de credenciais do classpath
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("rrsi-imob-firebase-adminsdk-fbsvc-c0c45e9354.json");
            
            if (serviceAccount == null) {
                throw new IOException("Arquivo de credenciais do Firebase não encontrado");
            }
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            
            // Inicializa o Firebase apenas se ainda não foi inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            
            firebaseAuth = FirebaseAuth.getInstance();
            initialized = true;
            
            System.out.println("Firebase inicializado com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro ao inicializar Firebase: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Erro ao conectar com o Firebase: " + e.getMessage(), 
                "Erro de Conexão", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Verifica se o Firebase foi inicializado corretamente
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Obtém um usuário pelo email
     */
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        if (!isInitialized()) {
            throw new IllegalStateException("Firebase não foi inicializado");
        }
        return firebaseAuth.getUserByEmail(email);
    }
    
    /**
     * Cria um novo usuário no Firebase
     */
    public UserRecord createUser(String email, String password, String displayName) throws FirebaseAuthException {
        if (!isInitialized()) {
            throw new IllegalStateException("Firebase não foi inicializado");
        }
        
        CreateRequest request = new CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false);
        
        return firebaseAuth.createUser(request);
    }
    
    /**
     * Verifica se um usuário existe pelo email
     */
    public boolean userExists(String email) {
        try {
            getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }
}