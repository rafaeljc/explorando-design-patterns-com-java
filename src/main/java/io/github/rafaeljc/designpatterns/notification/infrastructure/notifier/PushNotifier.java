package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushNotifier implements Notifier {

	private static final Logger log = LoggerFactory.getLogger(PushNotifier.class);

	@Override
	public Channel channel() {
		return Channel.PUSH;
	}

	@Override
	public void send(Notification notification) {
		// O token (destinatário) já é garantido pelo Builder
		log.info("event=notification.sent channel={} priority={}", channel(), notification.getPriority());
	}

}
