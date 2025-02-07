package com.example.store.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements ClientItf {

    @Autowired
    private ClientRepository repo;

    // Inscription d'un client
    @Override
    public Client registerClient(Client client) {
        // Vérifie si un client avec cet email existe déjà
        Client existingClient = repo.findByEmail(client.getEmail());
        if (existingClient != null) {
            return null;  // Si l'email existe déjà, retourner null
        }
        // Sauvegarder le nouveau client dans la base de données
        return repo.save(client);
    }

    // Authentification d'un client
    @Override
    public Client authenticateClient(String email, String password) {
        // Chercher le client par son email
        Client client = repo.findByEmail(email);
        if (client != null && client.getPassword().equals(password)) {
            return client; // Si les mots de passe correspondent, renvoyer l'objet client
        }
        return null;  // Si l'email ou le mot de passe ne correspond pas, retourner null
    }
}
