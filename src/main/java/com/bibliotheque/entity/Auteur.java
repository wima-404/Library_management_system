package com.bibliotheque.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "auteurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_deces")
    private LocalDate dateDeces;

    private String nationalite;

    @Column(length = 1000)
    private String biographie;

    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Livre> livres;
}

