package com.example.store.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/store")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/home")
    public ModelAndView homePage() {
        return new ModelAndView("/store/home");
    }

    @PostMapping("/register")
    public ModelAndView registerClient(Client client) {
        Client registeredClient = clientService.registerClient(client);
        ModelAndView modelAndView = new ModelAndView("/store/home");

        if (registeredClient != null) {
            // Inscription réussie
            modelAndView.addObject("message", "Inscription réussie !");
        } else {
            // Email déjà existant
            modelAndView.addObject("error", "Un client avec cet email existe déjà !");
        }

        return modelAndView;  // Retourne la page d'accueil après inscription
    }

    // Gérer la connexion
    @PostMapping("/store/login")
    public ModelAndView loginClient(String email, String password) {
        Client client = clientService.authenticateClient(email, password);
        ModelAndView modelAndView = new ModelAndView();

        if (client != null) {
            // Connexion réussie, redirection vers la page index
            modelAndView.setViewName("store/index");
            modelAndView.addObject("client", client);  // Ajouter le client à la vue
        } else {
            // Connexion échouée
            modelAndView.setViewName("store/home");
            modelAndView.addObject("error", "Identifiants incorrects !");
        }

        return modelAndView;  // Redirection vers index ou retour à home en cas d'erreur
    }

    // Page après connexion
    @GetMapping("/store/index")
    public ModelAndView indexPage() {
        return new ModelAndView("store/index");
    }

    // Déconnexion
    @GetMapping("/store/logout")
    public ModelAndView logout() {
        // Redirige vers la page d'accueil après déconnexion
        return new ModelAndView("redirect:/store/home");
    }
}
