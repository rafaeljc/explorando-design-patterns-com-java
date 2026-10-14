package io.github.rafaeljc.designpatterns.notification.service.usecase;

import io.github.rafaeljc.designpatterns.notification.dto.NotificationRequest;
import io.github.rafaeljc.designpatterns.notification.infrastructure.notifier.NotifierFactory;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import io.github.rafaeljc.designpatterns.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationUseCase {

	private static final Logger log = LoggerFactory.getLogger(SendNotificationUseCase.class);

	private final NotifierFactory notifierFactory;
	private final NotificationRepository repository;

	public SendNotificationUseCase(NotifierFactory notifierFactory, NotificationRepository repository) {
		this.notifierFactory = notifierFactory;
		this.repository = repository;
	}

	public Notification execute(NotificationRequest request) {
		Notification.Builder builder = Notification.builder(request.channel(), request.recipient(), request.message())
				.withSubject(request.subject());
		if (request.priority() != null) {
			builder.withPriority(request.priority());
		}
		Notification notification = builder.build();

		notifierFactory.notifierFor(notification.getChannel()).send(notification);

		Notification saved = repository.save(notification);
		log.info("event=notification.saved id={} channel={}", saved.getId(), saved.getChannel());
		return saved;
	}

}
