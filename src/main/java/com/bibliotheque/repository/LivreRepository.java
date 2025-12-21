package com.bibliotheque.repository;

import com.bibliotheque.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre ,Long> {
    Optional<Livre> findByIsbn(String isbn);
    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurId(Long auteurId);
    List<Livre> findByGenre(String genre);
    List<Livre> findByExemplairesDisponiblesGreaterThan(Integer nombre);
}

