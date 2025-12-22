package com.bibliotheque.repository;

import com.bibliotheque.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUtilisateurId(Long utilisateurId);
    List<Reservation> findByLivreId(Long livreId);
    List<Reservation> findByStatut(Reservation.StatutReservation statut);
    List<Reservation> findByDateExpirationBeforeAndStatut(LocalDate date, Reservation.StatutReservation statut);
    List<Reservation> findByLivreIdAndStatut(Long livreId, Reservation.StatutReservation statut);
}

