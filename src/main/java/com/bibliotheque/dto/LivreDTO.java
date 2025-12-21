package com.bibliotheque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreDTO {
    private Long id;
    private String titre;
    private String isbn;
    private Long auteurId;
    private String auteurNom;
    private String auteurPrenom;
    private LocalDate datePublication;
    private Integer nombrePages;
    private String genre;
    private Integer nombreExemplaires;
    private Integer exemplairesDisponibles;
    private String description;
    private List<String> auteursNoms;
}

