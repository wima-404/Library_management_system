package com.bibliotheque.controller;

import com.bibliotheque.dto.LivreDTO;
import com.bibliotheque.entity.Livre;
import com.bibliotheque.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
@Tag(name = "Livres", description = "API de gestion des livres")
public class LivreController {
    @Autowired
    private LivreService livreService;

    @GetMapping
    @Operation(summary = "Récupérer tous les livres ")
    public ResponseEntity<List<LivreDTO>> getAllLivres() {
        return ResponseEntity.ok(livreService.getAllLivres());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livre par son ID")
    public ResponseEntity<LivreDTO> getLivreById(@PathVariable Long id) {
        return livreService.getLivreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @Operation(summary = "Créer un nouveau livre")
    public ResponseEntity<LivreDTO> createLivre(@Valid @RequestBody Livre livre) {
        try {
            LivreDTO createdLivre = livreService.createLivre(livre);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivre);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un livre")
    public ResponseEntity<LivreDTO> updateLivre(@PathVariable Long id, @Valid @RequestBody Livre livre) {
        try {
            LivreDTO updatedLivre = livreService.updateLivre(id, livre);
            return ResponseEntity.ok(updatedLivre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre")
    public ResponseEntity<Void> deleteLivre(@PathVariable Long id) {
        try {
            livreService.deleteLivre(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    @Operation(summary = "Rechercher des livres par titre")
    public ResponseEntity<List<LivreDTO>> searchLivres(@RequestParam String titre) {
        return ResponseEntity.ok(livreService.searchLivres(titre));
    }
    @GetMapping("/auteur/{auteurId}")
    @Operation(summary = "Récupérer les livres d'un auteur")
    public ResponseEntity<List<LivreDTO>> getLivresByAuteur(@PathVariable Long auteurId) {
        return ResponseEntity.ok(livreService.getLivresByAuteur(auteurId));
    }
    @GetMapping("/disponibles")
    @Operation(summary = "Récupérer les livres disponibles")
    public ResponseEntity<List<LivreDTO>> getLivresDisponibles() {
        return ResponseEntity.ok(livreService.getLivresDisponibles());
    }
}



