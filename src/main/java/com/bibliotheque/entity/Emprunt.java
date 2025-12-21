package com.bibliotheque.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "emprunts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprunt {
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

    @Column(name = "date_emprunt", nullable = false)
    private LocalDate dateEmprunt = LocalDate.now();

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_effective")
    private LocalDate dateRetourEffective;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmprunt statut = StatutEmprunt.EN_COURS;

    @Column(name = "prolonge", nullable = false)
    private Boolean prolonge = false;

    public enum StatutEmprunt {
        EN_COURS,
        RETOURNE,
        EN_RETARD
    }
}

