package com.bibliotheque.controller;

import com.bibliotheque.entity.Auteur;
import com.bibliotheque.service.AuteurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auteurs")
@Tag(name = "Auteurs", description = "API de gestion des auteurs")
public class AuteurController {

    @Autowired
    private AuteurService auteurService;

    @GetMapping
    @Operation(summary = "Récupérer tous les auteurs")
    public ResponseEntity<List<Auteur>> getAllAuteurs() {
        return ResponseEntity.ok(auteurService.getAllAuteurs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un auteur par son ID")
    public ResponseEntity<Auteur> getAuteurById(@PathVariable Long id) {
        return auteurService.getAuteurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel auteur")
    public ResponseEntity<Auteur> createAuteur(@Valid @RequestBody Auteur auteur) {
        try {
            Auteur createdAuteur = auteurService.createAuteur(auteur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuteur);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un auteur")
    public ResponseEntity<Auteur> updateAuteur(@PathVariable Long id, @Valid @RequestBody Auteur auteur) {
        try {
            Auteur updatedAuteur = auteurService.updateAuteur(id, auteur);
            return ResponseEntity.ok(updatedAuteur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un auteur")
    public ResponseEntity<Void> deleteAuteur(@PathVariable Long id) {
        try {
            auteurService.deleteAuteur(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des auteurs par nom")
    public ResponseEntity<List<Auteur>> searchAuteurs(@RequestParam String nom) {
        return ResponseEntity.ok(auteurService.searchAuteurs(nom));
    }
}

