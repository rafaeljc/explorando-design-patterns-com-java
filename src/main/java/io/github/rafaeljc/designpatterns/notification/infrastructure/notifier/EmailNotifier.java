package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import io.github.rafaeljc.designpatterns.notification.exception.InvalidNotificationException;
import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotifier implements Notifier {

	private static final Logger log = LoggerFactory.getLogger(EmailNotifier.class);
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	@Override
	public Channel channel() {
		return Channel.EMAIL;
	}

	@Override
	public void send(Notification notification) {
		if (!EMAIL_PATTERN.matcher(notification.getRecipient()).matches()) {
			throw new InvalidNotificationException("O destinatário não é um e-mail válido");
		}
		if (notification.getSubject() == null || notification.getSubject().isBlank()) {
			throw new InvalidNotificationException("O assunto é obrigatório para e-mail");
		}
		log.info("event=notification.sent channel={} priority={} subject=\"{}\"",
				channel(), notification.getPriority(), notification.getSubject());
	}

}
