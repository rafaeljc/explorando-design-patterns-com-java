package io.github.rafaeljc.designpatterns.notification.service.usecase;

import io.github.rafaeljc.designpatterns.notification.model.Notification;
import io.github.rafaeljc.designpatterns.notification.repository.NotificationRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListNotificationsUseCase {

	private final NotificationRepository repository;

	public ListNotificationsUseCase(NotificationRepository repository) {
		this.repository = repository;
	}

	public List<Notification> execute() {
		return repository.findAll();
	}

}
