package io.github.rafaeljc.designpatterns.notification.service;

import io.github.rafaeljc.designpatterns.notification.dto.NotificationRequest;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationService {

	Notification send(NotificationRequest request);

	List<Notification> findAll();

	Notification findById(UUID id);

}
