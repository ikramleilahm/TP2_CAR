package com.example.store.Article;

import com.example.store.Commande.Commande;
import com.example.store.Commande.CommandeService;
import com.example.store.client.Client;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/store")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommandeService commandeService;

    @GetMapping("/commande/{id_commande}")
    public ModelAndView showArticles(@PathVariable Long id_commande, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("store/article");
        Client client = (Client) session.getAttribute("client");

        if (client != null) {
            Commande commande = commandeService.findById(id_commande);
            if (commande != null && commande.getClient().getEmail().equals(client.getEmail())) {
                if (commande.getArticles() == null) {
                    commande.setArticles(new ArrayList<>());
                }
                modelAndView.addObject("commande", commande);
            } else {
                modelAndView.setViewName("redirect:/store/index");
            }
        } else {
            modelAndView.setViewName("redirect:/home");
        }

        return modelAndView;
    }

    @PostMapping("/article/create")
    public ModelAndView create(@RequestParam Long id_commande, @RequestParam String libelle,
                               @RequestParam int quantite, @RequestParam double prix, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("redirect:/store/commande/" + id_commande);

        Client client = (Client) session.getAttribute("client");

        if (client != null) {
            Commande commande = commandeService.findById(id_commande);
            if (commande != null && commande.getClient().getEmail().equals(client.getEmail())) {
                Article newArticle = articleService.create(libelle, quantite, prix);
                if (commande.getArticles() == null) {
                    commande.setArticles(new ArrayList<>());
                }
                commande.getArticles().add(newArticle);
                commandeService.save(commande);
            }
        } else {
            modelAndView.setViewName("redirect:/home");
        }

        return modelAndView;
    }

    @PostMapping("/article/delete")
    public ModelAndView delete(@RequestParam Long id_commande, @RequestParam Long id_article, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("redirect:/store/commande/" + id_commande);

        Client client = (Client) session.getAttribute("client");

        if (client != null) {
            Commande commande = commandeService.findById(id_commande);
            Article article = articleService.findById(id_article);

            if (commande != null && article != null && commande.getClient().getEmail().equals(client.getEmail())) {
                commande.getArticles().removeIf(a -> a.getId_article().equals(id_article));
                commandeService.save(commande);
                articleService.delete(id_article);
            }
        } else {
            modelAndView.setViewName("redirect:/home");
        }

        return modelAndView;
    }
}
