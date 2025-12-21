package com.bibliotheque.repository;

import com.bibliotheque.entity.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur ,Long> {
    List<Auteur> findByNomContainingIgnoreCase(String nom);
    List<Auteur> findByPrenomContainingIgnoreCase(String prenom);
    Optional<Auteur> findByNomAndPrenom(String nom, String prenom);
}
