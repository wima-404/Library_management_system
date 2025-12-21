package com.bibliotheque.service;

import com.bibliotheque.dto.LivreDTO;
import com.bibliotheque.entity.Auteur;
import com.bibliotheque.entity.Livre;
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



    public List<LivreDTO> getAllLivres() {
        return livreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public Optional<LivreDTO> getLivreById(Long id) {
        return livreRepository.findById(id)
                .map(this::convertToDTO);
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
