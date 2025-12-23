package com.bibliotheque.service;

import com.bibliotheque.dto.EmpruntDTO;
import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Livre;
import com.bibliotheque.entity.Utilisateur;
import com.bibliotheque.repository.EmpruntRepository;
import com.bibliotheque.repository.LivreRepository;
import com.bibliotheque.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReservationService reservationService;

    private static final int DUREE_EMPRUNT_JOURS = 14;

    public List<EmpruntDTO> getAllEmprunts() {
        return empruntRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpruntDTO> getEmpruntById(Long id) {
        return empruntRepository.findById(id)
                .map(this::convertToDTO);
    }

    public EmpruntDTO createEmprunt(Long livreId, Long utilisateurId) {
        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (livre.getExemplairesDisponibles() <= 0) {
            throw new RuntimeException("Aucun exemplaire disponible pour ce livre");
        }

        Emprunt emprunt = new Emprunt();
        emprunt.setLivre(livre);
        emprunt.setUtilisateur(utilisateur);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(DUREE_EMPRUNT_JOURS));
        emprunt.setStatut(Emprunt.StatutEmprunt.EN_COURS);

        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles() - 1);
        livreRepository.save(livre);

        Emprunt savedEmprunt = empruntRepository.save(emprunt);
        return convertToDTO(savedEmprunt);
    }

    public EmpruntDTO retournerLivre(Long id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        if (emprunt.getDateRetourEffective() != null) {
            throw new RuntimeException("Ce livre a déjà été retourné");
        }

        emprunt.setDateRetourEffective(LocalDate.now());
        emprunt.setStatut(Emprunt.StatutEmprunt.RETOURNE);

        Livre livre = emprunt.getLivre();
        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles() + 1);
        livreRepository.save(livre);

        Emprunt updatedEmprunt = empruntRepository.save(emprunt);

        // Traiter les réservations en attente pour ce livre
        reservationService.traiterReservationsLivreDisponible(livre.getId());

        return convertToDTO(updatedEmprunt);
    }

    public EmpruntDTO prolongerEmprunt(Long id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

        if (emprunt.getProlonge()) {
            throw new RuntimeException("Cet emprunt a déjà été prolongé");
        }

        emprunt.setDateRetourPrevue(emprunt.getDateRetourPrevue().plusDays(DUREE_EMPRUNT_JOURS));
        emprunt.setProlonge(true);

        Emprunt updatedEmprunt = empruntRepository.save(emprunt);
        return convertToDTO(updatedEmprunt);
    }

    public List<EmpruntDTO> getEmpruntsByUtilisateur(Long utilisateurId) {
        return empruntRepository.findByUtilisateurId(utilisateurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmpruntDTO> getEmpruntsEnRetard() {
        LocalDate aujourdhui = LocalDate.now();
        List<Emprunt> empruntsEnRetard = empruntRepository
                .findByDateRetourPrevueBeforeAndDateRetourEffectiveIsNull(aujourdhui);

        empruntsEnRetard.forEach(emprunt -> {
            if (emprunt.getStatut() != Emprunt.StatutEmprunt.EN_RETARD) {
                emprunt.setStatut(Emprunt.StatutEmprunt.EN_RETARD);
                empruntRepository.save(emprunt);
            }
        });

        return empruntsEnRetard.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void notifierEmpruntsEnRetard() {
        List<EmpruntDTO> empruntsEnRetard = getEmpruntsEnRetard();

        for (EmpruntDTO emprunt : empruntsEnRetard) {
            emailService.envoyerEmailRetard(
                    emprunt.getUtilisateurEmail(),
                    emprunt.getLivreTitre(),
                    emprunt.getDateRetourPrevue().toString()
            );
        }
    }

    private EmpruntDTO convertToDTO(Emprunt emprunt) {
        EmpruntDTO dto = new EmpruntDTO();
        dto.setId(emprunt.getId());
        dto.setLivreId(emprunt.getLivre().getId());
        dto.setLivreTitre(emprunt.getLivre().getTitre());
        dto.setLivreIsbn(emprunt.getLivre().getIsbn());
        dto.setUtilisateurId(emprunt.getUtilisateur().getId());
        dto.setUtilisateurNom(emprunt.getUtilisateur().getNom() + " " + emprunt.getUtilisateur().getPrenom());
        dto.setUtilisateurEmail(emprunt.getUtilisateur().getEmail());
        dto.setDateEmprunt(emprunt.getDateEmprunt());
        dto.setDateRetourPrevue(emprunt.getDateRetourPrevue());
        dto.setDateRetourEffective(emprunt.getDateRetourEffective());
        dto.setStatut(emprunt.getStatut());
        dto.setProlonge(emprunt.getProlonge());
        return dto;
    }
}

