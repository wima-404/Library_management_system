package com.bibliotheque.repository;

import com.bibliotheque.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByUtilisateurId(Long utilisateurId);
    List<Emprunt> findByLivreId(Long livreId);
    List<Emprunt> findByStatut(Emprunt.StatutEmprunt statut);
    List<Emprunt> findByDateRetourPrevueBeforeAndStatut(LocalDate date, Emprunt.StatutEmprunt statut);
    List<Emprunt> findByDateRetourPrevueBeforeAndDateRetourEffectiveIsNull(LocalDate date);
}

