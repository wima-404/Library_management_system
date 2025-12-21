package com.bibliotheque.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "livres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotNull(message = "L'auteur est obligatoire")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Auteur auteur;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @Column(name = "nombre_pages")
    private Integer nombrePages;

    @Column(length = 50)
    private String genre;

    @Column(name = "nombre_exemplaires", nullable = false)
    private Integer nombreExemplaires = 1;

    @Column(name = "exemplaires_disponibles", nullable = false)
    private Integer exemplairesDisponibles = 1;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emprunt> emprunts;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}

