package io.github.rafaeljc.designpatterns.notification.repository;

import io.github.rafaeljc.designpatterns.notification.model.Notification;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
