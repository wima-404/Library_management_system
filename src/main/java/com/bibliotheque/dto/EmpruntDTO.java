package com.bibliotheque.dto;

import com.bibliotheque.entity.Emprunt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpruntDTO {
    private Long id;
    private Long livreId;
    private String livreTitre;
    private String livreIsbn;
    private Long utilisateurId;
    private String utilisateurNom;
    private String utilisateurEmail;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private Emprunt.StatutEmprunt statut;
    private Boolean prolonge;
}

