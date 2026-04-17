package com.solvative.book_vault.events;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OverdueLoanEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OverdueLoanEventHandler.class);

    @Async
    @EventListener
    public void handle(LoanOverdueEvent event) {
        log.info("Notification sent for overdue loan={}, member={}, book={}",
                event.getLoanId(), event.getMemberId(), event.getBookId());
    }
}
