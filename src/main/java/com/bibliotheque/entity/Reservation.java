package com.bibliotheque.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le livre est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @NotNull(message = "L'utilisateur est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation = LocalDate.now();

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    public enum StatutReservation {
        EN_ATTENTE,
        DISPONIBLE,
        ANNULEE,
        EXPIREE
    }
}

