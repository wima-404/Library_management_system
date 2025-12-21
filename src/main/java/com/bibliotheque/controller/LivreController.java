package com.bibliotheque.controller;

import com.bibliotheque.dto.LivreDTO;
import com.bibliotheque.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
