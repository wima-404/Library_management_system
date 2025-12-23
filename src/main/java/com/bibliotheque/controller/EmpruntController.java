package com.bibliotheque.controller;

import com.bibliotheque.dto.EmpruntDTO;
import com.bibliotheque.service.EmpruntService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprunts")
@Tag(name = "Emprunts", description = "API de gestion des emprunts")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @GetMapping
    @Operation(summary = "Récupérer tous les emprunts")
    public ResponseEntity<List<EmpruntDTO>> getAllEmprunts() {
        return ResponseEntity.ok(empruntService.getAllEmprunts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un emprunt par son ID")
    public ResponseEntity<EmpruntDTO> getEmpruntById(@PathVariable Long id) {
        return empruntService.getEmpruntById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel emprunt")
    public ResponseEntity<EmpruntDTO> createEmprunt(@RequestBody Map<String, Long> request) {
        try {
            Long livreId = request.get("livreId");
            Long utilisateurId = request.get("utilisateurId");
            EmpruntDTO createdEmprunt = empruntService.createEmprunt(livreId, utilisateurId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/retour")
    @Operation(summary = "Retourner un livre")
    public ResponseEntity<EmpruntDTO> retournerLivre(@PathVariable Long id) {
        try {
            EmpruntDTO emprunt = empruntService.retournerLivre(id);
            return ResponseEntity.ok(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/prolonger")
    @Operation(summary = "Prolonger un emprunt")
    public ResponseEntity<EmpruntDTO> prolongerEmprunt(@PathVariable Long id) {
        try {
            EmpruntDTO emprunt = empruntService.prolongerEmprunt(id);
            return ResponseEntity.ok(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(summary = "Récupérer les emprunts d'un utilisateur")
    public ResponseEntity<List<EmpruntDTO>> getEmpruntsByUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(empruntService.getEmpruntsByUtilisateur(utilisateurId));
    }

    @GetMapping("/retard")
    @Operation(summary = "Récupérer les emprunts en retard")
    public ResponseEntity<List<EmpruntDTO>> getEmpruntsEnRetard() {
        return ResponseEntity.ok(empruntService.getEmpruntsEnRetard());
    }

    @PostMapping("/notifier-retard")
    @Operation(summary = "Notifier les emprunts en retard par email")
    public ResponseEntity<Void> notifierEmpruntsEnRetard() {
        empruntService.notifierEmpruntsEnRetard();
        return ResponseEntity.ok().build();
    }
}

