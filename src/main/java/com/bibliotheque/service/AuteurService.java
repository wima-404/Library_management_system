package com.bibliotheque.service;

import com.bibliotheque.entity.Auteur;
import com.bibliotheque.repository.AuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuteurService {

    @Autowired
    private AuteurRepository auteurRepository;

    public List<Auteur> getAllAuteurs() {
        return auteurRepository.findAll();
    }

    public Optional<Auteur> getAuteurById(Long id) {
        return auteurRepository.findById(id);
    }

    public Auteur createAuteur(Auteur auteur) {
        return auteurRepository.save(auteur);
    }

    public Auteur updateAuteur(Long id, Auteur auteurDetails) {
        Auteur auteur = auteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé avec l'id : " + id));

        auteur.setNom(auteurDetails.getNom());
        auteur.setPrenom(auteurDetails.getPrenom());
        auteur.setDateNaissance(auteurDetails.getDateNaissance());
        auteur.setDateDeces(auteurDetails.getDateDeces());
        auteur.setNationalite(auteurDetails.getNationalite());
        auteur.setBiographie(auteurDetails.getBiographie());

        return auteurRepository.save(auteur);
    }

    public void deleteAuteur(Long id) {
        Auteur auteur = auteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé avec l'id : " + id));
        auteurRepository.delete(auteur);
    }

    public List<Auteur> searchAuteurs(String nom) {
        return auteurRepository.findByNomContainingIgnoreCase(nom);
    }
}

