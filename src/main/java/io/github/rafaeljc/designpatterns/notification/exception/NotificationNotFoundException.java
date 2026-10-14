package io.github.rafaeljc.designpatterns.notification.exception;

import java.util.UUID;

public class NotificationNotFoundException extends RuntimeException {

	public NotificationNotFoundException(UUID id) {
		super("Notificação não encontrada: " + id);
	}

}
