package com.bibliotheque.repository;

import com.bibliotheque.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByNomContainingIgnoreCase(String nom);
    List<Utilisateur> findByActif(Boolean actif);
}

