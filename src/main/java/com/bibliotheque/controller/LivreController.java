package com.bibliotheque.controller;

import com.bibliotheque.dto.LivreDTO;
import com.bibliotheque.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
