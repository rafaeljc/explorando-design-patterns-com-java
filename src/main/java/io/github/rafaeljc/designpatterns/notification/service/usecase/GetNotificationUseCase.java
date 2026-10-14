package io.github.rafaeljc.designpatterns.notification.service.usecase;

import io.github.rafaeljc.designpatterns.notification.exception.NotificationNotFoundException;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import io.github.rafaeljc.designpatterns.notification.repository.NotificationRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class GetNotificationUseCase {

	private final NotificationRepository repository;

	public GetNotificationUseCase(NotificationRepository repository) {
		this.repository = repository;
	}

	public Notification execute(UUID id) {
		return repository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
	}

}
