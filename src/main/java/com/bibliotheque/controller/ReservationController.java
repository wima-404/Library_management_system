package com.bibliotheque.controller;

import com.bibliotheque.entity.Reservation;
import com.bibliotheque.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "API de gestion des réservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les réservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par son ID")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle réservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody Map<String, Long> request) {
        try {
            Long livreId = request.get("livreId");
            Long utilisateurId = request.get("utilisateurId");
            Reservation createdReservation = reservationService.createReservation(livreId, utilisateurId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/annuler")
    @Operation(summary = "Annuler une réservation")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long id) {
        try {
            reservationService.annulerReservation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    @Operation(summary = "Récupérer les réservations d'un utilisateur")
    public ResponseEntity<List<Reservation>> getReservationsByUtilisateur(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(reservationService.getReservationsByUtilisateur(utilisateurId));
    }

    @GetMapping("/livre/{livreId}")
    @Operation(summary = "Récupérer les réservations d'un livre")
    public ResponseEntity<List<Reservation>> getReservationsByLivre(@PathVariable Long livreId) {
        return ResponseEntity.ok(reservationService.getReservationsByLivre(livreId));
    }
}

