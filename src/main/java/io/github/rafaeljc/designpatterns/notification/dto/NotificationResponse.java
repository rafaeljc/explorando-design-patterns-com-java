package io.github.rafaeljc.designpatterns.notification.dto;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import io.github.rafaeljc.designpatterns.notification.model.Priority;
import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
		UUID id,
		Channel channel,
		String recipient,
		String subject,
		String message,
		Priority priority,
		Instant createdAt) {

	public static NotificationResponse from(Notification notification) {
		return new NotificationResponse(
				notification.getId(),
				notification.getChannel(),
				notification.getRecipient(),
				notification.getSubject(),
				notification.getMessage(),
				notification.getPriority(),
				notification.getCreatedAt());
	}

}
