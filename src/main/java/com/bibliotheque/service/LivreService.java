package com.bibliotheque.service;

import com.bibliotheque.dto.LivreDTO;
import com.bibliotheque.entity.Auteur;
import com.bibliotheque.entity.Livre;
import com.bibliotheque.repository.AuteurRepository;
import com.bibliotheque.repository.LivreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LivreService {
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AuteurRepository auteurRepository;

    public List<LivreDTO> getAllLivres() {
        return livreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public Optional<LivreDTO> getLivreById(Long id) {
        return livreRepository.findById(id)
                .map(this::convertToDTO);
    }
    public LivreDTO createLivre(Livre livre) {
        Auteur auteur = auteurRepository.findById(livre.getAuteur().getId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé"));

        livre.setAuteur(auteur);
        livre.setExemplairesDisponibles(livre.getNombreExemplaires());

        Livre savedLivre = livreRepository.save(livre);
        return convertToDTO(savedLivre);
    }
    public LivreDTO updateLivre(Long id, Livre livreDetails) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'id : " + id));

        Auteur auteur = auteurRepository.findById(livreDetails.getAuteur().getId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé"));

        int ancienNombreExemplaires = livre.getNombreExemplaires();
        int nouveauNombreExemplaires = livreDetails.getNombreExemplaires();
        int difference = nouveauNombreExemplaires - ancienNombreExemplaires;

        livre.setTitre(livreDetails.getTitre());
        livre.setIsbn(livreDetails.getIsbn());
        livre.setAuteur(auteur);
        livre.setDatePublication(livreDetails.getDatePublication());
        livre.setNombrePages(livreDetails.getNombrePages());
        livre.setGenre(livreDetails.getGenre());
        livre.setDescription(livreDetails.getDescription());
        livre.setNombreExemplaires(nouveauNombreExemplaires);
        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles() + difference);

        Livre updatedLivre = livreRepository.save(livre);
        return convertToDTO(updatedLivre);
    }
    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'id : " + id));
        livreRepository.delete(livre);
    }

    public List<LivreDTO> searchLivres(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<LivreDTO> getLivresByAuteur(Long auteurId) {
        return livreRepository.findByAuteurId(auteurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LivreDTO> getLivresDisponibles() {
        return livreRepository.findByExemplairesDisponiblesGreaterThan(0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private LivreDTO convertToDTO(Livre livre) {
        LivreDTO dto = new LivreDTO();
        dto.setId(livre.getId());
        dto.setTitre(livre.getTitre());
        dto.setIsbn(livre.getIsbn());
        dto.setAuteurId(livre.getAuteur().getId());
        dto.setAuteurNom(livre.getAuteur().getNom());
        dto.setAuteurPrenom(livre.getAuteur().getPrenom());
        dto.setDatePublication(livre.getDatePublication());
        dto.setNombrePages(livre.getNombrePages());
        dto.setGenre(livre.getGenre());
        dto.setNombreExemplaires(livre.getNombreExemplaires());
        dto.setExemplairesDisponibles(livre.getExemplairesDisponibles());
        dto.setDescription(livre.getDescription());
        return dto;
    }


}
