package com.bibliotheque.scheduler;

import com.bibliotheque.service.EmpruntService;
import com.bibliotheque.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private ReservationService reservationService;

    @Scheduled(cron = "0 0 9 * * *") // Tous les jours à 9h
    public void notifierEmpruntsEnRetard() {
        empruntService.notifierEmpruntsEnRetard();
    }

    @Scheduled(cron = "0 0 * * * *") // Toutes les heures
    public void expirerReservations() {
        reservationService.expirerReservations();
    }
}

