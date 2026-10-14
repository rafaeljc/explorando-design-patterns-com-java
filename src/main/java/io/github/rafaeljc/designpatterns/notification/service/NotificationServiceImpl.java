package io.github.rafaeljc.designpatterns.notification.service;

import io.github.rafaeljc.designpatterns.notification.dto.NotificationRequest;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import io.github.rafaeljc.designpatterns.notification.service.usecase.GetNotificationUseCase;
import io.github.rafaeljc.designpatterns.notification.service.usecase.ListNotificationsUseCase;
import io.github.rafaeljc.designpatterns.notification.service.usecase.SendNotificationUseCase;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final SendNotificationUseCase sendNotification;
	private final ListNotificationsUseCase listNotifications;
	private final GetNotificationUseCase getNotification;

	public NotificationServiceImpl(
			SendNotificationUseCase sendNotification,
			ListNotificationsUseCase listNotifications,
			GetNotificationUseCase getNotification) {
		this.sendNotification = sendNotification;
		this.listNotifications = listNotifications;
		this.getNotification = getNotification;
	}

	@Override
	public Notification send(NotificationRequest request) {
		return sendNotification.execute(request);
	}

	@Override
	public List<Notification> findAll() {
		return listNotifications.execute();
	}

	@Override
	public Notification findById(UUID id) {
		return getNotification.execute(id);
	}

}
