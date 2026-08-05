package com.library.service;

import com.library.dao.NotificationDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.impl.NotificationDAOImpl;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.model.Notification;
import com.library.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationDAO notificationDAO;
    private final TransactionDAO transactionDAO;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        scheduleDailyCheck();
    }

    public void createNotification(int memberId, String message, String type) {
        notificationDAO.createNotification(new Notification(0, memberId, message, type, LocalDateTime.now(), false));
    }

    public List<Notification> getNotificationsForMember(int memberId) {
        return notificationDAO.getNotificationsForMember(memberId);
    }

    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }

    public void markAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    public void runDailyCheck() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        for (Transaction t : transactions) {
            if (t.getReturnDate() != null) {
                continue;
            }
            if (t.getDueDate() != null && LocalDateTime.now().plusDays(2).isAfter(t.getDueDate()) && LocalDateTime.now().isBefore(t.getDueDate())) {
                createNotification(t.getMemberId(), "Due date reminder: return your book before " + t.getDueDate(), "DUE_REMINDER");
                log.info("Generated due reminder notification for member {}", t.getMemberId());
            }
            if (t.getDueDate() != null && LocalDateTime.now().isAfter(t.getDueDate())) {
                long daysLate = ChronoUnit.DAYS.between(t.getDueDate(), LocalDateTime.now());
                createNotification(t.getMemberId(), "Overdue book alert. Current fine amount: " + (daysLate * 10) + ".00", "OVERDUE");
                log.info("Generated overdue notification for member {}", t.getMemberId());
            }
        }
    }

    private void scheduleDailyCheck() {
        scheduler.scheduleAtFixedRate(this::runDailyCheck, 0, 1, TimeUnit.DAYS);
    }
}
