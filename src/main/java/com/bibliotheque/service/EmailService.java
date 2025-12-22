package com.bibliotheque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailRetard(String destinataire, String nomLivre, String dateRetourPrevue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject("Rappel : Emprunt en retard");
        message.setText(String.format(
                "Bonjour,\n\n" +
                        "Nous vous rappelons que vous avez un emprunt en retard.\n\n" +
                        "Livre : %s\n" +
                        "Date de retour prévue : %s\n\n" +
                        "Merci de retourner le livre au plus vite.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de la bibliothèque",
                nomLivre, dateRetourPrevue
        ));
        mailSender.send(message);
    }

    public void envoyerEmailDisponibilite(String destinataire, String nomLivre) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject("Livre disponible");
        message.setText(String.format(
                "Bonjour,\n\n" +
                        "Le livre que vous avez réservé est maintenant disponible.\n\n" +
                        "Livre : %s\n\n" +
                        "Vous avez 48 heures pour venir l'emprunter.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de la bibliothèque",
                nomLivre
        ));
        mailSender.send(message);
    }
}

