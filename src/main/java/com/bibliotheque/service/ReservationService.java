package com.bibliotheque.service;

import com.bibliotheque.entity.Livre;
import com.bibliotheque.entity.Reservation;
import com.bibliotheque.entity.Utilisateur;
import com.bibliotheque.repository.LivreRepository;
import com.bibliotheque.repository.ReservationRepository;
import com.bibliotheque.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    private static final int DUREE_RESERVATION_JOURS = 2;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Long livreId, Long utilisateurId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (livre.getExemplairesDisponibles() > 0) {
            throw new RuntimeException("Le livre est disponible, vous pouvez l'emprunter directement");
        }

        List<Reservation> reservationsExistantes = reservationRepository
                .findByLivreIdAndStatut(livreId, Reservation.StatutReservation.EN_ATTENTE);

        if (reservationsExistantes.stream().anyMatch(r -> r.getUtilisateur().getId().equals(utilisateurId))) {
            throw new RuntimeException("Vous avez déjà une réservation en attente pour ce livre");
        }

        Reservation reservation = new Reservation();
        reservation.setLivre(livre);
        reservation.setUtilisateur(utilisateur);
        reservation.setDateReservation(LocalDate.now());
        reservation.setStatut(Reservation.StatutReservation.EN_ATTENTE);

        return reservationRepository.save(reservation);
    }

    public void annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        reservation.setStatut(Reservation.StatutReservation.ANNULEE);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUtilisateur(Long utilisateurId) {
        return reservationRepository.findByUtilisateurId(utilisateurId);
    }

    public List<Reservation> getReservationsByLivre(Long livreId) {
        return reservationRepository.findByLivreId(livreId);
    }

    public void traiterReservationsLivreDisponible(Long livreId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        if (livre.getExemplairesDisponibles() > 0) {
            List<Reservation> reservationsEnAttente = reservationRepository
                    .findByLivreIdAndStatut(livreId, Reservation.StatutReservation.EN_ATTENTE);

            if (!reservationsEnAttente.isEmpty()) {
                Reservation premiereReservation = reservationsEnAttente.get(0);
                premiereReservation.setStatut(Reservation.StatutReservation.DISPONIBLE);
                premiereReservation.setDateExpiration(LocalDate.now().plusDays(DUREE_RESERVATION_JOURS));
                reservationRepository.save(premiereReservation);

                emailService.envoyerEmailDisponibilite(
                        premiereReservation.getUtilisateur().getEmail(),
                        livre.getTitre()
                );
            }
        }
    }

    public void expirerReservations() {
        LocalDate aujourdhui = LocalDate.now();
        List<Reservation> reservationsExpirees = reservationRepository
                .findByDateExpirationBeforeAndStatut(aujourdhui, Reservation.StatutReservation.DISPONIBLE);

        for (Reservation reservation : reservationsExpirees) {
            reservation.setStatut(Reservation.StatutReservation.EXPIREE);
            reservationRepository.save(reservation);

            traiterReservationsLivreDisponible(reservation.getLivre().getId());
        }
    }
}

